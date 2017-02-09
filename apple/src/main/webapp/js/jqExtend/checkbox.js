/**
 * Created by Zhang Junwei on 2017/1/16 0016.
 */
;(function ($, window, document, undefined) {

    var defaults = {};

    var methods = {};
    $.fn.myPlugin = function (method, option) {
        var setting = $.extend({}, defaults, option);
        var result;
        if (methods[method]) {
            result = methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            result = methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.tooltip');
        }
        return result ? result : this;
    };
})(jQuery, window, document);
