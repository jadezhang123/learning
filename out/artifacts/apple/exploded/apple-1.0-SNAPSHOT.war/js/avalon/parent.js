	
avalon.ready(function(){

	var vm = avalon.define({
		$id:"parent",
		str:"",
		sex:2,
		gg:{a:'a'},
		userService:{},
		init:function () {
			vm.userService = avalon.vmodels['userService'].$model;
		},
		fork:function(){
			console.log("parent");
			vm.userService.getAll(function (data) {
				console.log(data);
			});
		},
		add:function(){
			var users = [{name:'111',sex:1},{name:'222',sex:2}];
			vm.userService.batchAddUsers(users, function(data){
				console.log(data);
			});
		}
	});
	avalon.scan($("#parent")[0], vm);
	vm.init();
});


