/**
 * Created by Zhang Junwei on 2016/12/3.
 */
avalon.ready(function () {
    avalon.define({
        $id:'userService',
        getAllUser:function (callback) {
            $.getJSON('../mock/users.json', function (data) {
                callback && callback(data);
            });
        }
    });

    var userCtrlVM = avalonContainer.inject('userCtrl', ['userService'], {
        users:[],
        findUsers:function () {
            userCtrlVM.$userService.getAllUser(function (data) {
                userCtrlVM.users = data;
            });
        }
    });

    avalon.scan(document.getElementById('userCtrl'), userCtrlVM);
    userCtrlVM.findUsers();
});