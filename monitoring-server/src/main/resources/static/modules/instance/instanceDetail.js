/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['admin', 'element', 'form', 'layer'], function () {
        var admin = layui.admin, $ = layui.$, form = layui.form, layer = layui.layer;
        // 基于准备好的dom，初始化echarts实例
        var getJvmMemoryHeapInfoChart = echarts.init(document.getElementById('get-jvm-memory-heap-info'), 'infographic');
        var getJvmMemoryNonHeapInfoChart = echarts.init(document.getElementById('get-jvm-memory-non-heap-info'), 'infographic');
        var getJvmMemoryPoolInfoChart = echarts.init(document.getElementById('get-jvm-memory-pool-info'), 'infographic');
        // 浏览器窗口大小发生改变时
        window.addEventListener("resize", function () {
            getJvmMemoryHeapInfoChart.resize();
            getJvmMemoryNonHeapInfoChart.resize();
            getJvmMemoryPoolInfoChart.resize();
        });
        // 堆内存图表和非堆内存图表时间
        var heapAndNonHeapTime = 'hour';
        // 时间条件发生改变
        form.on('select(time1)', function (data) {
            heapAndNonHeapTime = data.value;
            // 发送ajax请求，获取内存使用量数据
            getJvmMemoryInfo(heapAndNonHeapTime, 'Heap', 'Heap内存使用量');
            getJvmMemoryInfo(heapAndNonHeapTime, 'Non_Heap', 'Non_Heap内存使用量');
        });
        // 内存池图表时间
        var chartTime = 'hour';
        // 内存池图表内存类型
        var chartPool = $('#chart option:selected').val() === undefined ? '' : $('#chart option:selected').val();
        // 图表条件发生改变
        form.on('select(chart)', function (data) {
            // 内存池类型
            chartPool = data.value;
            // 发送ajax请求，获取内存使用量数据
            getJvmMemoryInfo(chartTime, chartPool, chartPool + '内存使用量');
        });
        form.on('select(time2)', function (data) {
            // 时间
            chartTime = data.value;
            // 发送ajax请求，获取内存使用量数据
            getJvmMemoryInfo(chartTime, chartPool, chartPool + '内存使用量');
        });

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
                    var threadCount = data.threadCount;
                    // 线程峰值
                    var peakThreadCount = data.peakThreadCount;
                    // 已创建并已启动的线程总数
                    var totalStartedThreadCount = data.totalStartedThreadCount;
                    // 当前活动守护线程数
                    var daemonThreadCount = data.daemonThreadCount;
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
                        var garbageCollectorName = obj.garbageCollectorName;
                        // GC总次数
                        var collectionCount = obj.collectionCount;
                        // GC总时间（毫秒）
                        var collectionTime = obj.collectionTime;
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
                    var totalLoadedClassCount = data.totalLoadedClassCount;
                    // 当前加载的类的总数
                    var loadedClassCount = data.loadedClassCount;
                    // 卸载的类总数
                    var unloadedClassCount = data.unloadedClassCount;
                    // 是否启用了类加载系统的详细输出
                    var isVerbose = data.isVerbose === '0' ? '否' : '是';
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
                    var name = data.name;
                    // Java虚拟机实现名称
                    var vmName = data.vmName;
                    // Java虚拟机实现供应商
                    var vmVendor = data.vmVendor;
                    // Java虚拟机实现版本
                    var vmVersion = data.vmVersion;
                    // Java虚拟机规范名称
                    var specName = data.specName;
                    // Java虚拟机规范供应商
                    var specVendor = data.specVendor;
                    // Java虚拟机规范版本
                    var specVersion = data.specVersion;
                    // 管理接口规范版本
                    var managementSpecVersion = data.managementSpecVersion;
                    // Java类路径
                    var classPath = data.classPath;
                    // Java库路径
                    var libraryPath = data.libraryPath;
                    // Java虚拟机是否支持引导类路径
                    var isBootClassPathSupported = data.isBootClassPathSupported === '0' ? '否' : '是';
                    // 引导类路径
                    var bootClassPath = data.bootClassPath;
                    // Java虚拟机入参
                    var inputArguments = data.inputArguments;
                    // Java虚拟机的正常运行时间（毫秒）
                    var uptime = data.uptime;
                    // Java虚拟机的开始时间
                    var startTime = data.startTime;
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

        // 发送ajax请求，获取内存使用量数据
        function getJvmMemoryInfo(time, memoryType, title) {
            // 弹出loading框
            var loadingIndex = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            admin.req({
                type: 'get',
                url: layui.setter.base + 'monitor-jvm-memory/get-instance-detail-page-jvm-memory-info',
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
                        return item.insertTime;
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
                    var max = '无数据'
                    if (data[0] != undefined) {
                        if (data[0].init != '未定义') {
                            init = data[0].init + 'Mb';
                        } else {
                            init = data[0].init;
                        }
                        if (data[0].max != '未定义') {
                            max = data[0].max + 'Mb';
                        } else {
                            max = data[0].max;
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
                            subtext: '初始内存值：' + init + '，最大内存量：' + max,
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
                                    var itemValue = item.marker + item.seriesName + ': ' + item.data + 'Mb</br>';
                                    result += itemValue;
                                });
                                var allResult = axisName + '</br>' + result;
                                return allResult;
                            }
                        },
                        legend: {
                            data: ['使用量', '提交量'],
                            left: 'right',
                        },
                        grid: {
                            left: '150px',
                            right: '150px'
                        },
                        xAxis: [{
                            type: 'category',
                            // X轴从零刻度开始
                            boundaryGap: false,
                            data: time
                        }],
                        yAxis: {
                            type: 'value',
                            name: '使用量',
                            axisLabel: {
                                formatter: '{value} Mb'
                            }
                        },
                        // 数据
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
                    layer.close(loadingIndex);
                },
                error: function () {
                    // 关闭loading框
                    layer.close(loadingIndex);
                }
            });
        }

        // 发送ajax请求，获取内存使用量数据
        getJvmMemoryInfo('hour', 'Heap', 'Heap内存使用量');
        getJvmMemoryInfo('hour', 'Non_Heap', 'Non_Heap内存使用量');
        getJvmMemoryInfo('hour', chartPool, chartPool + '内存使用量');
        // 发送ajax请求，获取线程信息
        getJvmThreadInfo();
        // 发送ajax请求，获取GC信息
        getJvmGcInfo();
        // 发送ajax请求，获取类加载数据
        getJvmClassLoadingInfo();
        // 发送ajax请求，获取运行时数据
        getJvmRuntimeInfo();
        // 每30秒刷新一次
        window.setInterval(function () {
            // 发送ajax请求，获取内存使用量数据
            getJvmMemoryInfo(heapAndNonHeapTime, 'Heap', 'Heap内存使用量');
            getJvmMemoryInfo(heapAndNonHeapTime, 'Non_Heap', 'Non_Heap内存使用量');
            getJvmMemoryInfo(chartTime, chartPool, chartPool + '内存使用量');
            // 发送ajax请求，获取线程信息
            getJvmThreadInfo();
            // 发送ajax请求，获取GC信息
            getJvmGcInfo();
            // 发送ajax请求，获取类加载数据
            getJvmClassLoadingInfo();
            // 发送ajax请求，获取运行时数据
            getJvmRuntimeInfo();
        }, 1000 * 30);
    });
    e('instanceDetail', {});
});