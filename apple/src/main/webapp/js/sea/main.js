/**
 * Created by Zhang Junwei on 2016/12/30 0030.
 */
define(function (require, exports, module) {
    var hello = require('./hello');
    $('#sayHello').click(function () {
        hello.hello();
    });
});