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

Vosao.PageSearchComponent = function(div) {
	
	var h = messages('pages.search_pages') + ' : <input name="query" type="text" /> '
		+ '<input id="search" type="button" value="' + messages('search') + '"/> '
		+ '<input id="clear" type="button" value="' + messages('clear') + '"/> '
		+ '<span id="progress"></span> '
		+ '<a id="enhancedSearch" href="#">' + messages('enhanced') + '</a>'
		+ '<div id="enhancedDiv">'
		+ ' <div class="form-row">'
		+ '  <input id="publishedSearch" type="checkbox" checked="checked"/>'
		+ '   <label for="publishedSearch">' + messages('published') + '</label>'
		+ '  <input id="unpublishedSearch" type="checkbox" checked="checked" />'
		+ '   <label for="unpublishedSearch">' + messages('unpublished') + '</label><br/>'
		+ ' </div>'
		+ ' <div class="form-row">'
		+ messages('change_date') + ' <input class="datepicker" type="input" name="from" size="10"/> '
		+ messages('to') + ' <input class="datepicker" type="input" name="to" size="10"/>'
		+ ' </div>'
		+ '</div>'
		+ '<div id="pageSearchResult"></div>';
	$(div).html(h);
    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
	
	$(div + ' #search').click(onSearch); 
	$(div + ' #clear').click(function() {
		$(div + ' #pageSearchResult').html('');
		$(div + ' input[name=query]').val('');
	}); 
	$(div + ' #enhancedSearch').click(onEnhancedSearch);
	
	$(div + ' #search').hide();
	Vosao.initChannel(onChannelOpened, onChannelMessage, onChannelError, onChannelClose);
	$(div + ' input[name=query]').keypress(function(e) {
		var code = (e.keyCode ? e.keyCode : e.which);
        if (code == 13 || code == 10) {
        	onSearch();
        	e.preventDefault();
        }
	});
	return this;

	
	function onSearch() {
		$(div + ' #pageSearchResult').html('');
		$(div + ' #progress').html('<img src="/static/images/ajax-loader.gif" />');
		var params = {};
		params.query = $(div + ' input[name=query]').val();
		params.published = $(div + ' #publishedSearch:checked').size() > 0;
		params.unpublished = $(div + ' #unpublishedSearch:checked').size() > 0;
		params.fromDate = $(div + ' input[name=from]').val();
		params.toDate = $(div + ' input[name=to]').val();
		Vosao.sendChannelCommand('pageSearch', params);
	}
	
	function onEnhancedSearch() {
		$(div + ' #enhancedDiv').slideToggle();
		var linkLabel = messages('enhanced');
		if ($(div + ' #enhancedSearch').text() == messages('enhanced')) {
			linkLabel = messages('simple');
		}	
		$(div + ' #enhancedSearch').text(linkLabel);
	}
	
	function onChannelOpened() {
		$(div + ' #search').toggle();
	}
	
	function onChannelMessage(message) {
		var m = eval(message.data);
		if (m.end) {
			$(div + ' #progress').html('');
			return;
		}
		var h = '<div><a href="/cms/page/content.vm?id=' + m.id + '">' + m.title 
			+ '</a> ' + messages('version') + ': '
			+ m.version + '<br>' + m.content;
		$(div + ' #pageSearchResult').append(h);
	} 
	
	function onChannelError(error) {
		Vosao.error(error.code + ' ' + error.description);
	} 
	
	function onChannelClose() {	
	}		
}
