define(function (require) {
    var $ = require('zepto')
    var avalon = require('avalon');
    var handAction = ["swipe", "swipeLeft", "swipeRight", "swipeUp", "swipeDown", "doubleTap", "tap", "singleTap", "longTap"];

    var vm = avalon.define({
        $id: 'zeptoDemo',
        init: function () {
            avalon.each(handAction, function (index, el) {
                $.fn[el].call($('#content'), function () {
                    console.log(handAction[index] + ' is calling');
                });
            });

            $('#toptips').click(function () {
                $.toptips('warn');
            });
        }
    });

    avalon.ready(function () {
        vm.init();
    });

});