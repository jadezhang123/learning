/**
 * Created by Zhang Junwei on 2017/3/3 0003.
 */

define(function (require) {
    var $ = require('jquery');
    var avalon = require('avalon');
    require('./ms-autoComplete');
    var headerVM = require('header');
    console.log(headerVM.isManager(0));
    var echarts = require('echarts');
    var today = avalon.filters.date(new Date(), 'yyyy-MM-dd');
    //var layer = require('layer');
    var layer_mobile = require('layer_mobile');
    var communities = [{
        community_id: 3,
        community_name: 'This',
    }, {
        community_id: 5,
        community_name: 'isnot',
    }, {
        community_id: 8,
        community_name: 'agood',
    }, {
        community_id: 10,
        community_name: 'example',
    }, {
        community_id: 22,
        community_name: 'for',
    }, {
        community_id: 23,
        community_name: 'such',
    }, {
        community_id: 43,
        community_name: 'test',
    }];


    var cmdDemoVM = avalon.define({
        $id: 'cmdDemo',
        test1: 'test1',
        communities: communities,
        init: function () {

        },

        say: function () {
            //layer.alert('layer alert');
            //alert(JSON.stringify(communities));
            layer_mobile.open({
                style: 'border:none; background-color:#78BA32; color:#fff;',
                content: '内容'
            });
        },
        onSearchResult: function (v) {
            avalon.log('获取了' + v);
        },
        styles: {
            width: 200,
            height: 200,
            borderWidth: 1,
            borderColor: "red",
            borderStyle: "solid",
            backgroundColor: "gray"
        },
        grades: [{code: 1, name: '一'}, {code: 2, name: '二'}],
        gradeCode: ''
    });
    avalon.scan($('#cmdDemo')[0], cmdDemoVM);
    cmdDemoVM.init();
    cmdDemoVM.$watch('gradeCode', function () {
        avalon.log(cmdDemoVM.gradeCode);
    });
});