/**
 * Created by Zhang Junwei on 2017/3/3 0003.
 */
// seajs 的简单配置
seajs.config({
    base: '/js',
    debug: true,
    alias: {
        "jquery": "/libs/jquery-cmd.js",
        "avalon": "/libs/avalon2.js",
        "layer": "/libs/layer/layer_all.js",
        "header":"/js/sea/common/header.js",
        "layer_mobile": "/libs/layer_mobile/layer_mobile.min.js",
        "json": "/libs/json2.js",
        "echarts":"/libs/echarts/echarts-cmd.js"
    }
});