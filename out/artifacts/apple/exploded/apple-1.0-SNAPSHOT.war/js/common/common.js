/*********************************************************************
 *                    通用命名空间的创建                                 *
 **********************************************************************/

var COMMON = COMMON || {};
COMMON.namespace = function (ns_string) {
    var parts = ns_string.split('.'),
        parent = COMMON,
        i;

    if (parts[0] === 'COMMON') {
        parts = parts.slice(1);
    }

    for (i = 0; i < parts.length; i++) {
        if (typeof parent[parts[i]] === 'undefined') {
            parent[parts[i]] = {};
        }
        parent = parent[parts[i]];
    }
    return parent;
};

//系统级别常量定义
COMMON.namespace('COMMON.constant.system');
COMMON.constant.system = {
    QTIP_Z_INDEX: 99999999
};

//qtips模块
COMMON.namespace('COMMON.qtipsModule');
COMMON.qtipsModule = (function () {
    var constant = COMMON.constant.system;
    if (jQuery.qtip) {
        //qtip最顶部
        $.fn.qtip.zindex = constant.QTIP_Z_INDEX;
    }

    //关闭所有提示
    var closeAllTips = function () {
        $('.qtip').each(function () {
            $(this).data('qtip').destroy();
        });
    };

    /**
     * 创建提示
     * @param selector 显示提示的选择器
     * @param msg   提示信息
     */
    var createTip = (function () {
        var tipConfig = {
            overwrite: true,
            content: {text: ""},
            show: {event: false, ready: true},
            style: {classes: 'qtip-cream qtip-shadow qtip-rounded'},
            position: {my: 'top left', at: 'bottom right'}
        };
        return function (selector, msg) {
            tipConfig.content.text = msg;
            $(selector).qtip(tipConfig);
        }
    })();


    /**错误显示**/
    var errorPlacement = function (error, element) {
        if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
            var eid = element.attr('name'); //获取元素的name属性
            element = $("input[name='" + eid + "']").last().parent(); //将错误信息添加当前元素的父结点后面
        }
        if (!error.is(':empty')) {
            $(element).not('.valid').qtip({
                overwrite: false,
                content: error,
                hide: false,
                show: {event: false, ready: true},
                style: {classes: 'qtip-cream qtip-shadow qtip-rounded'},
                position: {my: 'top left', at: 'bottom right'}
            }).qtip('option', 'content.text', error);
        }
        else {
            element.qtip('destroy');
        }
    };

    return {
        closeAllTips: closeAllTips,
        createTip:createTip,
        errorPlacement:errorPlacement
    };
})();

//layer模块
COMMON.namespace('COMMON.layerModule');
COMMON.layerModule = (function () {
    //统一添加公共模块的依赖
    var qtipsModule = COMMON.qtipsModule;
    /**
     * 公共弹框
     * @param param
     * {
     *    url:'',
     *    title:'',
     *    width:'',
     *    height:'',
     *    data: {} / [],
     *    success:function,
     *    cancel:function,
     *    end:function
     * }
     */
    var layerOpen = function (param) {
        var area = [param.width, param.height];
        if (param.height == 'auto') {
            area = width;
        }
        var str = JSON.stringify(param.data);
        var hidden = "<input id='param' type='hidden' value='" + str + "' />";
        $.get(param.url, function (html) {
            layer.open({
                type: 1,
                title: param.title,
                shift: 2,
                moveEnd: function () {
                    qtipsModule.closeAllTips();
                },
                //层弹出后的成功回调方法,success会携带两个参数，分别是当前层DOM当前层索引
                success: function (layero, index) {
                    param.success && param.success(layero, index);
                },
                //右上角关闭按钮触发的回调,
                // 该回调只携带当前层索引一个参数，无需进行手工关闭。如果不想关闭，return false即可
                cancel: function (index) {
                    if (!param.cancel) {
                        return true;
                    }
                    return param.cancel(index);
                },
                // 层销毁后触发的回调,无论是确认还是取消，只要层被销毁了，end都会执行，不携带任何参数
                end: function () {
                    qtipsModule.closeAllTips();
                    param.end && param.end();
                },
                shadeClose: false,
                content: html + hidden,
                area: area
            });
        });
    };

    //layer弹出层读取附带数据
    var getExtraData = function (name) {
        if (name) {
            return JSON.parse($(".layui-layer-content #param").val())[name];
        }
        return JSON.parse($(".layui-layer-content #param").val());
    };

    //弹窗关闭公共方法
    var layerClose = function () {
        layer.closeAll('page');
    };

    //弹窗关闭公共方法
    var layerCloseAllMsg = function () {
        layer.closeAll('dialog');
    };

    //取消弹窗
    function layerCancel() {
        $(".layui-layer-close").trigger("click");
        layer.closeAll('dialog'); //关闭信息框
    };

    //加载提示层
    var loadIndex;
    var openLoading = function () {
        loadIndex = layer.load(1, {
            shade: [0.3, '#000'],
            area: '64px'
        });
    };

    var closeLoading = function () {
        layer.close(loadIndex);
    };

    return {
        openLayer: layerOpen,
        getExtraData: getExtraData,
        closeLayer: layerClose,
        cancelLayer: layerCancel,
        closeAllMsg: layerCloseAllMsg,
        openLoading: openLoading,
        closeLoading: closeLoading
    };
})();

