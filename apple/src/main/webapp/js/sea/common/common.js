if (jQuery.qtip) {
    //qtip最顶部
    $.fn.qtip.zindex = 9999;
}
if (typeof(layer)!="undefined") {
    layer.config({
        extend: ['skin/grow/style.css'], //加载您的扩展样式
        skin: 'layer-ext-grow'
    });
}


/*********************************************************************
 *                    全局变量 及方法                                 *
 **********************************************************************/

//身份字典
var tagNameAndNum = {
    "教师": "1",
    "家长": "2",
    "学生": "4",
    "教育机构": "8",
    "1": "教师",
    "4": "学生",
    "2": "家长",
    "8": "教育机构"
};
//星期列表
var timeDay = {
    "0": "星期天",
    "1": "星期一",
    "2": "星期二",
    "3": "星期三",
    "4": "星期四",
    "5": "星期五",
    "6": "星期六"
};

//avalon自定义过滤器
if(typeof(avalon)!="undefined") {
    avalon.filters.booleanFilter = function (str) {
        if (str) {
            return '是';
        }
        else {
            return '否';
        }
    }
    avalon.filters.frequencyFilter = function (str) {
        return str == 0 ? '按日' : '按学期';
    }
    avalon.filters.topicTypeFilter = function (str) {
        return str == 0 ? '按每日评价' : '按每学期评价';
    }
    avalon.filters.scoreTypeFilter = function (str) {
        return str == 0 ? '百分制' : '等第制';
    }
    avalon.filters.emptyRemarkFilter = function (str) {
        return str || "无评价说明";
    }
    avalon.filters.userTypeFilter = function (str) {
        return tagNameAndNum[str];
    }
    avalon.filters.examTypeFilter = function (type) {
        if (type == 1) return "期末";
        else if (type == 2) return "期中";
        else if (type == 3) return "月考";
    }
    avalon.filters.BMIDegreeFilter = function (type) {
        var str = '正常';
        switch (type) {
            case 2:
                str = '低体重';
                break;
            case 3:
                str = '超重';
                break;
            case 4:
                str = '肥胖';
                break;
        }
        return str;
    }
    avalon.filters.degreeFilter = function (type) {
        var str;
        switch (type) {
            case 1:
                str = '优秀';
                break;
            case 2:
                str = '良好';
                break;
            case 3:
                str = '及格';
                break;
            case 4:
                str = '不及格';
                break;
        }
        return str;
    }
    /**自定义过滤**/
    avalon.filters.activityTypeFilter = function (str) {
        return str==1 ? '社区服务':'社会实践';
    }
}


var THIRDCONFIG = "thirdConfig";

//loading
var loadIndex;
var openLoading = function () {
    loadIndex = layer.load(1, {
        shade: [0.3, '#000'],
        area: '64px'
    });
}
var closeLoading = function () {
    layer.close(loadIndex);
}
//时间长度
var timeLength = function (param) {
    param += "";
    return param.length === 2 ? param : "0" + param;
};
//将时间戳改成年月日时分秒
var timeFormat = function (ms, showDay) {
    if (typeof ms == 'string') {
        ms = parseInt(ms);
    }
    var timeLocal = new Date(ms);
    var year = timeLocal.getYear() + 1900;
    var month = timeLength(timeLocal.getMonth() + 1);
    var day = timeLength(timeLocal.getDate());
    var hour = timeLength(timeLocal.getHours());
    var minutes = timeLength(timeLocal.getMinutes());
    var second = timeLength(timeLocal.getSeconds());
    var weekDay = timeDay[timeLocal.getDay()];
    if (!showDay) {
        var timeNew = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second;
    }
    else {
        var timeNew = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second + " " + weekDay;
    }

    return timeNew;
};
//日期字符串转时间戳
var stringToTimestamp = function (dateStr) {
    avalon.log(dateStr);
    var date = new Date(dateStr.replace(/-/g, '/'));
    return date.getTime().toString();
};
//是否成功返回
var isSuccess = function (result) {
    var rtnCode = result.rtnCode || result;
    if (result.uri && rtnCode == "0000001") {//登录无效
        window.location.href = result.uri;
        return false;
    }
    if (rtnCode == '0000000')
        return true;
    //没权限则弹窗提示
    if (rtnCode == '0100041') {
        window.location.href = '/html/error/noAuth.html';
    }
    return false;
}

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
};
// js中String添加endWith方法
String.prototype.endWith = function (str) {
    var reg = new RegExp(str + "$");
    return reg.test(this);
};
// js中Array指定位置插入元素
Array.prototype.insert = function (index, item) {
    this.splice(index, 0, item);
};


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
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
}

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
            return parseInt(ua.substring(msie + 5, ua.indexOf(".", msie)))
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
            async: true,
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
                    dom.async = settings.async;
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
                async: settings.async,
                dataType: settings.dataType,
                xhrFields: {
                    withCredentials: settings.isXhr
                },
                success: function (data) {
                    settings.onSuccess.call(this, data);
                },
                error: function (data) {
                    typeof(avalon)!="undefined"&&avalon.log("error", data);
                    settings.onError.call(this, data);
                }
            });
        }
    }
});
/*********************************************************************
 *                    公用方法                                  *
 **********************************************************************/

