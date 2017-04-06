/**
 * 通用脚本(依赖zepto、layerMobile)
 */
;(function (root, factory) {
    if (typeof define === 'function' && define.cmd) {
        define(function (require, exports, module) {
            module.exports = factory(require('zepto'), require('layer_mobile'));
        });
    } else if (typeof exports === 'object') {
        module.exports = factory();
    } else {
        root.common = factory(jQuery, layer);
    }
})(this, function ($, layer) {
    "use strict";
    var common = {
        //身份字典
        tagNameAndNum: {
            "教师": "1",
            "家长": "2",
            "学生": "4",
            "教育机构": "8",
            "1": "教师",
            "4": "学生",
            "2": "家长",
            "8": "教育机构"
        },
        //身份默认头像
        roleImg: {
            "1": "/images/tea.jpg",
            "2": "/images/parent.jpg",
            "4": "/images/stu.png",
            "8": "/images/edu.png"
        },
        //打开loading层
        openLoading: function () {
            return layer.open({type: 2});
        },
        //关闭loading层
        closeLoading: function (loadIndex) {
            loadIndex != undefined ? layer.close(loadIndex) : layer.closeAll();
        },
        //获取url参数
        getParameter: function (name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, "\\$&");
            var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, " "));
        },
        //判断请求返回是否成功
        isSuccess: function (result) {
            var rtnCode = result.rtnCode || result;
            if (result.uri && rtnCode == "0000001") {//登录无效
                window.location.href = result.uri;
                return false;
            }
            return "0000000" == rtnCode;
        },
        //封装ajax请求
        ajaxFun: function (options) {
            // 设置默认参数
            var settings = $.extend({
                url: '',
                loading: true,
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
            //请求统一打开loading层
            if (settings.loading){
                var loadIndex = common.openLoading();
            }
            //普通方式
            $.ajax({
                url: settings.url,
                type: settings.type,
                data: settings.data,
                cache: false,
                dataType: settings.dataType,
                xhrFields: {
                    withCredentials: settings.isXhr
                },
                complete: function () {
                    if (settings.loading) {
                        //关闭loading层
                        common.closeLoading(loadIndex);
                    }
                },
                success: function (data) {
                    if (common.isSuccess(data)) {
                        settings.onSuccess.call(this, data.bizData ? data.bizData : data);
                    } else {
                        console.log("请求出错");
                    }
                },
                error: function (data) {
                    console.log("ajax error", data);
                    settings.onError.call(this, data);
                }
            });

        },
        //设置本地存储
        setLocalValue: function (itemName, itemValue) {
            //浏览器HTML5本地存储
            localStorage.setItem(itemName, JSON.stringify(itemValue));
        },
        //获得本地存储
        getLocalValue: function (itemName, valueKey) {
            if (valueKey){
                return (JSON.parse(localStorage.getItem(itemName)))[valueKey];
            }
            return JSON.parse(localStorage.getItem(itemName));
        },
        //清除本地存储项
        removeLocalItem: function (itemName) {
            localStorage.removeItem(itemName);
        },
        //同步获取模板html
        getTemplateHtml:function (url) {
            var template = '';
            $.ajax({
                url: url,
                async: false,
                dataType: 'html',
                success: function (html) {
                    template = html;
                }
            });
            return template;
        },
        //初始化方法
        init: function () {
        },
        //获取当前登录用户的身份代码；1 => 教师；2 => 家长； 4 => 学生； 8 => 机构；'' => 未知
        getCurrUserType:function () {
            return this.getLocalValue('account', 'userType') || '';
        },
        getCurrUid:function () {
            return this.getLocalValue('account', 'uid') || '';
        },

        getGradesAndClasses:function (successCallback) {
            this.ajaxFun({
                url: "/grow/eval/getGradeAndClassBySchool",
                type: "get",
                onSuccess: function (data) {
                    successCallback && successCallback(data);
                }
            });
        }
    };
    common.init();
    return common;
});