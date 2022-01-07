/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'carousel'], function () {
        var e = layui.$, a = (layui.admin, layui.carousel), l = layui.element, t = layui.device();
        //轮播切换
        e('.layadmin-carousel').each(function () {
            var l = e(this);
            var option = {
                elem: this,
                width: '100%',
                arrow: 'hover',
                interval: 5000,
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
                    // 不提醒
                    var unsent = data.map(function (item) {
                        return parseInt(item.unsent);
                    });
                    var option = {
                        // 鼠标移到折线上展示数据
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['不提醒', '失败', '成功']
                        },
                        xAxis: [{
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: date
                        }],
                        yAxis: {
                            type: 'value',
                            name: '次数',
                            axisLabel: {
                                formatter: '{value} 次'
                            },
                            minInterval: 1
                        },
                        // 数据
                        series: [{
                            stack: '总数',
                            name: '不提醒',
                            data: unsent,
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
                        }, {
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
                                        color: '#A2CD5A'
                                    }, {
                                        offset: 0.5,
                                        color: '#BCEE68'
                                    }, {
                                        offset: 1,
                                        color: '#FFFFFF'
                                    }])
                            },
                            itemStyle: {
                                normal: {
                                    // 设置颜色
                                    color: '#6E8B3D'
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
                    var html = '<div class="layui-card-body layadmin-takerates">';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        // 占比
                        var rate = obj.rate;
                        // 数量
                        var totals = obj.totals;
                        // 类型
                        var types = obj.types;
                        html += '<div class="layui-progress" lay-showPercent="yes">'
                            + '     <h3>' + types + '（' + totals + '次）</h3>'
                            + '     <div class="layui-progress-bar layui-bg-orange" lay-percent="' + rate + '"></div>'
                            + '  </div>';
                    }
                    html += '</div>';
                    $('#alarm-record-type-statistics').empty().append(html);
                    // 重新渲染进度条
                    element.render('progress');
                },
                error: function () {
                    layer.msg('系统错误！', {icon: 5, shift: 6});
                }
            });
        }

        // 发送ajax请求，获取最新的5条告警记录
        function getLast5AlarmRecord() {
            admin.req({
                type: 'post',
                url: layui.setter.base + 'home/get-last-5-alarm-record',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                success: function (result) {
                    var data = result.data;
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        var title = obj.title;
                        var content = obj.content.length >= 500 ? obj.content.slice(0, 500) + '......' : obj.content;
                        var insertTime = obj.insertTime;
                        html += '<li>'
                            + '<h3>' + title + '</h3>'
                            + '<p>' + content + '</p>'
                            + '<span>' + insertTime + '</span>'
                            + '</li>';
                    }
                    $('#get-last-5-alarm-record').empty().append(html);
                },
                error: function () {
                    layer.msg('系统错误！', {icon: 5, shift: 6});
                }
            });
        }

        // 发送ajax请求，获取home页的摘要信息
        function getSummaryInfo() {
            admin.req({
                type: 'post',
                url: layui.setter.base + 'home/get-summary-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                success: function (result) {
                    var data = result.data;
                    var homeInstanceVo = data.homeInstanceVo;
                    var homeServerVo = data.homeServerVo;
                    var homeNetVo = data.homeNetVo;
                    var homeAlarmRecordVo = data.homeAlarmRecordVo;
                    var homeDbVo = data.homeDbVo;
                    // 服务器类型
                    var htmlServer1 = '<p class="layuiadmin-big-font layuiadmin-big-font-my">' + homeServerVo.serverSum + '</p>' +
                        '             <p>Windows' +
                        '                   <span class="layuiadmin-span-color">' + homeServerVo.windowsSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/windows-16.png"></i>' +
                        '                   </span>' +
                        '             </p>' +
                        '             <p>Linux' +
                        '                   <span class="layuiadmin-span-color">' + homeServerVo.linuxSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/linux-16.png"></i>' +
                        '                   </span>' +
                        '             </p>' +
                        '             <p>其他' +
                        '                   <span class="layuiadmin-span-color">' + homeServerVo.otherSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/other-16.png"></i>' +
                        '                   </span>' +
                        '             </p>';
                    $('#server-card-list-1').empty().append(htmlServer1);
                    // 服务器在线率
                    var htmlServer2 = '<p class="layuiadmin-big-font">' + homeServerVo.serverSum +
                        '                   <i class="home-i">' + homeServerVo.serverOnLineRate + '<img src="' + ctxPath + 'images/icon16/percentage-16.png"></i>' +
                        '               </p>' +
                        '               <p>在线' +
                        '                   <span class="layuiadmin-span-color">' + homeServerVo.serverOnLineSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/on-line-16.png"></i>' +
                        '                   </span>' +
                        '               </p>' +
                        '               <p>离线' +
                        '                   <span class="layuiadmin-span-color">' + homeServerVo.serverOffLineSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/off-line-16.png"></i>' +
                        '                   </span>' +
                        '               </p>';
                    $('#server-card-list-2').empty().append(htmlServer2);
                    // 应用程序
                    var htmlInstance = '<p class="layuiadmin-big-font">' + homeInstanceVo.instanceSum +
                        '                   <i class="home-i">' + homeInstanceVo.instanceOnLineRate + '<img src="' + ctxPath + 'images/icon16/percentage-16.png"></i>' +
                        '               </p>' +
                        '               <p>在线' +
                        '                   <span class="layuiadmin-span-color">' + homeInstanceVo.instanceOnLineSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/on-line-16.png"></i>' +
                        '                   </span>' +
                        '               </p>' +
                        '               <p>离线' +
                        '                   <span class="layuiadmin-span-color">' + homeInstanceVo.instanceOffLineSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/off-line-16.png"></i>' +
                        '                   </span>' +
                        '               </p>';
                    $('#instance-card-list').empty().append(htmlInstance);
                    // 数据库
                    var htmlDb = '<p class="layuiadmin-big-font layuiadmin-big-font-my">' + homeDbVo.dbSum +
                        '               <i class="home-i">' + homeDbVo.dbConnectRate + '<img src="' + ctxPath + 'images/icon16/percentage-16.png"></i>' +
                        '         </p>' +
                        '         <p>正常' +
                        '               <span class="layuiadmin-span-color">' + homeDbVo.dbConnectSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/normal-16.png"></i>' +
                        '               </span>' +
                        '         </p>' +
                        '         <p>异常' +
                        '               <span class="layuiadmin-span-color">' + homeDbVo.dbDisconnectSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/exception-16.png"></i>' +
                        '               </span>' +
                        '         </p>' +
                        '         <p>未知' +
                        '               <span class="layuiadmin-span-color">' + homeDbVo.dbUnsentSum +
                        '                       <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/unknown-16.png"></i>' +
                        '               </span>' +
                        '         </p>';
                    $('#db-card-list').empty().append(htmlDb);
                    // 网络
                    var htmlIp = '<p class="layuiadmin-big-font layuiadmin-big-font-my">' + homeNetVo.netSum +
                        '               <i class="home-i">' + homeNetVo.netConnectRate + '<img src="' + ctxPath + 'images/icon16/percentage-16.png"></i>' +
                        '         </p>' +
                        '         <p>正常' +
                        '               <span class="layuiadmin-span-color">' + homeNetVo.netConnectSum +
                        '                      <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/net-connect-16.png"></i>' +
                        '               </span>' +
                        '         </p>' +
                        '         <p>异常' +
                        '               <span class="layuiadmin-span-color">' + homeNetVo.netDisconnectSum +
                        '                      <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/net-disconnect-16.png"></i>' +
                        '               </span>' +
                        '         </p>' +
                        '         <p>未知' +
                        '               <span class="layuiadmin-span-color">' + homeNetVo.netUnsentSum +
                        '                      <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/net-unkown-16.png"></i>' +
                        '               </span>' +
                        '         </p>'
                    ;
                    $('#ip-card-list').empty().append(htmlIp);
                    // 告警
                    var htmlAlarm = '<p class="layuiadmin-big-font layuiadmin-big-font-my">' + homeAlarmRecordVo.alarmRecordSum +
                        '                   <i class="home-i">' + homeAlarmRecordVo.alarmSucRate + '<img src="' + ctxPath + 'images/icon16/percentage-16.png"></i>' +
                        '            </p>' +
                        '            <p>成功' +
                        '                   <span class="layuiadmin-span-color">' + homeAlarmRecordVo.alarmRecordSuccessSum +
                        '                           <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/success-16.png"></i>' +
                        '                   </span>' +
                        '            </p>' +
                        '            <p>失败' +
                        '                   <span class="layuiadmin-span-color">' + homeAlarmRecordVo.alarmRecordFailSum +
                        '                           <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/fail-16.png"></i>' +
                        '                   </span>' +
                        '            </p>' +
                        '            <p>不提醒' +
                        '                   <span class="layuiadmin-span-color">' + homeAlarmRecordVo.alarmRecordUnsentSum +
                        '                           <i class="layui-inline layui-icon"><img src="' + ctxPath + 'images/icon16/unsent-16.png"></i>' +
                        '                   </span>' +
                        '            </p>';
                    $('#alarm-card-list').empty().append(htmlAlarm);
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
        // 发送ajax请求，获取最新的5条告警记录
        getLast5AlarmRecord();
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取最近7天告警统计数据
            getLast7DaysAlarmRecordStatistics();
            // 发送ajax请求，获取告警类型统计信息
            getAlarmRecordTypeStatistics();
            // 发送ajax请求，获取最新的5条告警记录
            getLast5AlarmRecord();
            // 发送ajax请求，获取home页的摘要信息（不需要页面加载时发送ajax）
            getSummaryInfo();
        }, 1000 * 30);
    });
    e('home', {});
});