/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
requirejs.config({
    baseUrl: '/js/amd/lib',
    paths: {
        app: '../app'
    }
});
define(['jquery', 'Sortable'], function ($) {
    $(function () {
        $('#btn').click(function () {
            console.log('btn');

        });
    });
});

