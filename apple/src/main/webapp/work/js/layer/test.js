var layerTesterVm = null;
avalon.ready(function() {
	layerTesterVm = avalon.define({
		$id: "layerTester",

		openLayer: function() {
			var content = "";
			$.get("msg.html", function(result) {
				content = result;
				layer.open({
					type: 1,
					title: "弹出层",
					area: '100px',
					content: content,
					success: function() {
						avalon.scan($('#layerTester')[0], layerTesterVm);
					}
				});
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