//缓存模块
COMMON.namespace('COMMON.storageModule');
COMMON.storageModule = (function () {
    //设置本地存储
    var setLocalValue = function (itemName, itemValue) {
        //存储，IE6~7 cookie 其他浏览器HTML5本地存储
        if (window.localStorage) {
            localStorage.setItem(itemName, JSON.stringify(itemValue));
        } else {
            Cookie.write(itemName, JSON.stringify(itemValue));
        }
    };

    //获得本地存储
    var getLocalValue = function (item, key) {
        if (key == undefined) {
            return JSON.parse(window.localStorage ? localStorage.getItem(item) : Cookie.read(item));
        } else {
            return JSON.parse(window.localStorage ? localStorage.getItem(item) : Cookie.read(item))[key];
        }
    };

    // 获取cookie
    var getCookie = function (name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    };

    return {
        setLocalValue: setLocalValue,
        getLocalValue: getLocalValue,
        getCookie: getCookie
    };
})();

//http模块
COMMON.namespace('COMMON.httpModule');
COMMON.httpModule = (function () {
    //获取url地址栏参数
    var getQueryString = function (name) {
        if ($.isNull(name)) {
            return window.location.search;
        }
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return unescape(r[2]);
        return null;
    };
    return {
        getQueryString: getQueryString
    };
})();

//time模块
COMMON.namespace('COMMON.timeModule');
COMMON.timeModule = (function () {

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
    //将时间戳改成年月日时分秒
    var timestamp2str = function (ms, format) {
        if (typeof ms == 'string') {
            ms = parseInt(ms);
        }
        format = format || 'yyyy-MM-dd hh:mm:ss';
        var timeLocal = new Date(ms);

        return timeLocal.format(format);
    };
    //日期字符串转时间戳
    var str2timestamp = function (dateStr) {
        var date = new Date(dateStr.replace(/-/g, '/'));
        return date.getTime().toString();
    };

    return {
        str2timestamp:str2timestamp,
        timestamp2str:timestamp2str
    };
})();

/*********************************************************************
 *                    扩展方法                                  *
 **********************************************************************/
// js中String添加replaceAll 方法
String.prototype.replaceAll = function (a, b) {
    var reg = new RegExp(a, "g");
    return this.replace(reg, b);
};
// js中String添加startWith方法
String.prototype.startWith = function (str) {
    var reg = new RegExp("^" + str);
    return reg.test(this);
}
// js中String添加endWith方法
String.prototype.endWith = function (str) {
    var reg = new RegExp(str + "$");
    return reg.test(this);
};


$.extend({
    //是否为空
    isNull: function (obj) {
        if (typeof(obj) == "undefined" || obj == "undefined") {
            return true;
        } else {
            return obj == null ? true : false;
        }
    },
    //获取IE版本
    msieVersion: function () {
        var ua = window.navigator.userAgent;
        var msie = ua.indexOf("MSIE ");
        if (msie > 0)      // If Internet Explorer, return version number
            return parseInt(ua.substring(msie + 5, ua.indexOf(".", msie)));
        else                 // If another browser, return 0
            return 0
    },
    //是否IE
    isIE: function () {
        return $.msieVersion() > 0 ? true : false;
    },
    //扩展兼容主流浏览器ajax（兼容IE8+、chrome、firefox...）
    ajaxFun: function (options) {
        //IE版本
        var msieVersion = $.msieVersion.call(this);
        // 设置默认参数
        var settings = $.extend({
            url: '',
            isPlain: true,      //content-type是否为空text/plain
            isXhr: false,    //检查跨域头（主平台）
            data: {},
            type: 'get',
            dataType: 'json',
            onSuccess: function (data) {
            },
            onError: function (data) {
            }
        }, options);
        settings.isCrossDomain = false;
        //http开头，默认外链
        if (settings.url.startWith("http")) {
            settings.isCrossDomain = true;
        }
        //IE浏览器（IE10以下）并且跨域请求
        if ($.isIE.call(this) && settings.isCrossDomain && msieVersion <= 9) {
            if (!settings.isPlain) {
                settings.type = "get";
            }
            // IE7 and lower can't do cross domain
            if (msieVersion <= 7) {
                alert("不支持IE8以下浏览器，请升级浏览器版本！");
                return;
            }
            // IE8 & 9 only Cross domain  request
            if (msieVersion == 8 || msieVersion == 9) {
                var xdr = new XDomainRequest(); // Use Microsoft XDR
                settings.data["t"] = Math.random();
                if (settings.type.toLocaleLowerCase() == "get") {
                    settings.url += ( ( /\?/ ).test(settings.url) ? "&" : "?") + $.param(settings.data);
                }
                xdr.open(settings.type, settings.url);
                xdr.onload = function () {
                    var dom = new ActiveXObject('Microsoft.XMLDOM'),
                        JSON = $.parseJSON(xdr.responseText);
                    dom.async = false;
                    if (JSON == null || typeof (JSON) == 'undefined') {
                        JSON = $.parseJSON(data.firstChild.textContent);
                    }
                    settings.onSuccess.call(this, JSON);
                };
                xdr.onerror = function (e) {
                    settings.onError.call(this, {});
                };
                xdr.send($.param(settings.data));
            }
        }
        //普通方式
        else {
            $.ajax({
                url: settings.url,
                type: settings.type,
                data: settings.data,
                cache: false,
                dataType: settings.dataType,
                xhrFields: {
                    withCredentials: settings.isXhr
                },
                success: function (data) {
                    settings.onSuccess.call(this, data);
                },
                error: function (data) {
                    avalon.log("error", data)
                    settings.onError.call(this, data);
                }
            });
        }
    },

    //配置处于debug模式
    inDebug:false,

    debugInfo:function (msg, obj) {
        if (this.inDebug){
            msg && console.log(msg+':');
            obj && console.log(obj);
        }
    }

});

