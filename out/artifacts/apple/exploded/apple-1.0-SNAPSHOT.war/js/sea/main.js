/**
 * Created by Zhang Junwei on 2016/12/30 0030.
 */
define(function (require) {
    var jQuery = require('jquery');
    console.log(jQuery);

    var hello = require('./hello');
    jQuery('#sayHello').click(function () {
        hello.hello();
    });
});