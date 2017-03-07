/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
requirejs.config({
    baseUrl: '/js',
    paths: {
        lib: '/libs',
        app: '../app'
    }
});
define(['lib/jquery', 'lib/avalon1.5.8'], function ($, avalon) {
    console.log(window.avalon);
    console.log(avalon);
    $(function () {
        $('#btn').click(function () {
            console.log('btn');

        });
    });
});

