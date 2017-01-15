avalon.ready(function () {

    /**
     * 在这里定义只有service可以访问的私有方法
     */

    var CONSTANT = avalon.define({
        $id: 'CONSTANT',
        /**
         * 配置常量，支持两种配置方式
         * 1： 传入两个参数，分别为常量的名称和值
         * 2： 传入一个类型为数组的参数，数组元素为一个有name和value属性的对象
         */
        config: function () {
            if (arguments.length === 1) {
                if (arguments[0].length) {
                    for (var i = 0, length = arguments[0].length, arg; i < length; i++) {
                        arg = arguments[0][i];
                        if (arg.name && arg.value) {
                            this[arg.name] = arg.value;
                        }
                    }
                }
            } else if (arguments.length === 2) {
                this[arguments[0]] = arguments[1];
            }
        }
    });

    avalon.define({
        $id: 'userService',
        SEX_MALE: 1,
        SEX_FEMALE: 2,
        getAll: function (callback) {
            $.ajax({
                url: '/apple/base/user/findAll',
                type: 'get',
                dataType: 'json',
                success: function (data) {
                    callback && callback(data);
                }
            });
        },

        batchAddUsers: function (users, callback) {
            $.ajax({
                url: '/apple/base/user/batchAdd',
                type: 'post',
                dataType: 'json',
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(users),
                success: function (data) {
                    callback && callback(data);
                }
            });
        }
    });

    var pageService = avalon.define({
        $id: 'pageService',
        total: 1,
        queryPage: function (pageNo, callback) {
            if (pageNo > pageService.total || pageNo < 1) {
                return;
            }
            callback && callback();
        }
    });
});