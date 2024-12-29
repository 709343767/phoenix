/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'form', 'laydate', 'util'], function () {
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
        // 日期选择器
        laydate.render({
            elem: '#insertTime',
            type: 'date',
            value: dateValue,
            max: dateValue,
            isInitValue: true,
            done: function (value) {
                if (!isEmpty(value)) {
                    dateValue = value;
                    getAvgTimeChartInfo(id, ipSource, ipTarget, dateValue);
                }
            }
        });

        // 发送ajax请求，获取PING平均时间
        function getAvgTimeChartInfo(id, ipSource, ipTarget, dateValue) {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-network-history/get-avg-time-chart-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    id: id,
                    ipSource: ipSource,
                    ipTarget: ipTarget,
                    dateValue: dateValue
                },
                success: function (result) {
                    var data = result.data;
                    // 所有
                    var allList = data.allList;
                    // 离线
                    var offLineList = data.offLineList;
                    // 所有耗时
                    var avgTime = allList.map(function (item) {
                        return item.avgTime;
                    });
                    // 新增时间
                    var insertTime = allList.map(function (item) {
                        return item.insertTime.replace(' ', '\n');
                    });
                    // 标记点
                    var markPointData = [];
                    for (var i = 0; i < offLineList.length; i++) {
                        var offLine = offLineList[i];
                        markPointData.push({
                            yAxis: offLine.avgTime,
                            xAxis: offLine.insertTime.replace(' ', '\n'),
                            value: '离线',
                            itemStyle: {
                                color: '#E13C00'
                            }
                        });
                    }
                    var option = {
                        title: {
                            text: 'Ping 耗时',
                            left: 'center',
                            textStyle: {
                                color: '#696969',
                                fontSize: 18
                            },
                            subtext: 'IP地址（来源）：' + ipSource + '，IP地址（目的地）：' + ipTarget,
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
                                    var itemValue = item.marker + item.seriesName + ': ' + formatMillisecond(item.data) + '</br>';
                                    result += itemValue;
                                });
                                return axisName + '</br>' + result;
                            }
                        },
                        legend: {
                            data: ['耗时'],
                            x: '60%',
                            y: '4',
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
                            left: '80',
                            right: '80'
                        },
                        xAxis: {
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: insertTime,
                            axisLabel: {
                                rotate: 0, //调整数值改变倾斜的幅度（范围-90到90）
                                textStyle: {
                                    // color: '#c3dbff',  //更改坐标轴文字颜色
                                    fontSize: 14      //更改坐标轴文字大小
                                }
                            }
                        },
                        yAxis: [{
                            type: 'value',
                            name: 'ms',
                            nameTextStyle: {
                                fontSize: 18      //更改坐标轴文字大小
                            },
                            axisLabel: {
                                formatter: '{value}',
                                //formatter: function (value, index) {
                                //    return formatMillisecond(value);
                                //},
                                textStyle: {
                                    // color: '#c3dbff',  //更改坐标轴文字颜色
                                    fontSize: 14      //更改坐标轴文字大小
                                }
                            }
                        }],
                        // 数据
                        series: [{
                            name: '耗时',
                            yAxisIndex: 0,
                            data: avgTime,
                            type: 'line',
                            smooth: true,
                            markPoint: {
                                data: markPointData
                            },
                            markLine: {
                                data: [
                                    {
                                        type: 'average',//平均线
                                        name: '平均线',
                                        itemStyle: {
                                            color: '#5FB878'
                                        }
                                    }, {
                                        yAxis: 30,
                                        itemStyle: {
                                            color: '#2E90D1'
                                        }
                                    }, {
                                        yAxis: 60,
                                        itemStyle: {
                                            color: '#E13C00'
                                        }
                                    }]
                            },
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
                // 发送ajax请求，获取PING平均时间
                getAvgTimeChartInfo(id, ipSource, ipTarget, dateValue);
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