//设置本地存储
var setLocalValue = function (itemName, itemValue) {
    //存储，IE6~7 cookie 其他浏览器HTML5本地存储
    if (window.localStorage) {
        localStorage.setItem(itemName, JSON.stringify(itemValue));
    } else {
        Cookie.write(itemName, JSON.stringify(itemValue));
    }
}

//获得本地存储
var getLocalValue = function (item, key) {
    if (key == undefined) {
        return JSON.parse(window.localStorage ? localStorage.getItem(item) : Cookie.read(item));
    } else {
        return JSON.parse(window.localStorage ? localStorage.getItem(item) : Cookie.read(item))[key];
    }
}

//判断三种环境
var getEnvironment = function () {
    if (window.document.location.host.indexOf("dev") >= 0 || window.document.location.host.indexOf("127.0.0.1") >= 0 || window.document.location.host.indexOf("localhost") >= 0) {
        return 'dev';
    } else if (window.document.location.host.indexOf("test") >= 0) {
        return 'test';
    } else {
        return 'pro';
    }
}

//获取用户信息并设置在本地存储
var getUserInfo = function (callback) {
    openLoading();
    $.ajaxFun({
        url: "/sys/user/getUserInfo",
        onSuccess: function (data) {
            if (isSuccess(data)) {
                var user = data.bizData.account;
                setLocalValue("account", user);
                setLocalValue("projectInfo", data.bizData.projectInfo);
                setLocalValue("H5URL", data.bizData.H5URL);
                var env = getEnvironment();
                var url;
                if (env == "dev") {
                    url = 'http://open.dev.qky100.com/v2/eSAASProductsByJsonp';
                } else {
                    url = 'http://open.qky100.com/v2/eSAASProductsByJsonp';
                }
                $.ajax({
                    url: url, //实际环境地址
                    type: "GET",
                    dataType: "jsonp",//注意：解决跨域，只能写成该类型
                    cache: false,
                    timeout: 6000,
                    jsonp: 'callback',//注意：该参数指定为callback,服务器端才能接收，解决跨域问题
                    data: {
                        accessToken: $.cookie("access_token"),
                        uid: user.uid,
                        userType: user.userType,
                        agencyCode: user.agencyCode,
                        schoolCode: user.schoolCode,
                        loginSource: data.bizData.loginSource
                    },
                    success: function (res) {
                        setLocalValue("esaasProducts", res.bizData);
                        if (callback)callback(data.bizData);
                    },
                    complete: function () {
                        closeLoading();
                    }
                });
            }
        }
    });
}

//layer弹出层读取参数
var getParam = function (name) {
    return JSON.parse($(".layui-layer-content #param").val())[name];
}

