var exec = require('cordova/exec');

function getReferrer (success, error) {
	if (!success) {
		return new Promise(function(resolve, reject) {
			getReferrer(resolve, reject);
		});
	}

    	exec(function(ref) {
    		if (ref) {
			var params = [];
			ref = decodeURIComponent(ref);
			if (ref.indexOf("&")) {
				ref.split('&').forEach(function(param) {
					var key = param.split('=')[0];
					var value = param.split('=')[1];
					params[key] = value;
				});
			} else {
				var key = ref.split('=')[0];
				var value = ref.split('=')[1];
				params[key] = value;
			}
    			success(params);
    		} else {
    			setTimeout(function() {
    				getReferrer(success, error)
    			}, 500);
    		}
    	}, error, 'InstallReferrer');
}

exports.getReferrer = getReferrer;
