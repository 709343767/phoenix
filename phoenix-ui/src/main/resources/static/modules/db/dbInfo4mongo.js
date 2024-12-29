/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'table'], function () {
        var $ = layui.$, table = layui.table, device = layui.device();
        table.render({
            elem: '#list-table-mongo-info',
            url: ctxPath + 'db-info4mongo/get-mongo-info-list?id=' + id,
            toolbar: '#list-table-mongo-info-toolbar',
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
                    hide: true
                }, {
                    field: 'db',
                    width: 180,
                    title: 'db',
                    sort: !0
                }, {
                    field: 'collections',
                    title: 'collections',
                    minWidth: 115,
                    sort: !0
                }, {
                    field: 'objects',
                    title: 'objects',
                    minWidth: 80,
                    sort: !0
                }, {
                    field: 'indexes',
                    title: 'indexes',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'views',
                    title: 'views',
                    minWidth: 80,
                    sort: !0
                }, {
                    field: 'dataSize',
                    title: 'dataSize',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'avgObjSize',
                    title: 'avgObjSize',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'totalSize',
                    title: 'totalSize',
                    minWidth: 100,
                    sort: !0
                }, {
                    field: 'fsTotalSize',
                    title: 'fsTotalSize',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'fsUsedSize',
                    title: 'fsUsedSize',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'indexSize',
                    title: 'indexSize',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'storageSize',
                    title: 'storageSize',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'scaleFactor',
                    title: 'scaleFactor',
                    minWidth: 120,
                    sort: !0
                }, {
                    field: 'ok',
                    title: 'ok',
                    minWidth: 80,
                    sort: !0
                }]
            ],
            page: false,
            limit: 15,
            limits: [10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100],
            height: (device.ios || device.android) ? $(document).width() : $(document).width() * 0.5
        });
        // 点击表头排序
        table.on('sort(list-table-mongo-info)', function (obj) {
            //table.reload('list-table-mongo-info', {
            //  initSort: obj
            //});
        });
        //头工具栏事件
        table.on('toolbar(list-table-mongo-info)', function (obj) {
            // 刷新
            if (obj.event === 'batchRefresh') {
                table.reload('list-table-mongo-info'); //数据刷新
            }
        });
    });
    e('dbInfo4mongo', {});
});