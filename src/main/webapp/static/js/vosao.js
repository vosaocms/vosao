//******************************************************************************
// This is for frontend and backend use.
//******************************************************************************

function javaList(array) {
	return {javaClass: 'java.util.ArrayList', list: array};
}

function javaMap(aMap) {
	return {javaClass: 'java.util.HashMap', map: aMap};
}

function urlFromTitle(title) {
    return title.toLowerCase().replace(/\W/g, '-');
}

function isImage(filename) {
    var ext = getFileExt(filename);
	return ext.toLowerCase().match(/gif|jpg|jpeg|png|ico/) != null;
}

function getFileExt(filename) {
	return filename.substring(filename.lastIndexOf('.') + 1, filename.length);
}

function formatDate(date) {
	return $.datepicker.formatDate('dd.mm.yy', date);
}

/**
 * Global JSON-RPC entry point.
 */

var jsonrpc = '';
var jsonrpcListeners = [];
var jsonrpcInitialized = false;

/**
 * Don't call this function directly.
 */
function createJSONRpc() {
	jsonrpc = new JSONRpcClient(function(result, e) {
		if (e) {
			alert("Error during initializing JSON-RPC." + e);
		}
		else {
			while (jsonrpcListeners.length > 0) {
				var func = jsonrpcListeners.pop();
				func();
			}
		}
		jsonrpcInitialized = true;
	}, '/JSON-RPC/');
}

createJSONRpc();

/**
 * Global JSON-RPC entry point initialization.
 * @param func - optional callback to run after successful initialization.
 * @return
 */
function initJSONRpc(func) {
	if (func == undefined) {
		return;
	}
	if (jsonrpcInitialized) {
        func();
    } else {
    	jsonrpcListeners.push(func);
    }
}


function serviceFailed(e) {
	if (e != null) {
		alert('Can\'t connect to server. ' + e.message);
		return true;
	}
	return false;
}

/**
 * Frontend services.
 */
var loginService = {
	login: function(func, email, password) {
		jsonrpc.loginFrontService.login(function (r,e) {
			if (serviceFailed(e)) return;
			func(r);
		}, email, password);
    }
};










