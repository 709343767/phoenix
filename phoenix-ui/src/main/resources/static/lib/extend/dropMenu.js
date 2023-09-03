/*
   Authors: Jeff Lai,
   option:{
       type:0\1\2, 默认0
       ele:'#id'\$("#id"),
       eventName:'lay-event',默认lay-event
       eventNames:'lay-events',默认lay-events
       width:"150px",
       css:{"background":"red"},
       location:"c", //下拉菜单依靠位置，c居中、l左、r右
       align: "c", //菜单内容文本对齐方式，c居中、l左、r右
       data:[{title:'',event:'',icon:'layui-icon-search',isShow:true}], //菜单数据及事件
       event:{ e1:function(){}, e2:function(){} }, //事件处理
       done:function(dropMenu){ } //菜单渲染完成后触发
   }
*/
layui.define(['jquery', 'util'], function (exports) {
    var $ = layui.$, util = layui.util, device = layui.device();
    var basedir = layui.setter.base;
    layui.link(basedir + "layui/css/layui.dropMenu.css");

    function hide($menu) {
        $menu.css({
            height: 0,
            opacity: 0,
            visibility: "hidden"
        });
    }

    function show($menu) {
        $menu.css({
            height: "auto",
            opacity: 1,
            "z-index": 99999999,
            visibility: "visible"
        });
    }

    function clickopera(option) {
        option.ele.click(function (e) {
            e.preventDefault();
            var $menu = $(this).next();
            if ($menu.css("opacity") == 1) {
                hide($menu);
            } else {
                hide($(".layui-dropMenu"));
                show($menu);
                offset($(this), $menu, option);
            }
        });

        $(".layui-table-body").scroll(function () {
            hide($(".layui-dropMenu"));
        });

        if (device.android) {
            $(".layui-myDropMenuLi").click(function () {
                hide($(".layui-dropMenu"));
            });
        }
        //if (device.ios) {
        //    $(".layui-myDropMenuLi").bind("touchend", function () {
        //        hide($(".layui-dropMenu"));
        //    });
        //}
    }

    function mouseopera(option) {
        option.ele.parent().mouseover(
            function () {
                var $menu = $(this).children().last();
                show($menu);
                // 点击按钮后就隐藏
                //$(this).children().last().children().children().click(function () {
                //    hide($(".layui-dropMenu"));
                //});
                offset($(this).children().first(), $menu, option);
            }
        ).mouseout(function () {
            var $menu = $(this).children().last();
            hide($menu);
        });
    }

    function offset(ele, $menu, option) {
        var tt = 0;
        if ($(window).height() < (ele.offset().top + $menu.height() - window.scrollY + 20))
            tt = ele.offset().top - $menu.height() - window.scrollY;
        else
            tt = ele.offset().top + ele.height() - window.scrollY;

        var ll = 0;
        if (option.location == "c")
            ll = ele.offset().left - ($menu.width() / 3) - window.scrollX;
        else if (option.location == "r")
            ll = ele.offset().left - window.scrollX;
        else if (option.location == "l")
            ll = ele.offset().left - $menu.width() + ele.width() + 6 - window.scrollX;

        $menu.css({
            top: tt,
            left: ll
        });
    }

    function creatediv(option) {
        // console.time("creatediv");
        if (option.ele.attr(option.eventNames)) {
            $.each(option.ele, function (i, el) {
                var html = '';
                var events = $(el).attr(option.eventNames).split(",");
                var data = $.grep(option.data, function (v, i) {
                    return $.inArray(v.event, events) != -1;
                });
                $.each(data, function (i, v) {
                    if (v.isShow == false)
                        return;
                    if (v.type == "hr") {
                        html += `<li class="hr"></li>`;
                        return;
                    }
                    if (v.icon)
                        html += `<li class="layui-myDropMenuLi" ${option.eventName}="${v.event}"><i class="layui-icon ${v.icon}"> ${v.title}</i></li>`;
                    else
                        html += `<li class="layui-myDropMenuLi" ${option.eventName}="${v.event}"><i class="layui-icon"> ${v.title}</i></li>`;

                });
                html = `<ul class="layui-dropMenu">${html}</ul>`;

                $(el).wrap(`<div class="layui-inline"></div>`);
                $(el).after(html);
            });

        } else {
            var html = '';
            $.each(option.data, function (i, v) {
                if (v.isShow == false)
                    return;
                if (v.type == "hr") {
                    html += `<li class="hr"></li>`;
                    return;
                }
                if (v.icon)
                    html += `<li class="layui-myDropMenuLi" ${option.eventName}="${v.event}"><i class="layui-icon ${v.icon}"> ${v.title}</i></li>`;
                else
                    html += `<li class="layui-myDropMenuLi" ${option.eventName}="${v.event}"><i class="layui-icon"> ${v.title}</i></li>`;
            });
            html = `<ul class="layui-dropMenu">${html}</ul>`;
            option.ele.wrap(`<div class="layui-inline"></div>`);
            option.ele.after(html);
        }
        // console.timeEnd("creatediv");
    }

    //默认配置
    var defConfig = {
        type: 0,
        location: "c",
        align: "l",
        eventName: "lay-event",
        eventNames: "lay-events",
    };

    var obj = {
        config: defConfig,
        render: function (option) {
            if (!option.elem) {
                console.error("dropMenu elem is empty");
                return;
            }
            if (typeof option.elem == "string")
                option.ele = $(option.elem);
            else
                option.ele = option.elem;

            option = $.extend({}, defConfig, option);

            var isShowDatas = $.map(option.data, function (v, i) {
                return v.isShow != false ? v : null;
            });
            if (isShowDatas.length == 0) {
                // option.ele.hide();
                return;
            }

            creatediv(option);

            if (option.type == 0) {
                clickopera(option);
                mouseopera(option);
            } else if (option.type == 1) {
                clickopera(option);
            } else if (option.type == 2) {
                mouseopera(option);
            }

            if (option.width)
                option.ele.parent().find(".layui-dropMenu").width(option.width);

            if (option.align && option.align != "c") {
                if (option.align == "l")
                    option.ele.parent().find(".layui-dropMenu").css({textAlign: "left"});
                if (option.align == "r")
                    option.ele.parent().find(".layui-dropMenu").css({textAlign: "right"});
            }

            if (option.css)
                option.ele.parent().find(".layui-dropMenu").css(option.css);

            if (option.event)
                util.event(option.eventName, option.event);

            if (option.done)
                option.done(option.ele.parent().find(".layui-dropMenu"));
        }
    };
    exports('dropMenu', obj);
});