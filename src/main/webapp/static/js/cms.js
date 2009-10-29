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

	getFolder: function(func, id) {
		jsonrpc.folderService.getFolder(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	getByParent: function(func, id) {
		jsonrpc.folderService.getByParent(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},
	
	saveFolder: function(func, vo) {
		jsonrpc.folderService.saveFolder(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},
	
	deleteFolder: function(func, ids) {
		jsonrpc.folderService.deleteFolder(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
	
};

var fileService = {
		
	getByFolder: function(func, id) {
		jsonrpc.fileService.getByFolder(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	deleteFiles: function(func, ids) {
		jsonrpc.fileService.deleteFiles(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
	
	updateContent: function(func, id, content) {
		jsonrpc.fileService.updateContent(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id, content);
	},
	
	getFile: function(func, id) {
		jsonrpc.fileService.getFile(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},
	
	saveFile: function(func, vo) {
		jsonrpc.fileService.saveFile(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},
};

var configService = {
		
	getConfig: function(func) {
		jsonrpc.configService.getConfig(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},

	saveConfig: function(func, vo) {
		jsonrpc.configService.saveConfig(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},
	
};

var formService = {
		
	getForm: function(func, id) {
		jsonrpc.formService.getForm(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	saveForm: function(func, vo) {
		jsonrpc.formService.saveForm(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},

	select: function(func) {
		jsonrpc.formService.select(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},
		
	deleteForm: function(func, ids) {
		jsonrpc.formService.deleteForm(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
	
	getFormConfig: function(func) {
		jsonrpc.formService.getFormConfig(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},

	saveFormConfig: function(func, vo) {
		jsonrpc.formService.saveFormConfig(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},
	
	restoreFormTemplate: function(func) {
		jsonrpc.formService.restoreFormTemplate(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},
	
	restoreFormLetter: function(func) {
		jsonrpc.formService.restoreFormLetter(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},
	
};

var fieldService = {
		
	updateField: function(func, vo) {
		jsonrpc.fieldService.updateField(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},

	getByForm: function(func, id) {
		jsonrpc.fieldService.getByForm(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},
	
	getById: function(func, id) {
		jsonrpc.fieldService.getById(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	remove: function(func, ids) {
		jsonrpc.fieldService.remove(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
};

var seoUrlService = {
		
	save: function(func, vo) {
		jsonrpc.seoUrlService.save(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, vo);
	},

	select: function(func) {
		jsonrpc.seoUrlService.select(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		});
	},
		
	getById: function(func, id) {
		jsonrpc.seoUrlService.getById(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, id);
	},

	remove: function(func, ids) {
		jsonrpc.seoUrlService.remove(function (r,e) {
			if (backServiceFailed(e)) return;
			func(r);
		}, ids);
	},
};