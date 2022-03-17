/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'element', 'form', 'layer', 'laydate', 'util'], function () {
        var admin = layui.admin, form = layui.form, laydate = layui.laydate, util = layui.util;
        // 基于准备好的dom，初始化echarts实例
        var getAvgTimeInfoChart = echarts.init(document.getElementById('get-avg-time-info'), 'infographic');
        // 浏览器窗口大小发生改变时
        window.addEventListener("resize", function () {
            getAvgTimeInfoChart.resize();
        });
        // 是否自动刷新
        var autoRefresh = true;
        // 默认时间
        var dateValue = util.toDateString(new Date(), 'yyyy-MM-dd');
        // 开始时间
        var startTime = dateValue;
        // 结束时间
        var endTime = dateValue;
        // 日期选择器
        laydate.render({
            elem: '#insertTime',
            type: 'date',
            range: '~',
            value: dateValue + ' ~ ' + dateValue,
            isInitValue: true,
            done: function (value) {
                if (!isEmpty(value)) {
                    var times = value.split('~');
                    startTime = times[0].trim();
                    endTime = times[1].trim();
                    getAvgTimeChartInfo(id, ipSource, ipTarget, portTarget, protocol, startTime, endTime);
                }
            }
        });

        function getAvgTimeChartInfo(id, ipSource, ipTarget, portTarget, protocol, startTime, endTime) {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-tcpip-history/get-avg-time-chart-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    id: id,
                    ipSource: ipSource,
                    ipTarget: ipTarget,
                    portTarget: portTarget,
                    protocol: protocol,
                    startTime: startTime,
                    endTime: endTime
                },
                success: function (result) {
                    var data = result.data;
                    debugger;
                    // 耗时
                    var avgTime = data.map(function (item) {
                        return item.avgTime;
                    });
                    // 新增时间
                    var insertTime = data.map(function (item) {
                        return item.insertTime.replace(' ', '\n');
                    });
                    var option = {
                        title: {
                            text: 'TCP/IP耗时',
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
                                    var itemValue = item.marker + item.seriesName + ': ' + item.data + ' ms</br>';
                                    result += itemValue;
                                });
                                return axisName + '</br>' + result;
                            }
                        },
                        legend: {
                            data: ['耗时'],
                            x: 'center',
                            y: '5%',
                            orient: 'horizontal'
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
                            left: '2%',
                            right: '50'
                        },
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
                            name: 'ms',
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        // 数据
                        series: [{
                            name: '耗时',
                            yAxisIndex: 0,
                            data: avgTime,
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
                    getAvgTimeInfoChart.setOption(option);
                    // 关闭loading框
                    // layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    // layer.close(loadingIndex);
                }
            });
        }

        // 自动刷新条件改变
        form.on('switch(autoRefresh)', function (data) {
            //是否被选中，true或者false
            autoRefresh = data.elem.checked;
        });

        // 执行ajax请求
        function execute() {
            if (autoRefresh) {
                // 发送ajax请求，获取TCPIP平均时间
                getAvgTimeChartInfo(id, ipSource, ipTarget, portTarget, protocol, startTime, endTime);
            }
        }

        // 页面加载后第一次执行
        execute();
        // 每30秒刷新一次
        window.setInterval(function () {
            execute();
        }, 1000 * 30);
    });
    e('avgTime', {});
});