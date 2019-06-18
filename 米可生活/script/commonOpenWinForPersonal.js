var commonOpenWin = function(obj, name) {
	var id = $api.attr(obj, 'data-id');
	api.openWin({
		name : name,
		url : '../chat/chat_set/' + id + '.html',
		bounces : false,
		opaque : false,
		bgColor : '#fff',
		vScrollBarEnabled : true,
		hScrollBarEnabled : false,
		animation : {
			type : 'movein',
			subType : 'from_right',
			duration : 300
		}
	});
}
