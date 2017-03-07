/**
 * Created by Zhang Junwei on 2017/1/23 0023.
 */
requirejs.config({
    baseUrl: '/js',
    paths:{
        jquery:'../libs/jquery',
        layer:'../libs/layer/layer',
    }
});
define(['jquery', 'layer'], function ($, layer) {
    console.log(layer);

    $(function () {
        $('#btn').click(function () {
            layer.alert('aaa');
        });
    });
});

