/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(['admin', 'form'], function (e) {
    var admin = layui.admin, $ = layui.$, form = layui.form;
    // 基于准备好的dom，初始化echarts实例
    var getJvmMemoryHeapInfoChart = echarts.init(document.getElementById('get-jvm-memory-heap-info'), 'infographic');
    var getJvmMemoryNonHeapInfoChart = echarts.init(document.getElementById('get-jvm-memory-non-heap-info'), 'infographic');
    var getJvmMemoryPoolInfoChart = echarts.init(document.getElementById('get-jvm-memory-pool-info'), 'infographic');
    // 浏览器窗口大小发生改变时
    window.addEventListener("resize", function () {
        getJvmMemoryHeapInfoChart.resize();
        getJvmMemoryNonHeapInfoChart.resize();
        getJvmMemoryPoolInfoChart.resize();
    }, {capture: true});
    // 时间
    var chartTime = 'hour';
    // 内存池图表内存类型
    var jvmMemoryTypeVal = $('#jvmMemoryChart option:selected').val();
    var chartPool = jvmMemoryTypeVal === undefined ? '' : jvmMemoryTypeVal;
    // 是否自动刷新
    var autoRefresh = true;
    // 图表条件发生改变
    form.on('select(jvmMemoryChart)', function (data) {
        // 内存池类型
        chartPool = data.value;
        // 发送ajax请求，获取内存图表数据
        getJvmMemoryChartInfo(chartTime, chartPool, chartPool + '内存使用量');
    });
    // 时间条件发生改变
    form.on('select(jvmMemoryTime)', function (data) {
        chartTime = data.value;
        // 发送ajax请求，获取内存图表数据
        getJvmMemoryChartInfo(chartTime, 'Heap', 'Heap内存使用量');
        getJvmMemoryChartInfo(chartTime, 'Non_Heap', 'Non_Heap内存使用量');
        getJvmMemoryChartInfo(chartTime, chartPool, chartPool + '内存使用量');
    });
    // 自动刷新条件改变
    form.on('switch(autoRefreshJvmMemory)', function (data) {
        //是否被选中，true或者false
        autoRefresh = data.elem.checked;
    });

    // 发送ajax请求，获取内存图表数据
    function getJvmMemoryChartInfo(time, memoryType, title) {
        // 弹出loading框
        // var loadingIndex = layer.load(1, {
        //    shade: [0.1, '#fff'] //0.1透明度的白色背景
        // });
        admin.req({
            type: 'get',
            url: layui.setter.base + 'monitor-jvm-memory-history/get-instance-detail-page-jvm-memory-chart-info',
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            headers: {
                "X-CSRF-TOKEN": tokenValue
            },
            data: {
                instanceId: instanceId, // 应用实例ID
                time: time, // 时间
                memoryType: memoryType// 内存类型
            },
            success: function (result) {
                var data = result.data;
                // 时间
                var time = data.map(function (item) {
                    return item.insertTime.replace(' ', '\n');
                });
                // 使用量
                var used = data.map(function (item) {
                    return item.used;
                });
                // 提交内存量
                var committed = data.map(function (item) {
                    return item.committed;
                });
                // 初始内存值
                var init = '无数据';
                // 最大内存量
                var max = '无数据';
                if (data.length !== 0) {
                    if (data[data.length - 1].init !== '未定义') {
                        init = data[data.length - 1].init + ' MB';
                    } else {
                        init = data[data.length - 1].init;
                    }
                    if (data[data.length - 1].max !== '未定义') {
                        max = data[data.length - 1].max + ' MB';
                    } else {
                        max = data[data.length - 1].max;
                    }
                }
                var option = {
                    title: {
                        text: title,
                        left: 'center',
                        textStyle: {
                            color: '#696969',
                            fontSize: 14
                        },
                        subtext: '初始内存：' + init + '，最大内存：' + max,
                        subtextStyle: {
                            color: '#BEBEBE'
                        }
                    }, // 鼠标移到折线上展示数据
                    tooltip: {
                        trigger: 'axis',
                        formatter: function (params) {
                            var result = '';
                            var axisName = '';
                            params.forEach(function (item) {
                                axisName = item.axisValue;
                                var itemValue = item.marker + item.seriesName + ': ' + item.data + ' MB</br>';
                                result += itemValue;
                            });
                            return axisName + '</br>' + result;
                        }
                    },
                    legend: {
                        data: ['使用量', '提交量'],
                        orient: 'vertical',
                        x: '80%' //图例位置，设置right发现图例和文字位置反了，设置一个数值就好了
                    },
                    /*dataZoom: [{
                        type: 'inside'
                    }],*/
                    toolbox: {
                        show: true,
                        feature: {
                            dataZoom: {
                                yAxisIndex: "none"
                            },
                            dataView: {
                                readOnly: false
                            },
                            magicType: {
                                type: ["line", "bar"]
                            },
                            restore: {},
                            saveAsImage: {}
                        },
                        iconStyle: {
                            borderColor: "rgba(105, 98, 98, 1)"
                        },
                        right: "2%",
                        orient: "vertical",
                        showTitle: false,
                    },
                    grid: {
                        left: '5%',
                        right: '5%'
                    },
                    xAxis: {
                        type: 'category', // X轴从零刻度开始
                        boundaryGap: false,
                        data: time,
                        axisLabel: {
                            rotate: 0 //调整数值改变倾斜的幅度（范围-90到90）
                        },
                    },
                    yAxis: {
                        type: 'value',
                        name: '使用量',
                        axisLabel: {
                            formatter: '{value} MB'
                        }
                    }, // 数据
                    series: [{
                        name: '使用量',
                        data: used,
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
                    }, {
                        name: '提交量',
                        data: committed,
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
                    }]
                };
                if (memoryType === 'Heap') {
                    getJvmMemoryHeapInfoChart.setOption(option);
                } else if (memoryType === 'Non_Heap') {
                    getJvmMemoryNonHeapInfoChart.setOption(option);
                } else {
                    getJvmMemoryPoolInfoChart.setOption(option);
                }
                // 关闭loading框
                // layer.close(loadingIndex);
            },
            error: function () {
                // 关闭loading框
                // layer.close(loadingIndex);
            }
        });
    }

    // 执行ajax请求
    function execute() {
        if (autoRefresh) {
            // 发送ajax请求，获取内存图表数据
            getJvmMemoryChartInfo(chartTime, 'Heap', 'Heap内存使用量');
            getJvmMemoryChartInfo(chartTime, 'Non_Heap', 'Non_Heap内存使用量');
            getJvmMemoryChartInfo(chartTime, chartPool, chartPool + '内存使用量');
        }
    }

    // 页面加载后第一次执行
    execute();
    // 每30秒刷新一次
    window.setInterval(function () {
        execute();
    }, 1000 * 30);

    e('instanceDetailJvmMemory', {
        // tab页面切换调用方法
        tabSwitch: function () {
            getJvmMemoryHeapInfoChart.resize();
            getJvmMemoryNonHeapInfoChart.resize();
            getJvmMemoryPoolInfoChart.resize();
        }
    });
});