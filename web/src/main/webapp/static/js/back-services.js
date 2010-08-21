/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

// Vosao namespace should exists.

if (Vosao == undefined) {
	alert(messages('vosao.namespace_error'));
}

Vosao.backServiceFailed = function(e) {
	if (e != null) {
		Vosao.error(messages('vosao.cant_connect') + ' ' + e + ' ' + e.message 
			+ ' ' + e.code + ' ' + e.msg);
		return true;
	}
	return false;
};

Vosao.serviceHandler = function(serviceFunc) {
	return function() {
		var callback = arguments[0];
		var serviceFuncArgs = arguments;
		serviceFuncArgs[0] = function (r, e) {
			$('#loading').hide();
			if (Vosao.backServiceFailed(e)) return;
			callback(r);
		};
		$('#loading').show();
		serviceFunc.apply(null, serviceFuncArgs);
	}
};

Vosao.setupJSONRpcHooks = function() {
	for (var serviceName in Vosao.jsonrpc) {
		if (serviceName.indexOf('Service') != -1 
			&& serviceName.indexOf('FrontService') == -1) {
			for (var methodName in Vosao.jsonrpc[serviceName]) {
				if (typeof Vosao.jsonrpc[serviceName][methodName] == 'function') {
					var func = Vosao.jsonrpc[serviceName][methodName];
					Vosao.jsonrpc[serviceName][methodName] = Vosao.serviceHandler(func);
				}
			}
		}
	}
};

/**
 * Backend services.
 */
Vosao.initBackServices = function() {
	Vosao.setupJSONRpcHooks();
};

$(function() {
    Vosao.initJSONRpcSystem(Vosao.initBackServices);
});
