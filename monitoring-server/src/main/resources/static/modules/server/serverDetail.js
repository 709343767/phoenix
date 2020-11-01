/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'element', 'form', 'layer'], function () {
        var admin = layui.admin, $ = layui.$, form = layui.form, layer = layui.layer, element = layui.element;
        // 渲染进度条
        element.render('progress');
        // 基于准备好的dom，初始化echarts实例
        var getServerCpuInfoChart = echarts.init(document.getElementById('get-server-cpu-info'), 'infographic');
        var getServerMemoryInfoChart = echarts.init(document.getElementById('get-server-memory-info'), 'infographic');
        // 浏览器窗口大小发生改变时
        window.addEventListener("resize", function () {
            getServerCpuInfoChart.resize();
            getServerMemoryInfoChart.resize();
        });
        // 堆内存图表和非堆内存图表时间
        var time = 'hour';
        // 时间条件发生改变
        form.on('select(time)', function (data) {
            time = data.value;
            // 发送ajax请求，获取CPU图表数据
            getServerCpuChartInfo(time);
            // 发送ajax请求，获取内存图表数据
            getServerMemoryChartInfo(time);
            // 发送ajax请求，获取磁盘图表数据
            getServerDiskChartInfo();
        });

        // 发送ajax请求，获取CPU数据
        function getServerCpuInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-cpu/get-server-detail-page-server-cpu-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        var cpuVendor = obj.cpuVendor;
                        var cpuMhz = obj.cpuMhz;
                        var cpuModel = obj.cpuModel;
                        var cpuNice = obj.cpuNice;
                        var cpuCombined = obj.cpuCombined;
                        var cpuIdle = obj.cpuIdle;
                        var cpuSys = obj.cpuSys;
                        var cpuUser = obj.cpuUser;
                        var cpuWait = obj.cpuWait;
                        html += '<div class="layui-col-md4">' +
                            '       <label class="label-font-weight">频率：</label>' + cpuMhz + 'MHz' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">制造商：</label>' + cpuVendor +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">类型：</label>' + cpuModel +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">总使用率：</label>' + (cpuCombined * 100).toFixed(2) + '%' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">剩余率：</label>' + (cpuIdle * 100).toFixed(2) + '%' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">用户使用率：</label>' + (cpuUser * 100).toFixed(2) + '%' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">系统使用率：</label>' + (cpuSys * 100).toFixed(2) + '%' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">等待率：</label>' + (cpuWait * 100).toFixed(2) + '%' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">错误率：</label>' + (cpuNice * 100).toFixed(2) + '%' +
                            '    </div>';
                        if (i != data.length - 1) {
                            html += '<hr class="layui-bg-gray hr-padding">';
                        }
                    }
                    $('#cpu').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取网卡数据
        function getServerNetcardInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-netcard/get-server-detail-page-server-netcard-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        var address = obj.address;
                        var broadcast = obj.broadcast;
                        var description = obj.description;
                        var hwAddr = obj.hwAddr;
                        var mask = obj.mask;
                        var name = obj.name;
                        var rx = obj.rx;
                        var rxDropped = obj.rxDropped;
                        var rxErrors = obj.rxErrors;
                        var rxPackets = obj.rxPackets;
                        var tx = obj.tx;
                        var txDropped = obj.txDropped;
                        var txErrors = obj.txErrors;
                        var txPackets = obj.txPackets;
                        var type = obj.type;
                        html += '<div class="layui-col-md4">' +
                            '       <label class="label-font-weight">网卡名字：</label>' + name +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">网卡类型：</label>' + type +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">网卡地址：</label>' + address +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">子网掩码：</label>' + mask +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">广播地址：</label>' + broadcast +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">MAC地址：</label>' + hwAddr +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">网卡信息描述：</label>' + description +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">接收的总数据大小：</label>' + rx +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">接收的总包数：</label>' + rxPackets + ' 个' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">接收到的错误包数：</label>' + rxErrors + ' 个' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">接收时丢弃的包数：</label>' + rxDropped + ' 个' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">发送的总数据大小：</label>' + tx +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">发送的总包数：</label>' + txPackets + ' 个' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">发送时的错误包数：</label>' + txErrors + ' 个' +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '       <label class="label-font-weight">发送时丢弃的包数：</label>' + txDropped + ' 个' +
                            '    </div>';
                        if (i != data.length - 1) {
                            html += '<hr class="layui-bg-gray hr-padding">';
                        }
                    }
                    $('#netcard').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取操作系统数据
        function getServerOsInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'server/get-server-os-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    var ip = data.ip;
                    var osName = data.osName;
                    var osTimeZone = data.osTimeZone;
                    var osVersion = data.osVersion;
                    var serverName = data.serverName;
                    var userHome = data.userHome;
                    var userName = data.userName;
                    var html = '<div class="layui-col-md4">' +
                        '           <label class="label-font-weight">IP地址：</label>' + ip +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">服务器名：</label>' + serverName +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">操作系统：</label>' + osName +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">系统版本：</label>' + osVersion +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">系统时区：</label>' + osTimeZone +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">系统用户：</label>' + userName +
                        '       </div>' +
                        '       <div class="layui-col-md4">' +
                        '           <label class="label-font-weight">用户目录：</label>' + userHome +
                        '       </div>';
                    $('#os').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取磁盘图表数据
        function getServerDiskChartInfo() {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-disk/get-server-detail-page-server-disk-chart-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        // 分区的盘符名称
                        var devName = obj.devName;
                        // 磁盘总大小
                        var totalStr = obj.totalStr;
                        // 磁盘可用大小
                        var availStr = obj.availStr;
                        // 磁盘资源的利用率
                        var usePercent = obj.usePercent;
                        html += '<div class="layui-progress layui-progress-big" lay-showPercent="yes">' +
                            '        <h3>' + devName + '（可用' + availStr + '/共' + totalStr + '）</h3>';
                        if (usePercent >= 90) {
                            html += '<div class="layui-progress-bar layui-bg-red" lay-percent="' + usePercent + '%"></div>';
                        } else if (usePercent >= 80 && usePercent < 90) {
                            html += '<div class="layui-progress-bar layui-bg-orange" lay-percent="' + usePercent + '%"></div>';
                        } else {
                            html += '<div class="layui-progress-bar layui-bg-green" lay-percent="' + usePercent + '%"></div>';
                        }
                        html += '</div>';
                    }
                    $('#get-server-disk-info').empty().append(html);
                    // 重新渲染进度条
                    element.render('progress');
                    // 关闭loading框
                    layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    layer.close(loadingIndex);
                }
            });
        }

        // 发送ajax请求，获取内存图表数据
        function getServerMemoryChartInfo(time) {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-memory/get-server-detail-page-server-memory-chart-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip, // 应用实例ID
                    time: time // 时间
                },
                success: function (result) {
                    var data = result.data;
                    // 物理内存总量（单位：GB）
                    var memTotal = data.length !== 0 ? data[data.length - 1].memTotal + ' GB' : '没数据';
                    // 物理内存使用率
                    var menUsedPercent = data.length !== 0 ? data[data.length - 1].menUsedPercent + '%' : '没数据';
                    // 交换区总量（单位：GB）
                    var swapTotal = data.length !== 0 ? data[data.length - 1].swapTotal + ' GB' : '没数据';
                    // 交换区使用率
                    var swapUsedPercent = data.length !== 0 ? data[data.length - 1].swapUsedPercent + '%' : '没数据';
                    var memUsed = data.map(function (item) {
                        return item.memUsed;
                    });
                    // 交换区使用量
                    var swapUsed = data.map(function (item) {
                        return item.swapUsed;
                    });
                    // 新增时间
                    var insertTime = data.map(function (item) {
                        return item.insertTime.replace(' ', '\n');
                    });
                    var option = {
                        title: {
                            text: '内存/交换区',
                            left: 'center',
                            textStyle: {
                                color: '#696969',
                                fontSize: 14
                            },
                            subtext: '物理内存：' + memTotal + '，物理内存使用率：' + menUsedPercent + '，交换区：' + swapTotal + '，交换区使用率：' + swapUsedPercent,
                            subtextStyle: {
                                color: '#BEBEBE'
                            }
                        },
                        // 鼠标移到折线上展示数据
                        tooltip: {
                            trigger: 'axis',
                            formatter: function (params) {
                                var result = '';
                                var axisName = '';
                                params.forEach(function (item) {
                                    axisName = item.axisValue;
                                    var itemValue = item.marker + item.seriesName + ': ' + item.data + ' GB</br>';
                                    result += itemValue;
                                });
                                return axisName + '</br>' + result;
                            }
                        },
                        legend: {
                            data: ['内存使用量', '交换区使用量'],
                            x: 'center',
                            y: '12%',
                            orient: 'horizontal'
                        },
                        /*grid: {
                            left: '150px',
                            right: '150px'
                        },*/
                        xAxis: {
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: insertTime,
                            axisLabel: {
                                rotate: 0 //调整数值改变倾斜的幅度（范围-90到90）
                            }
                        },
                        yAxis: [{
                            type: 'value',
                            name: '内存使用量',
                            min: 0,  //一定要设置最小刻度
                            max: Math.ceil(Math.max.apply(null, data.map(function (item) {
                                return item.memTotal;
                            }))),  //一定要设置最大刻度
                            axisLabel: {
                                formatter: '{value} GB'
                            }
                        }, {
                            type: 'value',
                            name: '交换区使用量',
                            min: 0,  //一定要设置最小刻度
                            max: Math.ceil(Math.max.apply(null, data.map(function (item) {
                                return item.swapTotal;
                            }))),
                            axisLabel: {
                                formatter: '{value} GB'
                            }
                        }],
                        // 数据
                        series: [{
                            name: '内存使用量',
                            yAxisIndex: 0,
                            data: memUsed,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#9F79EE'
                                    }, {
                                        offset: 0.5,
                                        color: '#AB82FF'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#8968CD'
                                }
                            }
                        }, {
                            name: '交换区使用量',
                            yAxisIndex: 1,
                            data: swapUsed,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#B4EEB4'
                                    }, {
                                        offset: 0.5,
                                        color: '#C1FFC1'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#9BCD9B'
                                }
                            }
                        }]
                    };
                    getServerMemoryInfoChart.setOption(option);
                    // 关闭loading框
                    layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    layer.close(loadingIndex);
                }
            });
        }

        // 发送ajax请求，获取CPU图表数据
        function getServerCpuChartInfo(time) {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-cpu/get-server-detail-page-server-cpu-chart-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    ip: ip, // 应用实例ID
                    time: time // 时间
                },
                success: function (result) {
                    var data = result.data;
                    // CPU用户使用率
                    var cpuUser = data.map(function (item) {
                        return item.cpuUser;
                    });
                    // CPU系统使用率
                    var cpuSys = data.map(function (item) {
                        return item.cpuSys;
                    });
                    // CPU等待率
                    var cpuWait = data.map(function (item) {
                        return item.cpuWait;
                    });
                    // CPU错误率
                    var cpuNice = data.map(function (item) {
                        return item.cpuNice;
                    });
                    // CPU总利用率
                    var cpuCombined = data.map(function (item) {
                        return item.cpuCombined;
                    });
                    // CPU剩余率
                    var cpuIdle = data.map(function (item) {
                        return item.cpuIdle;
                    });
                    // 最新CPU总利用率
                    var lastCpuCombined = data.length !== 0 ? data[data.length - 1].cpuCombined.toFixed(2) + '%' : '没数据';
                    // 最新CPU剩余率
                    var lastCpuIdle = data.length !== 0 ? (100 - data[data.length - 1].cpuCombined).toFixed(2) + '%' : '没数据';
                    // 新增时间
                    var insertTime = data.map(function (item) {
                        return item.insertTime.replace(' ', '\n');
                    });
                    var option = {
                        title: {
                            text: 'CPU',
                            left: 'center',
                            textStyle: {
                                color: '#696969',
                                fontSize: 14
                            },
                            subtext: '剩余率：' + lastCpuIdle + '，总使用率：' + lastCpuCombined,
                            subtextStyle: {
                                color: '#BEBEBE'
                            }
                        },
                        // 鼠标移到折线上展示数据
                        tooltip: {
                            trigger: 'axis',
                            formatter: function (params) {
                                var result = '';
                                var axisName = '';
                                params.forEach(function (item) {
                                    axisName = item.axisValue;
                                    var itemValue = item.marker + item.seriesName + ': ' + item.data + '%</br>';
                                    result += itemValue;
                                });
                                return axisName + '</br>' + result;
                            }
                        },
                        legend: {
                            data: ['剩余率', '总使用率', '用户使用率', '系统使用率', '等待率', '错误率'],
                            selected: {'用户使用率': false, '系统使用率': false, '等待率': false, '错误率': false},
                            x: 'center',
                            y: '12%',
                            orient: 'horizontal'
                        },
                        /*grid: {
                            left: '150px',
                            right: '150px'
                        },*/
                        xAxis: {
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: insertTime,
                            axisLabel: {
                                rotate: 0 //调整数值改变倾斜的幅度（范围-90到90）
                            }
                        },
                        yAxis: {
                            type: 'value',
                            name: '利用率',
                            min: 0,  //一定要设置最小刻度
                            max: 100,  //一定要设置最大刻度
                            minInterval: 20, //这个可自己设置刻度间隔
                            axisLabel: {
                                formatter: '{value}%'
                            }
                        },
                        // 数据
                        series: [{
                            name: '剩余率',
                            data: cpuIdle,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#B4EEB4'
                                    }, {
                                        offset: 0.5,
                                        color: '#C1FFC1'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#9BCD9B'
                                }
                            }
                        }, {
                            name: '总使用率',
                            data: cpuCombined,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#EE9572'
                                    }, {
                                        offset: 0.5,
                                        color: '#FFA07A'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#CD8162'
                                }
                            }
                        }, {
                            name: '用户使用率',
                            data: cpuUser,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#EEEE00'
                                    }, {
                                        offset: 0.5,
                                        color: '#FFFF00'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#CDCD00'
                                }
                            }
                        }, {
                            name: '系统使用率',
                            data: cpuSys,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#D15FEE'
                                    }, {
                                        offset: 0.5,
                                        color: '#E066FF'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#B452CD'
                                }
                            }
                        }, {
                            name: '等待率',
                            data: cpuWait,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#EEE5DE'
                                    }, {
                                        offset: 0.5,
                                        color: '#FFF5EE'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#CDC5BF'
                                }
                            }
                        }, {
                            name: '错误率',
                            data: cpuNice,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#EE4000'
                                    }, {
                                        offset: 0.5,
                                        color: '#FF4500'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#CD3700'
                                }
                            }
                        }]
                    };
                    getServerCpuInfoChart.setOption(option);
                    // 关闭loading框
                    layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    layer.close(loadingIndex);
                }
            });
        }

        // 发送ajax请求，获取CPU图表数据
        getServerCpuChartInfo(time);
        // 发送ajax请求，获取内存图表数据
        getServerMemoryChartInfo(time);
        // 发送ajax请求，获取磁盘图表数据
        getServerDiskChartInfo();
        // 发送ajax请求，获取操作系统数据
        getServerOsInfo();
        // 发送ajax请求，获取网卡数据
        getServerNetcardInfo();
        // 发送ajax请求，获取CPU数据
        getServerCpuInfo();
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取CPU图表数据
            getServerCpuChartInfo(time);
            // 发送ajax请求，获取内存图表数据
            getServerMemoryChartInfo(time);
            // 发送ajax请求，获取磁盘图表数据
            getServerDiskChartInfo();
            // 发送ajax请求，获取操作系统数据
            getServerOsInfo();
            // 发送ajax请求，获取网卡数据
            getServerNetcardInfo();
            // 发送ajax请求，获取CPU数据
            getServerCpuInfo();
        }, 1000 * 30);
    });
    e('serverDetail', {});
});