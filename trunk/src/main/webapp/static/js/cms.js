function infoMessage(message) {
	$("#wrapper .messages").html("<ul><li class=\"info-msg\">" + message + "</li></ul>");
}

function errorMessage(message) {
	$("#wrapper .messages").html("<ul><li class=\"error-msg\">" + message + "</li></ul>");
}

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