//******************************************************************************
// This is CMS related JS code.
//******************************************************************************


// ****************************** Constants ************************************

/**
 * Autosave timeout in seconds.
 */
var AUTOSAVE_TIMEOUT = 60;

//************************** Utility functions *********************************

function info(message) {
	$("#wrapper .messages").html("<ul><li class=\"info-msg\">" + message 
			+ "</li></ul>");
}

function addInfo(message) {
	$("#wrapper .messages ul").append("<li class=\"info-msg\">" + message 
			+ "</li>");
}

function error(message) {
	$("#wrapper .messages").html("<ul><li class=\"error-msg\">" + message 
			+ "</li></ul>");
}

function addError(message) {
	$("#wrapper .messages ul").append("<li class=\"error-msg\">" + message 
			+ "</li>");
}

function backServiceFailed(e) {
	if (e != null) {
		error('Can\'t connect to server. ' + e.message);
		return true;
	}
	return false;
}

function showServiceMessages(r) {
	if (r.result == 'success') {
		info(r.message);
		if (r.messages.list.length > 0) {
			$.each(r.messages.list, function(n,value) { addInfo(value) });
		}
	}
	else {
		error(r.message);
		if (r.messages.list.length > 0) {
			$.each(r.messages.list, function(n,value) { addError(value) });
		}
	}
}

/**
 * Backend services.
 */

var pageService = {
	
	getTree: function(func) {
		jsonrpc.pageService.getTree(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
    },
    
	getPage: function(func, id) {
		jsonrpc.pageService.getPage(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
    },
    
	savePage: function(func, page) {
		jsonrpc.pageService.savePage(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, page);
    },

    updateContent: function(func, pageId, content) {
		jsonrpc.pageService.updateContent(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, pageId, content);
    },

    getChildren: function(func, id) {
		jsonrpc.pageService.getChildren(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
    },
    
    deletePages: function(func, ids) {
		jsonrpc.pageService.deletePages(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
    },
};

var templateService = {
		
	getTemplates: function(func) {
		jsonrpc.templateService.getTemplates(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
    },

    deleteTemplates: function(func, ids) {
		jsonrpc.templateService.deleteTemplates(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
    },
    
    getTemplate: function(func, id) {
		jsonrpc.templateService.getTemplate(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
    },
    
    saveTemplate: function(func, vo) {
		jsonrpc.templateService.saveTemplate(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
    },

    updateContent: function(func, templateId, content) {
		jsonrpc.templateService.updateContent(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, templateId, content);
    },
    
};

var commentService = {
		
	getByPage: function(func, id) {
		jsonrpc.commentService.getByPage(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	enableComments: function(func, ids) {
		jsonrpc.commentService.enableComments(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
	
	disableComments: function(func, ids) {
		jsonrpc.commentService.disableComments(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
	
	deleteComments: function(func, ids) {
		jsonrpc.commentService.deleteComments(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
};

var folderService = {
		
	getTree: function(func) {
		jsonrpc.folderService.getTree(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},

	getFolderPath: function(func, id) {
		jsonrpc.folderService.getFolderPath(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},
	
};

var fileService = {
		
	getByFolder: function(func, id) {
		jsonrpc.fileService.getByFolder(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

};
