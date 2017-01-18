/**
 * Created by Zhang Junwei on 2017/1/16 0016.
 * checkbox美化组件：
 1、约定那些包含qky-checkbox自定义属性的checkbox元素需要美化，因此在组件初始化时首先需要找到这些元素
 2、把checkbox的样式display属性设置为none
 3、在checkbox后面增加一个相同大小的a标签，并设置a标签的样式，使a标签看起来像一个美化过的checkbox
 4、给a标签绑定onclick事件，事件功能包含：1）实现自身样式的切换（选中和不选中）；2）当a标签为选中样式时，同步checkbox为选中状态，反之checkbox为非选中状态
 5、给checkbox绑定onchange事件，实现checkbox和a标签的状态同步
 6、组件初始化时，根据checkbox状态渲染a标签相应的样式状态

 */
;(function ($) {
    var $qky_Checkbox = $('input[qky-checkbox][type=checkbox]');
    $qky_Checkbox.each(function (checkbox) {
        $(checkbox).css('display','none').append('');

    });
})(jQuery);
