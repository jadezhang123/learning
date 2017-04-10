seajs.config({
    base: '/js/modules',
    //开发环境设置
    debug: true,
    paths: {
        'lib': '/js/libs'
    },
    // 别名配置
    alias: {
        "zepto":"lib/zepto/zepto-cmd.min.js",
        "jquery": "lib/jquery/1.11.1/jquery-cmd.min.js",
        "avalon": "lib/avalon/2.2.4/avalon2.modern-cmd.js",
        "layer_mobile": "lib/layer_mobile/2.0/layer_mobile.min.js",
        "echarts":"lib/echarts/3.5.0/echarts-cmd.js",
        "common": "common/common.js"
    }
});