//弹窗公共方法
var layerOpen = function (url, title, data, width, height, afterSave, afterCancel, afterOpen) {
    var area = [width, height];
    if (height == 'auto') {
        area = width;
    }
    var str = JSON.stringify(data);
    var hidden = "<input id='param' type='hidden' value='" + str + "' />";
    $.get(url, function (html) {
        var layerIndex = layer.open({
            type: 1,
            zIndex:888,
            title: title,
            shift: 2,
            moveEnd: function () {
                closeAllTip();
            },
            cancel: function () {
                afterCancel && afterCancel();
                //取消时重置回调，避免刷新
                afterSave = function () {
                };
            },
            end: function () {
                closeAllTip();
                //为了保存后刷新
                afterSave && afterSave();
            },
            shadeClose: false,
            content: html + hidden,
            area: area
        });
        afterOpen && afterOpen(layerIndex);
    });
}

//弹窗关闭公共方法
var layerClose = function () {
    layer.closeAll('page');
}

//弹窗关闭公共方法
var layerCloseAllMsg = function () {
    layer.closeAll('dialog'); //关闭信息框
}

//关闭所有提示
function closeAllTip() {
    $('.qtip').each(function () {
        $(this).data('qtip').destroy();
    })
}

//取消弹窗
function layerCancel() {
    $(".layui-layer-close").trigger("click");
    layer.closeAll('dialog'); //关闭信息框
}

//读取公共配置项，如果没有则读取本机配置项
var getCommonConfigFun = function () {
    $.ajax({
        url: '/commonConfig.json',
        type: 'get',
        cache: false,
        dataType: "json",
        success: function (res) {
            getCloudConfig(res["cloudyDisk"]);
            setLocalValue("cloudyDisk", res["cloudyDisk"]);
        },
        error: function (res) {
            justForMeFun();
        }
    });
};

var getCloudConfig = function (url) {
    $.ajax({
        url: '/config/thirdConfig.json',
        type: 'get',
        cache: false,
        dataType: "json",
        success: function (res) {
            for (var key in res) {
                res[key] = url + res[key];
            }
            setLocalValue("thirdConfig", res);
        }
    });
}

//本地私有配置项
var justForMeFun = function () {
    $.ajax({
        url: '/config/configForMe.json',
        type: 'get',
        cache: false,
        dataType: "json",
        success: function (res) {
            getCloudConfig(res["cloudyDisk"]);
            setLocalValue("cloudyDisk", res["cloudyDisk"]);
        },
        error: function (res) {
            console.log("res", res);
        }
    });
};

//获取url地址栏参数
function getQueryString(name) {
    if ($.isNull(name)) {
        return window.location.search;
    }
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}
//获取url地址参数，==
var getParameterByName = function (name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


// 获取cookie
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

//根据id获取云盘文件信息
function getCloudFileById() {
    var callback, id;
    for (var i in arguments) {
        var el = arguments[i];
        callback = avalon.isFunction(el) ? el : callback;
        id = avalon.type(el) == "number" ? el : id;
    }
    if (callback && id) {
        $.ajaxFun({
            url: JSON.parse(localStorage.getItem(THIRDCONFIG))["cloudyDiskfetchOneFile"],
            type: 'post',
            isXhr: true,
            isPlain: false,
            data: {
                'fileId': id
            },
            onSuccess: function (res) {
                callback(res);
            }
        });
    }
}

/**错误显示**/
function errorPlacement(error, element) {
    if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
        var eid = element.attr('name'); //获取元素的name属性
        element = $("input[name='" + eid + "']").last().parent(); //将错误信息添加当前元素的父结点后面
    }
    if (!error.is(':empty')) {

        $(element).not('.valid').qtip({
            overwrite: false,
            content: error,
            hide: false,
            show: {
                event: false,
                ready: true
            },
            style: {
                classes: 'qtip-cream qtip-shadow qtip-rounded'
            },
            position: {
                my: 'top left',
                at: 'bottom right'
            }
        }).qtip('option', 'content.text', error);
    }
    else {
        element.qtip('destroy');
    }
}

//初始化用户行为统计
if (jQuery.fn.userBehavior) {
    jQuery.fn.userBehavior({
        selector: ".qky_content", productCode: "grow",
        distinctId: getLocalValue("account", "uid"), userParams: {subAppCode: "QKY_GROW_PC"}, debug: true
    });
}

