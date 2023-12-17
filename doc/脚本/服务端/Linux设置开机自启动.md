# 一、方式一（推荐）

## 1.创建一个新的服务单元文件

执行以下命令创建一个新的服务单元文件，例如 phoenix_server.service

```shell
sudo nano /etc/systemd/system/phoenix_server.service
```

## 2.在打开的文件中，添加以下内容

```shell
[Unit]
Description=phoenix监控服务端
After=network.target syslog.target

[Service]
User=root
Group=root
WorkingDirectory=/path/to/your/app
ExecStart=/usr/bin/java -jar -Ddruid.mysql.usePingMethod=false /path/to/your/app/phoenix-server.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=60s
ExecReload=/bin/kill -HUP $MAINPID

[Install]
WantedBy=multi-user.target
```

① 请将 **/usr/bin/java** 替换为您服务器的Java可执行文件所在路径，如果不知道路径，可以用命令 **whereis java** 查询。  
② 请将 **/path/to/your/app** 替换为您的应用程序的实际路径，请确保用户、路径和文件名为您实际的应用程序信息，以确保正确的配置和启动。

## 3.保存并关闭文件

## 4.重新加载 systemd 配置

执行以下命令重新加载 systemd 配置：

```shell
sudo systemctl daemon-reload
```

## 5.将服务添加到开机启动项中

执行以下命令将服务添加到开机启动项中：

```shell
sudo systemctl enable phoenix_server.service
```

## 6.启动、停止、重启服务，查看服务状态

### 1)启动

```shell
sudo systemctl start phoenix_server.service
```

 ### 2)停止

```shell
sudo systemctl stop phoenix_server.service
```

### 3)重启

```shell
sudo systemctl restart phoenix_server.service
```

### 4)查看服务状态

```shell
systemctl status phoenix_server.service
```

## 7.问题诊断

在 systemd 中，可以使用以下命令来查看服务开机自启动失败的日志：

### 1)使用 journalctl 命令查看系统日志

```shell
sudo journalctl -u <service_name>.service
```

其中 `<service_name>` 是服务的名称。通过运行上述命令，您将只显示指定服务的日志。

### 2)查看引导过程的日志（包括服务启动）

```shell
sudo journalctl -b
```

该命令将显示最近一次引导的整个日志，其中包括服务启动和其他相关信息。

### 3)使用 systemctl 命令查看服务状态和日志

```shell
systemctl status <service_name>.service
```

该命令将显示服务的当前状态，包括最近的日志记录片段。  
  
这些命令将显示与指定服务相关的日志条目，帮助您确定服务开机自启动失败的原因。您可以检查日志中的错误消息、警告和其他有关服务启动过程的详细信息，以便进一步诊断问题。

# 二、方式二

## 1.创建一个启动脚本

执行以下命令创建一个新的启动脚本：

```shell
sudo nano /etc/init.d/phoenix_server
```

**注意：init脚本通常用于旧版的Linux发行版，如CentOS 6及更早版本中。现在，systemd已经成为许多Linux发行版的默认服务管理器，如果您的服务器不支持init脚本，请使用其它方式设置开机自启动。**

## 2.在打开的文件中，添加以下内容

```shell
#!/usr/bin/env bash

### BEGIN INIT INFO
# Provides:          phoenix_server
# Required-Start:    $network $syslog
# Required-Stop:     $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Description:       phoenix监控服务端
### END INIT INFO

#替换为您的应用程序的实际路径
cd /path/to/your/app
#包名
packageName="phoenix-server.jar"
#程序名
programName="phoenix-server"

#检测状态
function status() {
  echo "---------------------------------------检测状态---------------------------------------"
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  if [ ${pid} ]; then
    echo "${programName}正在运行，进程ID：${pid}"
  else
    echo "${programName}未运行！"
  fi
}

#停止程序
function stop() {
  echo "---------------------------------------停止程序---------------------------------------"
  #打印出当前的进程，grep -v grep 去掉grep进程
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  #查询进程个数：wc -l 返回行数
  count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
  echo "${programName}进程ID：$pid，进程个数：$count"
  #关闭进程
  if (($count > 0)); then
    kill $pid
    #打印关掉的进程ID
    echo "关闭进程：$pid"
    count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
    sec=5
    sum=12
    #开始一个循环
    while (($sum > 0)); do
      if (($count > 0)); then
        #若进程还未关闭，则脚本sleep几秒
        sleep $sec
        count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
      else
        #若进程已经关闭，则跳出循环
        echo "${programName}已经关闭！"
        break
      fi
      sum=$(($sum - 1))
    done
    #超时不能停止，强制杀掉进程
    if (($count > 0)); then
      kill -9 $pid
      echo "${programName}被强制关闭！"
      sleep 1
    fi
  else
    echo "${programName}未运行！"
  fi
}

#启动程序
function start() {
  echo "---------------------------------------启动程序---------------------------------------"
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  if [ ${pid} ]; then
    echo "${programName}正在运行，请先停止程序！"
  else
    #启动进程
    nohup java -jar -Ddruid.mysql.usePingMethod=false ${packageName} --spring.profiles.active=prod >/dev/null 2>&1 &
    pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
    if [ ${pid} ]; then
      echo "${programName}已经启动，进程ID为：$pid"
    else
      #等待15秒
      sleep 15
      pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
      if [ !${pid} ]; then
        echo "${programName}启动失败！"
      fi
    fi
  fi
}

#启动时带参数，根据参数执行
if [ ${#} -ge 1 ]; then
  case ${1} in
  "start")
    start
    ;;
  "restart")
    stop
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  *)
    echo "${1}无任何操作"
    ;;
  esac
else
  echo "
    start：启动
    stop：停止
    restart：重启
    status：检查状态
    示例命令如：service phoenix_server start
    "
fi
```

请将 **/path/to/your/app** 替换为您的应用程序的实际路径。

## 3.保存并关闭文件

## 4.设置脚本的权限

执行以下命令设置脚本的执行权限：

```shell
sudo chmod +x /etc/init.d/phoenix_server
```
## 5.添加到开机启动项中

执行以下命令将服务添加到开机启动项中：

```shell
sudo chkconfig --add phoenix_server
```

## 6.启动、停止、重启服务，查看服务状态

 ### 1)启动

```shell
sudo service phoenix_server start
```

 ### 2)停止

```shell
sudo service phoenix_server stop
```

### 3)重启

```shell
sudo service phoenix_server restart
```

### 4)查看服务状态

```shell
sudo service phoenix_server status
```