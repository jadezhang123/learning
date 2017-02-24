avalon.ready(function() {
	//添加公共模块的依赖
	var layerModule = COMMON.layerModule;

	var layerTesterVm = avalon.define({
		$id: "layerTester",

		openLayer: function() {
			var content = "";
			layerModule.openLayer({
                url:'msg.html',
                title:'好东西',
                data:{name:'jack', sex:1},
                success:function () {
                    avalon.scan($('#layerTester')[0], layerTesterVm);
                },
                cancel:function () {
                    console.log('cancel');
                },
                end:function () {
                    console.log('end');
                }
			});
		},
		
		changeBody:function(){
			$("body").html("<div>转换</div>");
			setTimeout(function() {
				$.get('test.html', function(data) {
					var temp = data.substring(data.indexOf("<body"), data.indexOf("</body>")+7);
					$('body').html(temp);
					avalon.scan($('#layerTester')[0], layerTesterVm);
				});
			}, 1000);
		},
		
		msg: function() {
			alert("msg");
		}
	});
	avalon.scan($('#layerTester')[0], layerTesterVm);
});