//初始化权限控件
if(jQuery.fn.authController){
    //把html节点设置的ac-moduleUrl属性作为当前模块url
    var moduleUrl = $("html").attr('ac-moduleUrl');
    if(typeof moduleUrl == "undefined") {
        moduleUrl = jQuery.fn.authController.defaults.moduleUrl;
    }
    //根据具体项目应用情况，覆盖默认配置
    jQuery.extend(jQuery.fn.authController.defaults, {
        ctrlMode: 'visible',
        moduleUrl: moduleUrl,
        serviceUrl: '/auth/getPermissions',
        //对受控元素渲染之后的额外操作
        afterRender: function ($obj, hasPermission) {
            if (!hasPermission) {
                $obj.removeAttr('ms-click').attr('title', '你没有该操作权限');
            }
        }
    });
    jQuery(function(){
        $('.judge_nav li[ac-authCode],a[ac-authCode],input[ac-authCode]').authController();
    });
}


//初始化jquery.mqtt
if (jQuery.fn.mqtt) {
    $.ajaxFun({
        url: "/sys/config/mqtt",
        onSuccess: function (data) {
            if (isSuccess(data)) {
                jQuery.extend($.fn.mqtt.defaults, {
                    host: data.bizData.mqtt_host, port: data.bizData.mqtt_websocket_port,
                    clientId: 'qky_grow-' + Math.round(Math.random()*Math.pow(10,5)).toString() + "-" + new Date().getTime().toString()
                });
                var bizModule=jQuery("html").attr("bizModule");
                var onMessageArrivedFun = jQuery("html").attr("onMessageArrived");
                if(bizModule&&onMessageArrivedFun){
                    jQuery(document).mqtt({
                        topic:data.bizData.env+'/'+bizModule,
                        onMessageArrived:eval(onMessageArrivedFun)
                    });
                }
            }
        }
    });
}

