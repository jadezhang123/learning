/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
requirejs.config({
    baseUrl: '/js',
    paths: {
        lib:'/libs',
        app: '../app',
        jquery:'/libs/jquery-cmd'
    }
});
define(['jquery','lib/qtips/jquery.qtip'], function ($) {
    console.log($);
    console.log($.qtip);
    $(function () {
        $('#btn').click(function () {
            console.log('btn');

        });
    });
});

