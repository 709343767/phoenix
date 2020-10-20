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
        });

        // 发送ajax请求，获取CPU使用量数据
        function getServerCpuInfo(time) {
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
                    debugger;
                }
            });
        }

        // 发送ajax请求，获取CPU使用量数据
        getServerCpuInfo(time);
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取CPU使用量数据
            getServerCpuInfo(time);
        });
    });
    e('serverDetail', {});
});