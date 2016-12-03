/**
 * Created by Zhang Junwei on 2016/12/3.
 */
/**
 * avalonContainer:avalon VM容器，提供注入VM间的依赖的功能
 * 例：vm1 注入vm2（id：'service'）的依赖后，vm1将拥有一个名为 "$service"的属性，持有vm2的引用
 */
var avalonContainer = (function () {
    var doInject = function ($vm, injections) {
        for (var i = 0; i < injections.length; i++) {
            $vm['$' + injections[i]] = avalon.vmodels[injections[i]];
        }
    };
    /**
     * 公开方法：注入依赖函数，两种调用方式
     * 1：avalonContainer.inject(id, injections)
     *    => id: avalon VM的$id值，此时id必须是已经通过avalon.define的VM的id
     *    => injections： 数组类型，元素为被注入的avalon VM的id
     * 2：avalonContainer.inject(id, injections, vmObj)
     *    => id: avalon VM的$id值
     *    => injections： 参见第1种
     *    => vmObj: vm对象的定义，风格同avalon.define的参数
     */
    var inject = function () {
        var $vm;
        if (arguments.length == 2) {
            if (($vm = avalon.vmodels[arguments[0]])) {
                doInject($vm, arguments[1]);
            }
        } else if (arguments.length == 3) {
            $vm = arguments[2];
            $vm.$id = arguments[0];
            doInject($vm, arguments[1]);
            return avalon.define($vm);
        }
    };
    return {
        inject: inject
    };
})();