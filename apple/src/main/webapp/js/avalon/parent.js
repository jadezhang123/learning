	
avalon.ready(function(){

	var vm = avalon.define({
		$id:"parent",
		str:"",
		sex:2,
		gg:{a:'a'},
		uid:0,
		userName:'',
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
		findUsersWithArticles:function () {
			$.ajax({
				url: '/apple/base/user/findUsers',
				type: 'post',
				data:{userName:vm.userName},
				dataType: 'json',
				success: function (data) {
					console.log(data);
					if (data.length >0){
						vm.uid = data[0].id;
						console.log(vm.uid);
					}
				}
			});
		},
		findUsers:function () {
			$.ajax({
				url: '/apple/base/user/findAll',
				type: 'get',
				dataType: 'json',
				success: function (data) {
					console.log(data);
				}
			});
		},
		add:function(){
			var users = [{name:'111',sex:1},{name:'222',sex:2}];
			vm.userService.batchAddUsers(users, function(data){
				console.log(data);
			});
		},
		addArticle:function () {
			$.ajax({
				url: '/apple/base/user/addArticle',
				type: 'post',
				data:{uid:vm.uid, name: 'Article', comment: 'article of user'},
				dataType: 'json',
				success: function (data) {
					console.log(data);
				}
			});
		},

	});
	avalon.scan($("#parent")[0], vm);
	vm.init();
});


