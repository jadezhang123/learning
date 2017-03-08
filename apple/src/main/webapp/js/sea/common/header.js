/**
 * Created by Zhang Junwei on 2017/3/8 0008.
 */
define(function (require) {
    var $ = require('jquery');
    var avalon = require('avalon');
    var headerTemplate = '';
    //同步获取头部模板html
    $.ajax({
        url:'/html/public/head.html',
        async:false,
        dataType:'html',
        success:function (header) {
            headerTemplate = header;
        }
    });
    //头部组件
    avalon.component('ms-header', {
        template: headerTemplate,
        defaults: {},
    });

    //页脚组件
    avalon.component('ms-footer', {
        template: '<div class="qky_footer"><div ms-important="headerVM">{{copyRight | html}}</div></div>',
        defaults: {}
    });

    var headerVM = avalon.define({
        $id: 'headerVM',
        copyRight: "版权所有，使用须授权",
        schoolName: "全课云小学",
        agencyName: "湖北省教育局",
        schoolCode:"123",
        headImage: "",   //头像
        userName: "清风",
        isManager: function (moduleUrl) {
            return moduleUrl == 1;
        }
    });

    return headerVM;

});