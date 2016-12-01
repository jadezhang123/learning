avalon.ready(function(){
	var vm2 = avalon.define({
		$id:"son",
		sex:2,
		btnOptions:{
			btn:'sonBtn'
		},
		number:3,
		sonStr:'',
		planObj:{sex:0,sets:function(){alert('alert');}},
		array:[1,2,3,4],

		sonClick:function(){
			var array = [1,2,3];
			avalon.Array.remove(array,1);
			console.log(array);
			vm2.array.forEach(function(item){
				console.log(item);
				if (item === 3) {
					return false;
				}
			});

			avalon.each(vm2.array, function(index, item){
				console.log(item);
				if (item === 3) {
					return false;
				}
			});
		},

		addProperty:function(){
			if (typeof vm2.sonStr === 'string' && !vm2.sonStr) {
				vm2.sonStr = 'isBlank';
			}
			console.log(vm2.sonStr);
			vm2.planObj.name='name';
			vm2.planObj.func =  function(){
				alert('alert');
			};
			console.log(vm2.planObj);
			console.log(vm2.planObj.$model);

			var planObj = {};
			planObj.name='name';
			console.log(planObj);
		}
	});
});