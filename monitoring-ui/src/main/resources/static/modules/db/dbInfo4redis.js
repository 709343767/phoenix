/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'table'], function () {
        var $ = layui.$, admin = layui.admin;
        admin.req({
            type: 'get',
            url: layui.setter.base + 'db-info4redis/get-redis-info',
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            headers: {
                "X-CSRF-TOKEN": tokenValue
            },
            data: {
                id: id, // 数据库主键ID
            },
            success: function (result) {
                var info = result.data;
                if (!isEmpty(info)) {
                    info = info.replace(new RegExp('\r\n\r\n', 'g'), '<br><br>')
                        .replace(new RegExp('\n\r\n\r', 'g'), '<br><br>')
                        .replace(new RegExp('\n\r', 'g'), '<br>')
                        .replace(new RegExp('\r\n', 'g'), '<br>');
                }
                $('#redis-info').html(info);
            }
        });
    });
    e('dbInfo4redis', {});
});