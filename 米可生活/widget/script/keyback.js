/*两次退出*/
var first = null;
function back() {
	api.addEventListener({
		name : 'keyback'
	}, function(ret, err) {
		if (!first) {
			first = new Date().getTime();
			api.toast({
				msg : '再按一次返回桌面',
				duration : 1500,
				location : 'bottom'
			});
			setTimeout(function() {
				first = null;
			}, 1000);
		} else {
			if (new Date().getTime() - first < 1000) {
				api.closeWidget({
					silent : true
				});
		api.toLauncher();
			}
		}
	});
}
