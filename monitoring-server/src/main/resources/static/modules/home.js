/** layuiAdmin.std-v2020.4.1 LPPL License By https://www.layui.com/admin/ */
;layui.define(function (e) {
    var a = layui.admin;
    layui.use(["admin", "carousel"], function () {
        var e = layui.$, a = (layui.admin, layui.carousel), l = layui.element, t = layui.device();
        e(".layadmin-carousel").each(function () {
            var l = e(this);
            a.render({
                elem: this,
                width: "100%",
                arrow: "none",
                interval: l.data("interval"),
                autoplay: l.data("autoplay") === !0,
                trigger: t.ios || t.android ? "click" : "hover",
                anim: l.data("anim")
            });
        });
        // 重新渲染进度条
        l.render("progress");
    }), layui.use(["carousel", "echarts"], function () {
        // 最近一周
        var thrityMonth = [];
        for (var i = 0; i < 7; i++) {
            thrityMonth.unshift(new Date(new Date().setDate(new Date().getDate() - i)).toLocaleDateString());
        }

        var e = layui.$, a = (layui.carousel, layui.echarts), l = [], t = [{
            tooltip: {trigger: "axis"},
            calculable: !0,
            legend: {data: ["访问量", "下载量", "平均访问量"]},
            xAxis: [{
                type: "category",
                data: thrityMonth
            }],
            yAxis: [{type: "value", name: "访问量", axisLabel: {formatter: "{value} 万"}}, {
                type: "value",
                name: "下载量",
                axisLabel: {formatter: "{value} 万"}
            }],
            series: [{
                itemStyle: {
                    normal: {
                        // 设置颜色
                        color: '#ff9a18'
                    }
                },
                name: "访问量",
                type: "line",
                data: [900, 850, 950, 1e3, 1100, 1050, 1e3, 1150, 1250, 1370, 1250, 1100]
            }, {
                itemStyle: {
                    normal: {
                        // 设置颜色
                        color: '#ff0000'
                    }
                },
                name: "下载量",
                type: "line",
                yAxisIndex: 1,
                data: [850, 850, 800, 950, 1e3, 950, 950, 1150, 1100, 1240, 1e3, 950]
            }, {
                name: "平均访问量",
                type: "line",
                data: [870, 850, 850, 950, 1050, 1e3, 980, 1150, 1e3, 1300, 1150, 1e3]
            }]
        }], i = e("#LAY-index-pagetwo").children("div"), n = function (e) {
            l[e] = a.init(i[e], layui.echartsTheme), l[e].setOption(t[e]), window.onresize = l[e].resize;
        };
        i[0] && n(0);
    }), e("home", {});
});