/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'element', 'form', 'layer'], function () {
        var admin = layui.admin, $ = layui.$, form = layui.form, layer = layui.layer;
        // 基于准备好的dom，初始化echarts实例
        var getServerCpuInfoChart = echarts.init(document.getElementById('get-server-cpu-info'), 'infographic');
        var getServerMemoryInfoChart = echarts.init(document.getElementById('get-server-memory-info'), 'infographic');
        var getServerDiskInfoChart = echarts.init(document.getElementById('get-server-disk-info'), 'infographic');
        // 浏览器窗口大小发生改变时
        window.addEventListener("resize", function () {
            getServerCpuInfoChart.resize();
            getServerMemoryInfoChart.resize();
            getServerDiskInfoChart.resize();
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
        });

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
                    debugger;
                    var data = result.data;
                    // 物理内存总量（单位：Mb）
                    var memTotal = data[0] !== undefined ? data[0].memTotal + ' Gb' : '未定义';
                    // 物理内存剩余量（单位：Mb）
                    var memFree = data[0] !== undefined ? data[0].memFree + ' Gb' : '未定义';
                    // 物理内存使用率
                    var menUsedPercent = data[0] !== undefined ? data[0].menUsedPercent + '%' : '未定义';
                    // 物理内存使用量（单位：Mb）
                    var memUsed0 = data[0] !== undefined ? data[0].memUsed + ' Gb' : '未定义';
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
                        grid: {
                            left: '150px',
                            right: '150px'
                        },
                        xAxis: [{
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: insertTime
                        }],
                        yAxis: {
                            type: 'value',
                            name: '使用量',
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
                        grid: {
                            left: '150px',
                            right: '150px'
                        },
                        xAxis: [{
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: insertTime
                        }],
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
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取CPU使用量数据
            getServerCpuInfo(time);
            // 发送ajax请求，获取内存使用量数据
            getServerMemoryInfo(time);
        }, 1000 * 30);
    });
    e('serverDetail', {});
});