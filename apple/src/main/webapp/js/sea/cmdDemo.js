/**
 * Created by Zhang Junwei on 2017/3/3 0003.
 */

define(function (require) {
    var avalon = require('avalon');
    var $ = require('jquery');
    //var layer = require('layer');
    var layer_mobile = require('layer_mobile');
    avalon.component('ms-autocomplete', {
        template: '<div><input type="text" ms-duplex-string="@search" />' +
        '<ul><li ms-for="(idx,opt) in @aaa">' +
        '{{opt.community_name}}</li></ul></div>',
        defaults: {
            search: '',
            communities: [],
            onReady: function (e) {
                e.vmodel.$watch('search', function (v) {
                    avalon.log('current search word is ' + v)
                })
            },
            $computed: {
                aaa: {
                    get: function () {
                        var ret = [];
                        for (var i = 0; i < this.communities.length; i++) {
                            if ((this.communities[i].community_name.indexOf(this.search) > -1)) {
                                ret[ret.length] = this.communities[i];
                                if (ret.length === 5) {
                                    break
                                }
                            }
                        }
                        return ret;
                    }
                }
            }

        }
    });
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
    }, {
        community_id: 45,
        community_name: 'thank',
    }, {
        community_id: 47,
        community_name: 'you',
    }, {
        community_id: 50,
        community_name: 'verymuch',
    }, {
        community_id: 51,
        community_name: 'youre',
    }, {
        community_id: 53,
        community_name: 'welcome',
    }, {
        community_id: 54,
        community_name: 'too',
    }, {
        community_id: 55,
        community_name: 'notsogood',
    }, {
        community_id: 56,
        community_name: 'cheerful',
    }];

    var cmdDemoVM = avalon.define({
        $id:'cmdDemo',
        test1: 'test1',
        communities: communities,
        init:function () {

        },

        say:function () {
            //layer.alert('layer alert');
            layer_mobile.open({
                style: 'border:none; background-color:#78BA32; color:#fff;',
                content:'内容'
            });
        },
        styles: {
            width: 200,
            height: 200,
            borderWidth: 1,
            borderColor: "red",
            borderStyle: "solid",
            backgroundColor: "gray"
        },
        grades:[{code:1,name:'一'},{code:2,name:'二'}],
        gradeCode:''
    });
    avalon.scan($('#cmdDemo')[0], cmdDemoVM);
    cmdDemoVM.init();
    cmdDemoVM.$watch('gradeCode', function () {
        console.log(cmdDemoVM.gradeCode);
    });
});