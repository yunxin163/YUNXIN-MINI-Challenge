
//获取当前应用的缓存大小
function _get_Cache_Size(){
	api.getCacheSize(function( ret, err ){
		if( ret ){
			$api.byId('cleanCachein').innerHTML = (ret.size/8388608).toFixed(2) +'MB';
		}else{
			$api.byId('cleanCachein').innerHTML = '无法读取缓存大小';
		}
	});
}
//清除缓存功能
var cleanCachein = function() {
	api.clearCache({
		timeThreshold : 30
	}, function(ret, err) {
		var span = $api.dom('#cleanCachein');
		var inner = $api.byId('cleanCachein').innerHTML;
		if (inner != 0) {
			$api.removeCls(span, 'aui-badge');
			$api.removeCls(span, 'aui-badge-danger');
			$api.byId('cleanCachein').innerHTML = '';
			api.toast({
				msg : '清除成功',
				location : 'middle'
			});
		} else {
			return;
		}
	});
};

