/**
 * Created by Zhang Junwei on 2017/1/16 0016.
 */
;(function ($, window, document, undefined) {

    var defaults = {};

    var methods = {};
    $.fn.myPlugin = function (method, option) {
        var setting = $.extend({}, defaults, option);
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.tooltip');
        }
    };
})(jQuery, window, document);
