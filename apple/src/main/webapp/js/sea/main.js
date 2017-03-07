/**
 * Created by Zhang Junwei on 2016/12/30 0030.
 */
define(function (require) {
    var avalon = require('avalon');
    console.log(avalon);
    console.log(window.avalon);
    var jQuery = require('jquery');
    console.log(jQuery);


    var hello = require('sea/hello');
    jQuery('#sayHello').click(function () {
        hello.hello();
    });
    
});