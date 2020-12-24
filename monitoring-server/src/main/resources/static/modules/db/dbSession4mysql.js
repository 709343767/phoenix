/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'table'], function () {
        var $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table, device = layui.device();
        table.render({
            elem: '#list-table',
            url: ctxPath + 'db-session4mysql/get-Session-list?id=' + id,
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
            done: function () {
                $('[data-field="isOnline"]').children().each(function () {
                    if ($(this).text() === '0') {
                        $(this).html('<span style="color: #FF4500;">离线</span>');
                    }
                    if ($(this).text() === '1') {
                        $(this).text('正常');
                    }
                    if ($(this).text() === null || $(this).text() === '' || $(this).text() === undefined) {
                        $(this).text('未知');
                    }
                });
            },
            cols: [
                [{
                    type: 'checkbox'
                }, {
                    field: 'id',
                    width: 70,
                    title: 'ID',
                    sort: !0
                }, {
                    field: 'connName',
                    title: '连接名称',
                    minWidth: 150,
                    sort: !0
                }, {
                    field: 'url',
                    title: 'URL',
                    minWidth: 200,
                    sort: !0
                }, {
                    field: 'dbType',
                    title: '类型',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'isOnline',
                    title: '状态',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'dbDesc',
                    title: '描述',
                    minWidth: 200,
                    sort: !0
                }, {
                    field: 'insertTime',
                    title: '新增时间',
                    minWidth: 170,
                    sort: !0
                }, {
                    field: 'updateTime',
                    title: '更新时间',
                    minWidth: 170,
                    sort: !0
                }, {
                    title: '操作',
                    width: 85,
                    align: 'center',
                    fixed: (device.ios || device.android) ? false : 'right',
                    toolbar: '#list-table-toolbar-detail'
                }]
            ],
            page: !0,
            limit: 15,
            height: 'full-240'
        });
        //监听搜索
        form.on('submit(list-table-search)', function (data) {
            var field = data.field;
            //执行重载
            table.reload('list-table', {
                where: field
            });
        });
        // 点击表头排序
        table.on('sort(list-table)', function (obj) {
            table.reload('list-table', {
                initSort: obj
            });
        });
        //监听工具条
        table.on('tool(list-table)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                layer.confirm('确定删除吗？', function (index) {
                    admin.req({
                        type: 'post',
                        url: ctxPath + 'monitor-db/delete-monitor-db',
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
                                layer.msg('删除成功！', {icon: 6});
                            } else {
                                layer.msg('删除失败！', {icon: 5, shift: 6});
                            }
                        },
                        error: function () {
                            layer.msg('系统错误！', {icon: 5, shift: 6});
                        }
                    });
                });
            }
        });
    });
    e('dbSession4mysql', {});
});