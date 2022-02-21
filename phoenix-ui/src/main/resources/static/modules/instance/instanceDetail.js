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
        // 时间
        var chartTime = 'hour';
        // 内存池图表内存类型
        var chartPool = $('#chart option:selected').val() === undefined ? '' : $('#chart option:selected').val();
        // 是否自动刷新
        var autoRefresh = true;
        // 图表条件发生改变
        form.on('select(chart)', function (data) {
            // 内存池类型
            chartPool = data.value;
            // 发送ajax请求，获取内存图表数据
            getJvmMemoryChartInfo(chartTime, chartPool, chartPool + '内存使用量');
        });
        // 时间条件发生改变
        form.on('select(time1)', function (data) {
            chartTime = data.value;
            // 发送ajax请求，获取内存图表数据
            getJvmMemoryChartInfo(chartTime, 'Heap', 'Heap内存使用量');
            getJvmMemoryChartInfo(chartTime, 'Non_Heap', 'Non_Heap内存使用量');
            getJvmMemoryChartInfo(chartTime, chartPool, chartPool + '内存使用量');
        });
        // 自动刷新条件改变
        form.on('switch(autoRefresh)', function (data) {
            //是否被选中，true或者false
            autoRefresh = data.elem.checked;
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
                    var threadCount = isEmpty(data.threadCount) ? webConstCn.NOT_OBTAINED_CN : data.threadCount;
                    // 线程峰值
                    var peakThreadCount = isEmpty(data.peakThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.peakThreadCount;
                    // 已创建并已启动的线程总数
                    var totalStartedThreadCount = isEmpty(data.totalStartedThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.totalStartedThreadCount;
                    // 当前活动守护线程数
                    var daemonThreadCount = isEmpty(data.daemonThreadCount) ? webConstCn.NOT_OBTAINED_CN : data.daemonThreadCount;
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
                    var max = '无数据'
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
                        },
                        // 鼠标移到折线上展示数据
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
                            type: 'category',
                            // X轴从零刻度开始
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
    e('instanceDetail', {});
});