/** layuiAdmin.std-v2020.4.1 LPPL License By 皮锋 */
;layui.define(function (e) {
    layui.use(['index', 'element', 'layer'], function () {
        var $ = layui.$, admin = layui.admin, element = layui.element, layer = layui.layer;
        // 弹出loading框
        var loadingIndex = layer.load(1, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
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
                // 定义要展示的内容
                var server, clients, memory, persistence, stats, replication, cpu, cluster, keyspace;
                if (!isEmpty(info)) {
                    info = info.replace(new RegExp('\r\n\r\n', 'g'), '<br><br>')
                        .replace(new RegExp('\n\r\n\r', 'g'), '<br><br>')
                        .replace(new RegExp('\n\r', 'g'), '<br>')
                        .replace(new RegExp('\r\n', 'g'), '<br>');
                    var infos = info.split('<br><br>');
                    for (var i = 0; i < infos.length; i++) {
                        if (infos[i].indexOf('# Server') !== -1) {
                            server = infos[i].replace('# Server<br>', '');
                        }
                        if (infos[i].indexOf('# Clients') !== -1) {
                            clients = infos[i].replace('# Clients<br>', '');
                        }
                        if (infos[i].indexOf('# Memory') !== -1) {
                            memory = infos[i].replace('# Memory<br>', '');
                        }
                        if (infos[i].indexOf('# Persistence') !== -1) {
                            persistence = infos[i].replace('# Persistence<br>', '');
                        }
                        if (infos[i].indexOf('# Stats') !== -1) {
                            stats = infos[i].replace('# Stats<br>', '');
                        }
                        if (infos[i].indexOf('# Replication') !== -1) {
                            replication = infos[i].replace('# Replication<br>', '');
                        }
                        if (infos[i].indexOf('# CPU') !== -1) {
                            cpu = infos[i].replace('# CPU<br>', '');
                        }
                        if (infos[i].indexOf('# Cluster') !== -1) {
                            cluster = infos[i].replace('# Cluster<br>', '');
                        }
                        if (infos[i].indexOf('# Keyspace') !== -1) {
                            keyspace = infos[i].replace('# Keyspace', '');
                        }
                    }
                }
                var html = '<div class="layui-col-xs12 layui-col-sm4">' +
                    '          <div class="layui-collapse">' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Server</h2>' +
                    '                <div class="layui-colla-content layui-show">' + server + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Clients</h2>' +
                    '                <div class="layui-colla-content layui-show">' + clients + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Cluster</h2>' +
                    '                <div class="layui-colla-content layui-show">' + cluster + '</div>' +
                    '             </div>' +
                    '          </div>' +
                    '       </div>' +
                    '       <div class="layui-col-xs12 layui-col-sm4">' +
                    '          <div class="layui-collapse">' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Memory</h2>' +
                    '                <div class="layui-colla-content layui-show">' + memory + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">CPU</h2>' +
                    '                <div class="layui-colla-content layui-show">' + cpu + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Stats</h2>' +
                    '                <div class="layui-colla-content layui-show">' + stats + '</div>' +
                    '             </div>' +
                    '          </div>' +
                    '       </div>' +
                    '       <div class="layui-col-xs12 layui-col-sm4">' +
                    '          <div class="layui-collapse">' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Replication</h2>' +
                    '                <div class="layui-colla-content layui-show">' + replication + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Persistence</h2>' +
                    '                <div class="layui-colla-content layui-show">' + persistence + '</div>' +
                    '             </div>' +
                    '             <div class="layui-colla-item">' +
                    '                <h2 class="layui-colla-title">Keyspace</h2>' +
                    '                <div class="layui-colla-content layui-show">' + keyspace + '</div>' +
                    '             </div>' +
                    '          </div>' +
                    '       </div>';
                $('#redis-info').html(html);
                //更新全部
                element.render();
                // 关闭loading框
                layer.close(loadingIndex);
            },
            error: function () {
                // 关闭loading框
                layer.close(loadingIndex);
            }
        });
    });
    e('dbInfo4redis', {});
});