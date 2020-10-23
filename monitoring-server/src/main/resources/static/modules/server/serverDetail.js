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
            // 发送ajax请求，获取CPU使用量数据
            getServerCpuInfo(time);
            // 发送ajax请求，获取内存使用量数据
            getServerMemoryInfo(time);
            // 发送ajax请求，获取磁盘使用量数据
            getServerDiskInfo();
        });

        // 发送ajax请求，获取磁盘使用量数据
        function getServerDiskInfo() {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-disk/get-server-detail-page-server-disk-info',
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
                        } else {
                            html += '<div class="layui-progress-bar" lay-percent="' + usePercent + '%"></div>';
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

        // 发送ajax请求，获取内存使用量数据
        function getServerMemoryInfo(time) {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-memory/get-server-detail-page-server-memory-info',
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
                    // 物理内存总量（单位：Gb）
                    var memTotal = data.length !== 0 ? data[data.length - 1].memTotal + ' Gb' : '没数据';
                    // 物理内存剩余量（单位：Gb）
                    var memFree = data.length !== 0 ? data[data.length - 1].memFree + ' Gb' : '没数据';
                    // 物理内存使用率
                    var menUsedPercent = data.length !== 0 ? data[data.length - 1].menUsedPercent + '%' : '没数据';
                    // 物理内存使用量（单位：Gb）
                    var memUsed0 = data.length !== 0 ? data[data.length - 1].memUsed + ' Gb' : '没数据';
                    var memUsed = data.map(function (item) {
                        return item.memUsed;
                    });
                    // 新增时间
                    var insertTime = data.map(function (item) {
                        return item.insertTime;
                    });
                    var option = {
                        title: {
                            text: '内存使用量',
                            left: 'center',
                            textStyle: {
                                color: '#696969',
                                fontSize: 14
                            },
                            subtext: '总量：' + memTotal + '，使用量：' + memUsed0 + '，剩余量：' + memFree + '，使用率：' + menUsedPercent,
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
                                    var itemValue = item.marker + item.seriesName + ': ' + item.data + ' Gb</br>';
                                    result += itemValue;
                                });
                                var allResult = axisName + '</br>' + result;
                                return allResult;
                            }
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
                            name: '使用量',
                            min: 0,  //一定要设置最小刻度
                            max: data.length !== 0 ? Math.ceil(data[data.length - 1].memTotal) : 1,  //一定要设置最大刻度
                            axisLabel: {
                                formatter: '{value} Gb'
                            }
                        },
                        // 数据
                        series: [{
                            name: '使用量',
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
                                        color: '#87CEEB'
                                    }, {
                                        offset: 0.5,
                                        color: '#ADD8E6'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#5F9EA0'
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

        // 发送ajax请求，获取CPU使用量数据
        function getServerCpuInfo(time) {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-server-cpu/get-server-detail-page-server-cpu-info',
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
                    // CPU利用率
                    var cpuCombined = data.map(function (item) {
                        return item.cpuCombined;
                    });
                    // 最新CPU利用率
                    var lastCpuCombined = data.length !== 0 ? data[data.length - 1].cpuCombined + '%' : '没数据';
                    // 最新CPU剩余率
                    var lastCpuIdle = data.length !== 0 ? (100 - data[data.length - 1].cpuCombined).toFixed(2) + '%' : '没数据';
                    // 新增时间
                    var insertTime = data.map(function (item) {
                        return item.insertTime;
                    });
                    var option = {
                        title: {
                            text: 'CPU利用率',
                            left: 'center',
                            textStyle: {
                                color: '#696969',
                                fontSize: 14
                            },
                            subtext: '利用率：' + lastCpuCombined + '，剩余率：' + lastCpuIdle,
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
                                var allResult = axisName + '</br>' + result;
                                return allResult;
                            }
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
                            name: '利用率',
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
                                        color: '#87CEEB'
                                    }, {
                                        offset: 0.5,
                                        color: '#ADD8E6'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#5F9EA0'
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

        // 发送ajax请求，获取CPU使用量数据
        getServerCpuInfo(time);
        // 发送ajax请求，获取内存使用量数据
        getServerMemoryInfo(time);
        // 发送ajax请求，获取磁盘使用量数据
        getServerDiskInfo();
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取CPU使用量数据
            getServerCpuInfo(time);
            // 发送ajax请求，获取内存使用量数据
            getServerMemoryInfo(time);
            // 发送ajax请求，获取磁盘使用量数据
            getServerDiskInfo();
        }, 1000 * 30);
    });
    e('serverDetail', {});
});