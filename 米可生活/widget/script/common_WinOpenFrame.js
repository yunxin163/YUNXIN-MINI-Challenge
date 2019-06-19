function common_WinOpenFrame(name, url, bgColor) {

	var header = $api.dom('#aui-header');
	apiready = function() {
		api.parseTapmode();
		$api.fixIos7Bar(header);
		$api.fixStatusBar(header);
		var headerPos = $api.offset(header);
		var body_h = $api.offset($api.dom('body')).h;

		rect = {
			x : 0,
			y : headerPos.h,
			w : 'auto',
			h : api.winHeight - headerPos.h
		};
		api.openFrame({
			name : name,
			url : url,
			bounces : true,
			vScrollBarEnabled : true,
			hScrollBarEnabled : false,
			bgColor : 'rgba(255,255,255,1)'||bgColor,
			rect : rect
		});
		//监听当前窗口和frame，如果返回，直接关闭frame和win 否则，无法同时关闭
		api.addEventListener({
			name : 'keyback'
		}, function(ret, err) {
			//coding...
			api.closeWin({});
		});
	};
	//关闭窗口函数

}
