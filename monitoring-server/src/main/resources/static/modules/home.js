/** layuiAdmin.std-v2020.4.1 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    var a = layui.admin;
    layui.use(['admin', 'carousel'], function () {
        var e = layui.$, a = (layui.admin, layui.carousel), l = layui.element, t = layui.device();
        e('.layadmin-carousel').each(function () {
            var l = e(this);
            a.render({
                elem: this,
                width: '100%',
                arrow: 'none',
                interval: l.data('interval'),
                autoplay: l.data('autoplay') === !0,
                trigger: t.ios || t.android ? 'click' : 'hover',
                anim: l.data('anim')
            });
        });
        // 重新渲染进度条
        l.render('progress');
    }), layui.use('carousel', function () {
        var admin = layui.admin;
        // 基于准备好的dom，初始化echarts实例
        var myChart = (layui.carousel, echarts.init(document.getElementById('myChart'), 'infographic'));

        // 发送ajax请求，获取echarts数据
        function getLast7DaysAlarmRecordStatistics() {
            admin.req({
                type: 'post',
                url: layui.setter.base + 'home/get-last-7-days-alarm-record-statistics',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                success: function (result) {
                    var data = result.data;
                    // 日期
                    var date = data.map(function (item) {
                        return item.date;
                    });
                    // 成功
                    var success = data.map(function (item) {
                        return parseInt(item.success);
                    });
                    // 失败
                    var fail = data.map(function (item) {
                        return parseInt(item.fail);
                    });
                    var option = {
                        // 鼠标移到折线上展示数据
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['成功', '失败']
                        },
                        xAxis: [{
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: date
                        }],
                        yAxis: {
                            type: 'value',
                            name: '告警数',
                            axisLabel: {
                                formatter: '{value} 次'
                            }
                        },
                        // 数据
                        series: [{
                            stack: '总数',
                            name: '成功',
                            data: success,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#FFDEAD'
                                    }, {
                                        offset: 0.5,
                                        color: '#FFFACD'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#FF9A18'
                                }
                            }
                        }, {
                            stack: '总数',
                            name: '失败',
                            data: fail,
                            type: 'line',
                            smooth: true,
                            areaStyle: {
                                type: 'default',
                                // 渐变色实现
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                                    // 三种由深及浅的颜色
                                    [{
                                        offset: 0,
                                        color: '#FF4500'
                                    }, {
                                        offset: 0.5,
                                        color: '#FF6347'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#FF0000'
                                }
                            }
                        }]
                    };
                    myChart.setOption(option);
                },
                error: function () {
                    layer.msg('系统错误！', {icon: 5, shift: 6});
                }
            });
        }

        // 发送ajax请求，获取echarts数据
        getLast7DaysAlarmRecordStatistics();
        // 每五分钟刷新一次
        window.setInterval(function () {
            getLast7DaysAlarmRecordStatistics();
        }, 1000 * 60 * 5);
        window.onresize = myChart.resize;
    });
    e('home', {});
});