/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'table'], function () {
        var $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table, device = layui.device();
        table.render({
            elem: '#list-table',
            url: ctxPath + 'db-session4oracle/get-session-list?id=' + id,
            toolbar: '#list-table-toolbar',
            request: {
                pageName: 'current',//页码的参数名称，默认：page
                limitName: 'size' //每页数据量的参数名，默认：limit
            },
            response: {
                statusName: 'code', //规定数据状态的字段名称，默认：code
                statusCode: 200,//规定成功的状态码，默认：0
                msgName: 'msg',//规定状态信息的字段名称，默认：msg
                countName: 'count', //规定数据总数的字段名称，默认：count
                dataName: 'data' //规定数据列表的字段名称，默认：data
            },
            parseData: function (res) { //res 即为原始返回的数据
                return {
                    'code': res.code, //解析接口状态
                    'msg': res.msg, //解析提示文本
                    'count': res.data.total, //解析数据长度
                    'data': res.data.records //解析数据列表
                };
            },
            cols: [
                [{
                    type: 'checkbox',
                    hide: !authority
                }, {
                    field: 'sid',
                    width: 100,
                    title: '会话ID',
                    sort: !0
                }, {
                    field: 'serial',
                    width: 100,
                    title: 'serial#',
                    sort: !0
                }, {
                    field: 'username',
                    title: '用户',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'schemaName',
                    title: '模式',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'type',
                    title: '会话类型',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'state',
                    title: '状态',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'logonTime',
                    title: '登录时间',
                    minWidth: 180,
                    sort: !0
                }, {
                    field: 'machine',
                    title: '远程主机',
                    minWidth: 150,
                    sort: !0
                }, {
                    field: 'osUser',
                    title: '远程用户',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'program',
                    title: '远程程序',
                    minWidth: 200,
                    sort: !0
                }, {
                    field: 'event',
                    title: '事件',
                    minWidth: 200,
                    sort: !0
                }, {
                    field: 'waitTime',
                    title: '等待时间',
                    minWidth: 200,
                    sort: !0
                }, {
                    field: 'sql',
                    title: 'SQL',
                    minWidth: 200,
                    sort: !0
                }, {
                    title: '操作',
                    hide: !authority,
                    width: 85,
                    align: 'center',
                    fixed: (device.ios || device.android) ? false : 'right',
                    toolbar: '#list-table-toolbar-detail'
                }]
            ],
            page: false,
            limit: 15,
            height: (device.ios || device.android) ? $(document).width() : $(document).width() * 0.5
        });
        // 点击表头排序
        table.on('sort(list-table)', function (obj) {
            //table.reload('list-table', {
            //  initSort: obj
            //});
        });
        //头工具栏事件
        table.on('toolbar(list-table)', function (obj) {
            if (obj.event === 'batchdel') {
                var checkStatus = table.checkStatus('list-table'), checkData = checkStatus.data; //得到选中的数据
                if (checkData.length === 0) {
                    return layer.msg('请选择数据');
                }
                layer.confirm('确定结束会话吗？', function (index) {
                    // 弹出loading框
                    var loadingIndex = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    admin.req({
                        type: 'delete',
                        url: ctxPath + 'db-session4oracle/destroy-session?id=' + id,
                        data: JSON.stringify(checkStatus.data),
                        dataType: 'json',
                        contentType: 'application/json;charset=utf-8',
                        headers: {
                            "X-CSRF-TOKEN": tokenValue
                        },
                        success: function (result) {
                            var data = result.data;
                            if (data === webConst.SUCCESS) {
                                table.reload('list-table'); //数据刷新
                                layer.msg('操作成功！', {icon: 6});
                            } else {
                                layer.msg('操作失败！', {icon: 5, shift: 6});
                            }
                            // 关闭loading框
                            layer.close(loadingIndex);
                        },
                        error: function () {
                            layer.msg('系统错误！', {icon: 5, shift: 6});
                            // 关闭loading框
                            layer.close(loadingIndex);
                        }
                    });
                });
            }
            // 刷新
            if (obj.event === 'batchRefresh') {
                table.reload('list-table'); //数据刷新
            }
        });
        //监听工具条
        table.on('tool(list-table)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                layer.confirm('确定结束会话吗？', function (index) {
                    // 弹出loading框
                    var loadingIndex = layer.load(1, {
                        shade: [0.1, '#fff'] //0.1透明度的白色背景
                    });
                    admin.req({
                        type: 'delete',
                        url: ctxPath + 'db-session4oracle/destroy-session?id=' + id,
                        data: JSON.stringify([data]),
                        dataType: 'json',
                        contentType: 'application/json;charset=utf-8',
                        headers: {
                            "X-CSRF-TOKEN": tokenValue
                        },
                        success: function (result) {
                            var data = result.data;
                            if (data === webConst.SUCCESS) {
                                obj.del();
                                table.reload('list-table'); //数据刷新
                                layer.msg('操作成功！', {icon: 6});
                            } else {
                                layer.msg('操作失败！', {icon: 5, shift: 6});
                            }
                            // 关闭loading框
                            layer.close(loadingIndex);
                        },
                        error: function () {
                            layer.msg('系统错误！', {icon: 5, shift: 6});
                            // 关闭loading框
                            layer.close(loadingIndex);
                        }
                    });
                });
            }
        });
    });
    e('dbSession4oracle', {});
});