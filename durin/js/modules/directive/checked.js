/**
 * Created by Zhang Junwei on 2017/4/1 0001.
 */
define(function (require) {
    var common = require('common');
    var avalon = require('avalon');
    require('common/classSelectorPanel');
    var vm = avalon.define({
        $id: 'checked',
        
        init: function () {
            
        },

        afterSelectClass:function (classCode) {
            console.log(classCode);
        }
       
    });
    avalon.ready(function () {
        vm.init();
    });
});