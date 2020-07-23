/** layuiAdmin.std-v2020.4.1 LPPL License By https://www.layui.com/admin/ */
;layui.define(['table', 'form'], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        elem: '#LAY-user-manage',
        url: layui.setter.base + 'user/get-monitor-user-list',
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
        cols: [[
            {
                type: 'checkbox',
                fixed: 'left'
            }, {
                field: 'id',
                width: 100,
                title: 'ID',
                sort: !0
            }, {
                field: 'account',
                title: '账号',
                minWidth: 100
            }, {
                field: 'username',
                title: '用户名',
                minWidth: 100
            }, {
                field: 'roleName',
                title: '角色',
                minWidth: 100
            }, {
                field: 'email',
                title: '邮箱'
            }, {
                field: 'registerTime',
                title: '注册时间',
                sort: !0
            }, {
                field: 'updateTime',
                title: '更新时间',
                sort: !0
            }, {
                title: '操作',
                width: 150,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-useradmin-webuser'
            }]],
        page: !0,
        limit: 30,
        height: 'full-220',
        text: '对不起，加载出现异常！'
    });
    e('user/list', {});
});