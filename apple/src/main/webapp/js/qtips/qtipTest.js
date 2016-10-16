$(function(){
	$("#input1").qtip({
		id:"input1Tip",
		content: {
            text: $(".hiddenTip")
        },
         /*overwrite: false,
         hide: {
         	//target:"#target",
         	event:"click"
         },
         hide:false,*/
         show: {
         	event:"click",
         	target:$('h1')
         },
         hide:{
         	event:"click",
         	target:$('h2')
         },
        style: {
        	classes: 'qtip-blue qtip-rounded'
    	},
        position: {
           my:"top left",
           at:"bottom right"
        }
	});
});