//兼容不支持placeholder
!function (a) {
    "use strict";
    function b() {
    }

    function c() {
        try {
            return document.activeElement
        } catch (a) {
        }
    }

    function d(a, b) {
        for (var c = 0, d = a.length; d > c; c++)if (a[c] === b)return !0;
        return !1
    }

    function e(a, b, c) {
        return a.addEventListener ? a.addEventListener(b, c, !1) : a.attachEvent ? a.attachEvent("on" + b, c) : void 0
    }

    function f(a, b) {
        var c;
        a.createTextRange ? (c = a.createTextRange(), c.move("character", b), c.select()) : a.selectionStart && (a.focus(), a.setSelectionRange(b, b))
    }

    function g(a, b) {
        try {
            return a.type = b, !0
        } catch (c) {
            return !1
        }
    }

    function h(a, b) {
        if (a && a.getAttribute(B)) b(a); else for (var c, d = a ? a.getElementsByTagName("input") : N, e = a ? a.getElementsByTagName("textarea") : O, f = d ? d.length : 0, g = e ? e.length : 0, h = f + g, i = 0; h > i; i++)c = f > i ? d[i] : e[i - f], b(c)
    }

    function i(a) {
        h(a, k)
    }

    function j(a) {
        h(a, l)
    }

    function k(a, b) {
        var c = !!b && a.value !== b, d = a.value === a.getAttribute(B);
        if ((c || d) && "true" === a.getAttribute(C)) {
            a.removeAttribute(C), a.value = a.value.replace(a.getAttribute(B), ""), a.className = a.className.replace(A, "");
            var e = a.getAttribute(I);
            parseInt(e, 10) >= 0 && (a.setAttribute("maxLength", e), a.removeAttribute(I));
            var f = a.getAttribute(D);
            return f && (a.type = f), !0
        }
        return !1
    }

    function l(a) {
        var b = a.getAttribute(B);
        if ("" === a.value && b) {
            a.setAttribute(C, "true"), a.value = b, a.className += " " + z;
            var c = a.getAttribute(I);
            c || (a.setAttribute(I, a.maxLength), a.removeAttribute("maxLength"));
            var d = a.getAttribute(D);
            return d ? a.type = "text" : "password" === a.type && g(a, "text") && a.setAttribute(D, "password"), !0
        }
        return !1
    }

    function m(a) {
        return function () {
            P && a.value === a.getAttribute(B) && "true" === a.getAttribute(C) ? f(a, 0) : k(a)
        }
    }

    function n(a) {
        return function () {
            l(a)
        }
    }

    function o(a) {
        return function () {
            i(a)
        }
    }

    function p(a) {
        return function (b) {
            return v = a.value, "true" === a.getAttribute(C) && v === a.getAttribute(B) && d(x, b.keyCode) ? (b.preventDefault && b.preventDefault(), !1) : void 0
        }
    }

    function q(a) {
        return function () {
            k(a, v), "" === a.value && (a.blur(), f(a, 0))
        }
    }

    function r(a) {
        return function () {
            a === c() && a.value === a.getAttribute(B) && "true" === a.getAttribute(C) && f(a, 0)
        }
    }

    function s(a) {
        var b = a.form;
        b && "string" == typeof b && (b = document.getElementById(b), b.getAttribute(E) || (e(b, "submit", o(b)), b.setAttribute(E, "true"))), e(a, "focus", m(a)), e(a, "blur", n(a)), P && (e(a, "keydown", p(a)), e(a, "keyup", q(a)), e(a, "click", r(a))), a.setAttribute(F, "true"), a.setAttribute(B, T), (P || a !== c()) && l(a)
    }

    var t = document.createElement("input"), u = void 0 !== t.placeholder;
    if (a.Placeholders = {nativeSupport: u, disable: u ? b : i, enable: u ? b : j}, !u) {
        var v, w = ["text", "search", "url", "tel", "email", "password", "number", "textarea"], x = [27, 33, 34, 35, 36, 37, 38, 39, 40, 8, 46], y = "#ccc", z = "placeholdersjs", A = new RegExp("(?:^|\\s)" + z + "(?!\\S)"), B = "data-placeholder-value", C = "data-placeholder-active", D = "data-placeholder-type", E = "data-placeholder-submit", F = "data-placeholder-bound", G = "data-placeholder-focus", H = "data-placeholder-live", I = "data-placeholder-maxlength", J = 100, K = document.getElementsByTagName("head")[0], L = document.documentElement, M = a.Placeholders, N = document.getElementsByTagName("input"), O = document.getElementsByTagName("textarea"), P = "false" === L.getAttribute(G), Q = "false" !== L.getAttribute(H), R = document.createElement("style");
        R.type = "text/css";
        var S = document.createTextNode("." + z + " {color:" + y + ";}");
        R.styleSheet ? R.styleSheet.cssText = S.nodeValue : R.appendChild(S), K.insertBefore(R, K.firstChild);
        for (var T, U, V = 0, W = N.length + O.length; W > V; V++)U = V < N.length ? N[V] : O[V - N.length], T = U.attributes.placeholder, T && (T = T.nodeValue, T && d(w, U.type) && s(U));
        var X = setInterval(function () {
            for (var a = 0, b = N.length + O.length; b > a; a++)U = a < N.length ? N[a] : O[a - N.length], T = U.attributes.placeholder, T ? (T = T.nodeValue, T && d(w, U.type) && (U.getAttribute(F) || s(U), (T !== U.getAttribute(B) || "password" === U.type && !U.getAttribute(D)) && ("password" === U.type && !U.getAttribute(D) && g(U, "text") && U.setAttribute(D, "password"), U.value === U.getAttribute(B) && (U.value = T), U.setAttribute(B, T)))) : U.getAttribute(C) && (k(U), U.removeAttribute(B));
            Q || clearInterval(X)
        }, J);
        e(a, "beforeunload", function () {
            M.disable()
        })
    }
}(this), function (a, b) {
    "use strict";
    var c = a.fn.val, d = a.fn.prop;
    b.Placeholders.nativeSupport || (a.fn.val = function (a) {
        var b = c.apply(this, arguments), d = this.eq(0).data("placeholder-value");
        return void 0 === a && this.eq(0).data("placeholder-active") && b === d ? "" : b
    }, a.fn.prop = function (a, b) {
        return void 0 === b && this.eq(0).data("placeholder-active") && "value" === a ? "" : d.apply(this, arguments)
    })
}(jQuery, this);