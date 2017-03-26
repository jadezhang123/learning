define(function (require) {
    var $ = require('jquery');
    var avalon = require('avalon');
    var layer = require('layer_mobile');
    var common = require('common');

    common.ajaxFun({
        url: "/sys/user/getUserInfo",
        onSuccess: function (data) {
            var projectInfo = data.projectInfo;
            common.setLocalValue("account", data.account);
            common.setLocalValue("projectInfo", data.projectInfo);
            if(projectInfo&&projectInfo.name){
                window.location.href="/html/home.html";
            }else{
                //信息框
                layer.open({
                    content: '抱歉！你暂时没有该应用的访问权限',
                    btn:'我知道了',
                    end: function () {
                        history.go(-1);
                    }
                });
            }
        }
    });
});