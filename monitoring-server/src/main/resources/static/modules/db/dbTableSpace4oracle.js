/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'table'], function () {
        var $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table, device = layui.device();
        table.render({
            elem: '#list-table-space',
            url: ctxPath + 'db-tablespace4oracle/get-tablespace-list?id=' + id,
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
                    field: 'fileId',
                    width: 100,
                    title: '文件ID',
                    sort: !0
                }, {
                    field: 'fileName',
                    width: 400,
                    title: '文件名',
                    sort: !0
                }, {
                    field: 'tablespaceName',
                    title: '表空间名',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'total',
                    title: '总空间',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'used',
                    title: '使用空间',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'free',
                    title: '剩余空间',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'usedPer',
                    title: '使用率(%)',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'freePer',
                    title: '剩余率(%)',
                    minWidth: 100,
                    sort: !0
                }]
            ],
            page: false,
            limit: 15,
            height: (device.ios || device.android) ? $(document).width() : $(document).width() * 0.5
        });
        // 点击表头排序
        table.on('sort(list-table-space)', function (obj) {
            //table.reload('list-table', {
            //  initSort: obj
            //});
        });
    });
    e('dbTableSpace4oracle', {});
});