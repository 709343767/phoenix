/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'form'], function () {
        var admin = layui.admin, $ = layui.$;

        // 发送ajax请求，获取线程信息
        function getJvmThreadInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-jvm-thread/get-jvm-thread-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    instanceId: instanceId, // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    // 当前活动线程数
                    var threadCount = isEmpty(data.threadCount) ? webConstCn.NOT_OBTAINED_CN : data.threadCount;
                    // 线程峰值
                    var peakThreadCount = isEmpty(data.peakThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.peakThreadCount;
                    // 已创建并已启动的线程总数
                    var totalStartedThreadCount = isEmpty(data.totalStartedThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.totalStartedThreadCount;
                    // 当前活动守护线程数
                    var daemonThreadCount = isEmpty(data.daemonThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.daemonThreadCount;
                    // 所有线程信息
                    var threadInfoList = data.threadInfoList;
                    var html = '<div class="layui-col-md3">' +
                        '           <label class="label-font-weight">活动线程数：</label>' + threadCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '           <label class="label-font-weight">线程峰值：</label>' + peakThreadCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '           <label class="label-font-weight">守护程序线程数：</label>' + daemonThreadCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '           <label class="label-font-weight">启动的线程总数：</label>' + totalStartedThreadCount +
                        '       </div>';
                    if (Array.isArray(threadInfoList)) {
                        html += '<div class="layui-col-md12"><label class="label-font-weight">线程详情：</label>';
                        for (let threadInfo of threadInfoList) {
                            html += '<br>' + threadInfo;
                        }
                        html += '</div>';
                    }
                    $('#thread').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取GC信息
        function getJvmGcInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-jvm-garbage-collector/get-jvm-gc-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    instanceId: instanceId, // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    var html = '';
                    for (var i = 0; i < data.length; i++) {
                        var obj = data[i];
                        // 内存管理器名称
                        var garbageCollectorName = isEmpty(obj.garbageCollectorName) ? webConstCn.NOT_OBTAINED_CN : obj.garbageCollectorName;
                        // GC总次数
                        var collectionCount = isEmpty(obj.collectionCount) ? webConstCn.NOT_OBTAINED_CN : obj.collectionCount;
                        // GC总时间（毫秒）
                        var collectionTime = isEmpty(obj.collectionTime) ? webConstCn.NOT_OBTAINED_CN : obj.collectionTime;
                        html += '<div class="layui-col-md4">' +
                            '        <label class="label-font-weight">名称：</label>' + garbageCollectorName +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '        <label class="label-font-weight">GC总次数：</label>' + collectionCount +
                            '    </div>' +
                            '    <div class="layui-col-md4">' +
                            '        <label class="label-font-weight">GC总时间（毫秒）：</label>' + collectionTime +
                            '    </div>';
                    }
                    $('#gc').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取类加载数据
        function getJvmClassLoadingInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-jvm-class-loading/get-jvm-class-loading-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    instanceId: instanceId, // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    // 加载的类的总数
                    var totalLoadedClassCount = isEmpty(data.totalLoadedClassCount) ? webConstCn.NOT_OBTAINED_CN : data.totalLoadedClassCount;
                    // 当前加载的类的总数
                    var loadedClassCount = isEmpty(data.loadedClassCount) ? webConstCn.NOT_OBTAINED_CN : data.loadedClassCount;
                    // 卸载的类总数
                    var unloadedClassCount = isEmpty(data.unloadedClassCount) ? webConstCn.NOT_OBTAINED_CN : data.unloadedClassCount;
                    // 是否启用了类加载系统的详细输出
                    var isVerbose = isEmpty(data.isVerbose) ? webConstCn.NOT_OBTAINED_CN : (data.isVerbose === '0' ? '否' : '是');
                    var html = '<div class="layui-col-md3">' +
                        '           <label class="label-font-weight">已加载当前类：</label>' + loadedClassCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '          <label class="label-font-weight">已加载类总数：</label>' + totalLoadedClassCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '           <label class="label-font-weight">以卸载类总数：</label>' + unloadedClassCount +
                        '       </div>' +
                        '       <div class="layui-col-md3">' +
                        '           <label class="label-font-weight">是否启用了类加载系统的详细输出：</label>' + isVerbose +
                        '       </div>';
                    $('#class-loading').empty().append(html);
                }
            });
        }

        // 发送ajax请求，获取运行时数据
        function getJvmRuntimeInfo() {
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-jvm-runtime/get-jvm-runtime-info',
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                headers: {
                    "X-CSRF-TOKEN": tokenValue
                },
                data: {
                    instanceId: instanceId, // 应用实例ID
                },
                success: function (result) {
                    var data = result.data;
                    // 正在运行的Java虚拟机名称
                    var name = isEmpty(data.name) ? webConstCn.NOT_OBTAINED_CN : data.name;
                    // Java虚拟机实现名称
                    var vmName = isEmpty(data.vmName) ? webConstCn.NOT_OBTAINED_CN : data.vmName;
                    // Java虚拟机实现供应商
                    var vmVendor = isEmpty(data.vmVendor) ? webConstCn.NOT_OBTAINED_CN : data.vmVendor;
                    // Java虚拟机实现版本
                    var vmVersion = isEmpty(data.vmVersion) ? webConstCn.NOT_OBTAINED_CN : data.vmVersion;
                    // Java虚拟机规范名称
                    var specName = isEmpty(data.specName) ? webConstCn.NOT_OBTAINED_CN : data.specName;
                    // Java虚拟机规范供应商
                    var specVendor = isEmpty(data.specVendor) ? webConstCn.NOT_OBTAINED_CN : data.specVendor;
                    // Java虚拟机规范版本
                    var specVersion = isEmpty(data.specVersion) ? webConstCn.NOT_OBTAINED_CN : data.specVersion;
                    // 管理接口规范版本
                    var managementSpecVersion = isEmpty(data.managementSpecVersion) ? webConstCn.NOT_OBTAINED_CN : data.managementSpecVersion;
                    // Java类路径
                    var classPath = isEmpty(data.classPath) ? webConstCn.NOT_OBTAINED_CN : data.classPath;
                    // Java库路径
                    var libraryPath = isEmpty(data.libraryPath) ? webConstCn.NOT_OBTAINED_CN : data.libraryPath;
                    // Java虚拟机是否支持引导类路径
                    var isBootClassPathSupported = isEmpty(data.isBootClassPathSupported) ? webConstCn.NOT_OBTAINED_CN : (data.isBootClassPathSupported === '0' ? '否' : '是');
                    // 引导类路径
                    var bootClassPath = isEmpty(data.bootClassPath) ? webConstCn.NOT_OBTAINED_CN : data.bootClassPath;
                    // Java虚拟机入参
                    var inputArguments = isEmpty(data.inputArguments) ? webConstCn.NOT_OBTAINED_CN : data.inputArguments;
                    // Java虚拟机的正常运行时间（毫秒）
                    var uptime = isEmpty(data.uptime) ? webConstCn.NOT_OBTAINED_CN : data.uptime;
                    // Java虚拟机的开始时间
                    var startTime = isEmpty(data.startTime) ? webConstCn.NOT_OBTAINED_CN : data.startTime;
                    var html = '<div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机的开始时间：</label>' + startTime +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机的正常运行时间（毫秒）：</label>' + uptime +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '           <label class="label-font-weight">正在运行的Java虚拟机名称：</label>' + name +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '           <label class="label-font-weight">Java虚拟机实现名称：</label>' + vmName +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '           <label class="label-font-weight">Java虚拟机实现供应商：</label>' + vmVendor +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '           <label class="label-font-weight">Java虚拟机实现版本：</label>' + vmVersion +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机规范名称：</label>' + specName +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机规范供应商：</label>' + specVendor +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机规范版本：</label>' + specVersion +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">管理接口规范版本：</label>' + managementSpecVersion +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机入参：</label>' + inputArguments +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java类路径：</label>' + classPath +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java库路径：</label>' + libraryPath +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">Java虚拟机是否支持引导类路径：</label>' + isBootClassPathSupported +
                        '       </div>' +
                        '       <div class="layui-col-md12">' +
                        '            <label class="label-font-weight">引导类路径：</label>' + bootClassPath +
                        '       </div>';
                    $('#runtime').empty().append(html);
                }
            });
        }

        // 执行ajax请求
        function execute() {
            // 发送ajax请求，获取线程信息
            getJvmThreadInfo();
            // 发送ajax请求，获取GC信息
            getJvmGcInfo();
            // 发送ajax请求，获取类加载数据
            getJvmClassLoadingInfo();
            // 发送ajax请求，获取运行时数据
            getJvmRuntimeInfo();
        }

        // 页面加载后第一次执行
        execute();
        // 每30秒刷新一次
        window.setInterval(function () {
            execute();
        }, 1000 * 30);
    });
    e('instanceDetailJvm', {});
});