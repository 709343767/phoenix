/** layuiAdmin.std-v2020.4.1 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    var a = layui.admin;
    layui.use(['admin', 'carousel'], function () {
        var e = layui.$, a = (layui.admin, layui.carousel), l = layui.element, t = layui.device();
        //轮播切换
        e('.layadmin-carousel').each(function () {
            var l = e(this);
            var option = {
                elem: this,
                width: '100%',
                arrow: 'hover',
                interval: 3000,
                autoplay: true,
                trigger: t.ios || t.android ? 'click' : 'hover',
                anim: l.data('anim')
            };
            a.render(option);
        });
        // 渲染进度条
        l.render('progress');
    }), layui.use(['admin', 'carousel', 'jquery', 'element'], function () {
        var admin = layui.admin, $ = layui.$, element = layui.element;
        // 基于准备好的dom，初始化echarts实例
        var myChart = (layui.carousel, echarts.init(document.getElementById('last-7-days-alarm-record-statistics'), 'infographic'));
        // 浏览器窗口大小发生改变时
        window.onresize = myChart.resize;

        // 发送ajax请求，获取最近7天告警统计数据
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
                            },
                            minInterval: 1
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

        // 发送ajax请求，获取告警类型统计信息
        function getAlarmRecordTypeStatistics() {
            admin.req({
                type: 'post',
                url: layui.setter.base + 'home/get-alarm-record-type-statistics',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                success: function (result) {
                    var data = result.data;
                    var html = ``;
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        // 占比
                        var rate = obj.rate;
                        // 类型
                        var types = obj.types;
                        html += `<div class="layuiadmin-card-list">
                                <span>${types}</span>
                                <div class="layui-progress layui-progress-big" lay-showPercent="yes">
                                    <div class="layui-progress-bar layui-bg-orange" lay-percent="${rate}"></div>
                                </div>
                            </div>`;
                    }
                    $('#alarm-record-type-statistics').empty().append(html);
                    // 重新渲染进度条
                    element.render('progress');
                },
                error: function () {
                    layer.msg('系统错误！', {icon: 5, shift: 6});
                }
            });
        }

        // 发送ajax请求，获取最近7天告警统计数据
        getLast7DaysAlarmRecordStatistics();
        // 发送ajax请求，获取告警类型统计信息
        getAlarmRecordTypeStatistics();
        // 每五分钟刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取最近7天告警统计数据
            getLast7DaysAlarmRecordStatistics();
            // 发送ajax请求，获取告警类型统计信息
            getAlarmRecordTypeStatistics();
        }, 1000 * 60 * 5);
    });
    e('home', {});
});