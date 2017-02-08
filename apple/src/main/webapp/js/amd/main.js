/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
requirejs.config({
    //By default load any module IDs from js/lib
    baseUrl: '/js/amd/lib',
    //except, if the module ID starts with "app",
    //load it from the js/app directory. paths
    //config is relative to the baseUrl, and
    //never includes a ".js" extension since
    //the paths config could be for a directory.
    paths: {
        app: '../app'
    }
});
define(['jquery','Sortable'], function ($, Sortable) {
    console.log($);
    console.log(Sortable);
    $(function () {
       $('#btn').click(function () {
            console.log('btn');
       });
    });
});