//兼容不支持placeholder
(function ($) {
    /**
     * 没有开花的树
     * http://blog.csdn.net/mycwq/
     * 2012/11/28 15:12
     */

    var placeholderfriend = {
        focus: function (s) {
            s = $(s).hide().prev().show().focus();
            var idValue = s.attr("id");
            if (idValue) {
                s.attr("id", idValue.replace("placeholderfriend", ""));
            }
            var clsValue = s.attr("class");
            if (clsValue) {
                s.attr("class", clsValue.replace("placeholderfriend", ""));
            }
        }
    }

    //判断是否支持placeholder
    function isPlaceholer() {
        var input = document.createElement('input');
        return "placeholder" in input;
    }

    //不支持的代码
    if (!isPlaceholer()) {
        $(function () {

            var form = $(this);

            //遍历所有文本框，添加placeholder模拟事件
            var elements = form.find("input[type='text'][placeholder]");
            elements.each(function () {
                var s = $(this);
                var pValue = s.attr("placeholder");
                var sValue = s.val();
                if (pValue) {
                    if (sValue == '') {
                        s.val(pValue);
                    }
                }
            });

            elements.focus(function () {
                var s = $(this);
                var pValue = s.attr("placeholder");
                var sValue = s.val();
                if (sValue && pValue) {
                    if (sValue == pValue) {
                        s.val('');
                    }
                }
            });

            elements.blur(function () {
                var s = $(this);
                var pValue = s.attr("placeholder");
                var sValue = s.val();
                if (!sValue) {
                    s.val(pValue);
                }
            });

            //遍历所有密码框，添加placeholder模拟事件
            var elementsPass = form.find("input[type='password'][placeholder]");
            elementsPass.each(function (i) {
                var s = $(this);
                var pValue = s.attr("placeholder");
                var sValue = s.val();
                if (pValue) {
                    if (sValue == '') {
                        //DOM不支持type的修改，需要复制密码框属性，生成新的DOM
                        var html = this.outerHTML || "";
                        html = html.replace(/\s*type=(['"])?password\1/gi, " type=text placeholderfriend")
                            .replace(/\s*(?:value|on[a-z]+|name)(=(['"])?\S*\1)?/gi, " ")
                            .replace(/\s*placeholderfriend/, " placeholderfriend value='" + pValue
                                + "' " + "onfocus='placeholderfriendfocus(this);' ");
                        var idValue = s.attr("id");
                        if (idValue) {
                            s.attr("id", idValue + "placeholderfriend");
                        }
                        var clsValue = s.attr("class");
                        if (clsValue) {
                            s.attr("class", clsValue + "placeholderfriend");
                        }
                        s.hide();
                        s.after(html);
                    }
                }
            });

            elementsPass.blur(function () {
                var s = $(this);
                var sValue = s.val();
                if (sValue == '') {
                    var idValue = s.attr("id");
                    if (idValue) {
                        s.attr("id", idValue + "placeholderfriend");
                    }
                    var clsValue = s.attr("class");
                    if (clsValue) {
                        s.attr("class", clsValue + "placeholderfriend");
                    }
                    s.hide().next().show();
                }
            });

        });
    }
    window.placeholderfriendfocus = placeholderfriend.focus;
})(jQuery);
