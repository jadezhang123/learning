/**
 * Created by Zhang Junwei on 2017/3/7 0007.
 */
define(function (require) {
    var avalon = require('avalon');
    avalon.component('ms-autocomplete', {
        template: '<div><input type="text" ms-duplex-string="@search" />' +
        '<ul><li ms-for="(idx,opt) in @aaa">' +
        '{{opt.community_name}}</li></ul></div>',
        defaults: {
            search: '',
            communities: [],
            onResult:function (v) {
                avalon.log('current result is ' + v);
            },
            onReady: function (e) {
                e.vmodel.$watch('search', function (v) {
                    avalon.log('current search word is ' + v)
                });
                e.vmodel.$watch('aaa', e.vmodel.onResult);
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
});