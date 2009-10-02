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

//****************************** JSON-RPC **************************************

/**
 * Global JSON-RPC entry point.
 */
var jsonrpc = '';

/**
 * Global JSON-RPC entry point initialization.
 * @param func - optional callback to run after successful initialization.
 * @return
 */
function initJSONRpc(func) {
	if (jsonrpc != '') {
        if (func != undefined) {
            func();
        }
		return;
	}
	jsonrpc = new JSONRpcClient(function(result, e) {
        if (e) {
            errorMessage("Error dusing initializing JSON-RPC." + e);
        }
        else {
            if (func != undefined) {
                func();
            }
        }
    }, "/JSON-RPC/");
}
