//******************************************************************************
// This is CMS related JS code.
//******************************************************************************


// ****************************** Constants ************************************

/**
 * Autosave timeout in seconds.
 */
var AUTOSAVE_TIMEOUT = 60;

//************************** Utility functions *********************************

function infoMessage(message) {
	$("#wrapper .messages").html("<ul><li class=\"info-msg\">" + message + "</li></ul>");
}

function errorMessage(message) {
	$("#wrapper .messages").html("<ul><li class=\"error-msg\">" + message + "</li></ul>");
}
