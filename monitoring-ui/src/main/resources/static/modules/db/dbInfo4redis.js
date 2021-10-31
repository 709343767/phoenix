/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'element', 'layer', 'form'], function () {
        var $ = layui.$, admin = layui.admin, element = layui.element, layer = layui.layer, form = layui.form;
        // 是否自动刷新
        var redisAutoRefresh = true;
        form.on('checkbox(redisAutoRefresh)', function (data) {
            //是否被选中，true或者false
            redisAutoRefresh = data.elem.checked;
        });

        // 获取redis信息
        function getRedisInfo() {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'db-info4redis/get-redis-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    id: id, // 数据库主键ID
                },
                success: function (result) {
                    var info = result.data;
                    // 定义要展示的内容
                    var server, clients, memory, persistence, stats, replication, cpu, cluster, keyspace;
                    var redisVersion, os, processId, usedMemoryHuman, usedMemoryPeakHuman, usedMemoryLua,
                        connectedClients, totalConnectionsReceived, totalCommandsProcessed;
                    if (!isEmpty(info)) {
                        info = info.replace(new RegExp('\r\n\r\n', 'g'), '<br><br>')
                            .replace(new RegExp('\n\r\n\r', 'g'), '<br><br>')
                            .replace(new RegExp('\n\r', 'g'), '<br>')
                            .replace(new RegExp('\r\n', 'g'), '<br>');
                        var infos = info.split('<br><br>');
                        for (var i = 0; i < infos.length; i++) {
                            if (infos[i].indexOf('# Server') !== -1) {
                                server = infos[i].replace('# Server<br>', '');
                            }
                            if (infos[i].indexOf('# Clients') !== -1) {
                                clients = infos[i].replace('# Clients<br>', '');
                            }
                            if (infos[i].indexOf('# Memory') !== -1) {
                                memory = infos[i].replace('# Memory<br>', '');
                            }
                            if (infos[i].indexOf('# Persistence') !== -1) {
                                persistence = infos[i].replace('# Persistence<br>', '');
                            }
                            if (infos[i].indexOf('# Stats') !== -1) {
                                stats = infos[i].replace('# Stats<br>', '');
                            }
                            if (infos[i].indexOf('# Replication') !== -1) {
                                replication = infos[i].replace('# Replication<br>', '');
                            }
                            if (infos[i].indexOf('# CPU') !== -1) {
                                cpu = infos[i].replace('# CPU<br>', '');
                            }
                            if (infos[i].indexOf('# Cluster') !== -1) {
                                cluster = infos[i].replace('# Cluster<br>', '');
                            }
                            if (infos[i].indexOf('# Keyspace') !== -1) {
                                keyspace = infos[i].replace('# Keyspace', '');
                            }
                        }
                    }
                    // Redis版本
                    redisVersion = server.split('<br>').find(function (value) {
                        return value.indexOf('redis_version:') !== -1;
                    }).slice('redis_version:'.length);
                    // OS
                    os = server.split('<br>').find(function (value) {
                        return value.indexOf('os:') !== -1;
                    }).slice('os:'.length);
                    // 进程ID
                    processId = server.split('<br>').find(function (value) {
                        return value.indexOf('process_id:') !== -1;
                    }).slice('process_id:'.length);
                    // 已用内存
                    usedMemoryHuman = memory.split('<br>').find(function (value) {
                        return value.indexOf('used_memory_human:') !== -1;
                    }).slice('used_memory_human:'.length);
                    // 内存峰值
                    usedMemoryPeakHuman = memory.split('<br>').find(function (value) {
                        return value.indexOf('used_memory_peak_human:') !== -1;
                    }).slice('used_memory_peak_human:'.length);
                    // Lua内存
                    usedMemoryLua = convertSize(memory.split('<br>').find(function (value) {
                        return value.indexOf('used_memory_lua:') !== -1;
                    }).slice('used_memory_lua:'.length));
                    // 客户端连接数
                    connectedClients = clients.split('<br>').find(function (value) {
                        return value.indexOf('connected_clients:') !== -1;
                    }).slice('connected_clients:'.length);
                    // 历史连接数
                    totalConnectionsReceived = stats.split('<br>').find(function (value) {
                        return value.indexOf('total_connections_received:') !== -1;
                    }).slice('total_connections_received:'.length);
                    // 历史命令数
                    totalCommandsProcessed = stats.split('<br>').find(function (value) {
                        return value.indexOf('total_commands_processed:') !== -1;
                    }).slice('total_commands_processed:'.length);
                    var mainInfoHtml = '<form class="layui-form layui-form-pane">' +
                        '                  <div class="layui-col-xs12 layui-col-sm4">' +
                        '                     <div class="layui-card">' +
                        '                        <div class="layui-card-header" style="font-weight: bold;">' +
                        '                             服务器' +
                        '                       </div>' +
                        '                        <div class="layui-card-body layuiadmin-card-list"' +
                        '                             id="redis-server-card-list">' +
                        '                           <div class="layui-form-item">' +
                        '                              <label class="layui-form-label">Redis版本</label>' +
                        '                              <div class="layui-input-block">' +
                        '                                 <input class="layui-input" readonly type="text" value="' + redisVersion + '">' +
                        '                             </div>' +
                        '                           </div>' +
                        '                            <div class="layui-form-item">' +
                        '                               <label class="layui-form-label">OS</label>' +
                        '                               <div class="layui-input-block">' +
                        '                                  <input class="layui-input" readonly type="text" value="' + os + '">' +
                        '                              </div>' +
                        '                            </div>' +
                        '                            <div class="layui-form-item">' +
                        '                               <label class="layui-form-label">进程ID</label>' +
                        '                               <div class="layui-input-block">' +
                        '                                  <input class="layui-input" readonly type="text" value="' + processId + '">' +
                        '                            </div>' +
                        '                        </div>' +
                        '                     </div>' +
                        '                   </div>' +
                        '                </div>' +
                        '            <div class="layui-col-xs12 layui-col-sm4">' +
                        '               <div class="layui-card">' +
                        '                  <div class="layui-card-header" style="font-weight: bold;">' +
                        '                       内存' +
                        '                 </div>' +
                        '                  <div class="layui-card-body layuiadmin-card-list"' +
                        '                       id="redis-memory-card-list">' +
                        '                     <div class="layui-form-item">' +
                        '                        <label class="layui-form-label">已用内存</label>' +
                        '                        <div class="layui-input-block">' +
                        '                           <input class="layui-input" readonly type="text" value="' + usedMemoryHuman + '">' +
                        '                       </div>' +
                        '                     </div>' +
                        '                      <div class="layui-form-item">' +
                        '                         <label class="layui-form-label">内存峰值</label>' +
                        '                      <div class="layui-input-block">' +
                        '                         <input class="layui-input" readonly type="text" value="' + usedMemoryPeakHuman + '">' +
                        '                    </div>' +
                        '                  </div>' +
                        '                   <div class="layui-form-item">' +
                        '                      <label class="layui-form-label">Lua内存</label>' +
                        '                      <div class="layui-input-block">' +
                        '                         <input class="layui-input" readonly type="text" value="' + usedMemoryLua + '">' +
                        '                     </div>' +
                        '                   </div>' +
                        '                 </div>' +
                        '               </div>' +
                        '             </div>' +
                        '              <div class="layui-col-xs12 layui-col-sm4">' +
                        '                 <div class="layui-card">' +
                        '                    <div class="layui-card-header" style="font-weight: bold;">' +
                        '                         状态' +
                        '                   </div>' +
                        '                    <div class="layui-card-body layuiadmin-card-list"' +
                        '                         id="redis-status-card-list">' +
                        '                       <div class="layui-form-item">' +
                        '                          <label class="layui-form-label">连接数</label>' +
                        '                          <div class="layui-input-block">' +
                        '                             <input class="layui-input" readonly type="text" value="' + connectedClients + '">' +
                        '                         </div>' +
                        '                       </div>' +
                        '                        <div class="layui-form-item">' +
                        '                           <label class="layui-form-label">历史连接数</label>' +
                        '                           <div class="layui-input-block">' +
                        '                              <input class="layui-input" readonly type="text" value="' + totalConnectionsReceived + '">' +
                        '                           </div>' +
                        '                        </div>' +
                        '                         <div class="layui-form-item">' +
                        '                            <label class="layui-form-label">历史命令数</label>' +
                        '                         <div class="layui-input-block">' +
                        '                             <input class="layui-input" readonly type="text" value="' + totalCommandsProcessed + '">' +
                        '                         </div>' +
                        '                       </div>' +
                        '                     </div>' +
                        '                   </div>' +
                        '                 </div>' +
                        '               </form>';
                    $('#redis-main-info').empty().html(mainInfoHtml);
                    var html = '<div class="layui-col-xs12 layui-col-sm4">' +
                        '          <div class="layui-collapse">' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Server</h2>' +
                        '                <div class="layui-colla-content layui-show">' + server + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Clients</h2>' +
                        '                <div class="layui-colla-content layui-show">' + clients + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Cluster</h2>' +
                        '                <div class="layui-colla-content layui-show">' + cluster + '</div>' +
                        '             </div>' +
                        '          </div>' +
                        '       </div>' +
                        '       <div class="layui-col-xs12 layui-col-sm4">' +
                        '          <div class="layui-collapse">' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Memory</h2>' +
                        '                <div class="layui-colla-content layui-show">' + memory + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">CPU</h2>' +
                        '                <div class="layui-colla-content layui-show">' + cpu + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Stats</h2>' +
                        '                <div class="layui-colla-content layui-show">' + stats + '</div>' +
                        '             </div>' +
                        '          </div>' +
                        '       </div>' +
                        '       <div class="layui-col-xs12 layui-col-sm4">' +
                        '          <div class="layui-collapse">' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Replication</h2>' +
                        '                <div class="layui-colla-content layui-show">' + replication + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Persistence</h2>' +
                        '                <div class="layui-colla-content layui-show">' + persistence + '</div>' +
                        '             </div>' +
                        '             <div class="layui-colla-item">' +
                        '                <h2 class="layui-colla-title">Keyspace</h2>' +
                        '                <div class="layui-colla-content layui-show">' + keyspace + '</div>' +
                        '             </div>' +
                        '          </div>' +
                        '       </div>';
                    $('#redis-info').empty().html(html);
                    //更新全部
                    element.render();
                    // 关闭loading框
                    layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    layer.close(loadingIndex);
                }
            });
        }

        // 执行ajax请求
        function execute() {
            // 获取redis信息
            getRedisInfo();
        }

        // 页面加载后第一次执行
        execute();
        // 每30秒刷新一次
        window.setInterval(function () {
            if (redisAutoRefresh) {
                execute();
            }
        }, 1000 * 30);
    });
    e('dbInfo4redis', {});
});