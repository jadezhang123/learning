/**
 * Created by Zhang Junwei on 2017/4/1 0001.
 */
define(function (require) {
    var common = require('common');
    var avalon = require('avalon');
    var panelTemplate = common.getTemplateHtml('/html/common/classSelectorTemplate.html');
    //该组件对应的VM
    var vm = {};
    //班级选择器
    avalon.component('ms-classSelector', {
        template:panelTemplate,
        defaults:{
            visible:false,
            classCode:'',            //班级
            className:'',            //当前班级名称
            gradeCode:'',            //年级
            gradeList:[],            //年级列表

            init:function () {
                common.getGradesAndClasses(function (data) {
                    avalon.each(data, function (gi, grade) {
                        avalon.each(grade.classes, function (ci, el) {
                            el.checked = false;
                        })
                    });
                    vm.gradeList = data;
                    vm.classCode = vm.gradeList[0].classes[0].code;
                    vm.gradeList[0].classes[0].checked = true;
                    vm.gradeCode = vm.gradeList[0].code;
                    vm.className = vm.gradeList[0].classes[0].name;
                });
            },

            onReady:function (e) {
                vm = e.vmodel;
                vm.init();
            },

            //隐藏或显示班级选择器面板
            toggleClassSelector:function () {
                vm.visible = !vm.visible;
            },

            //隐藏或显示当前年级下的班级
            toggleClass:function (gradeCode) {
                vm.gradeCode = gradeCode;
            },

            //选中班级
            selectClass:function (selectedClass) {
                vm.classCode = selectedClass.code;
                vm.className = selectedClass.name;
                vm.toggleClassSelector();
                this.onSelected(selectedClass.code, selectedClass);
            },
            
            onSelected:function (classCode, selectedClass) {
                console.log('you selected '+classCode);
            }
        }
    });
});