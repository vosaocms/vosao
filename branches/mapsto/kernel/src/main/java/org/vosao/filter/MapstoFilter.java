/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.filter;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.mapsto.Table;
import org.vosao.business.impl.BusinessImpl;
import org.vosao.business.impl.mq.MessageQueueImpl;
import org.vosao.common.VosaoContext;
import org.vosao.service.impl.BackServiceImpl;
import org.vosao.service.impl.FrontServiceImpl;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * Vosao context creation and request injection.
 * 
 * @author Alexander Oleynik
 *
 */
public class MapstoFilter extends AbstractFilter implements Filter {

    public MapstoFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	loadTables();
    	chain.doFilter(request, response);
    	saveTables();
    }

	private void saveTables() {
		for (Table table : getBusiness().getDao().getMapsto().getTables()
				.values()) {
			if (!table.getTransactionLog().isEmpty()) {
				table.getTransactionLog().clear();
				byte[] data = StreamUtil.toBytes(table.select());
				Entity entity = getDataEntity(table.getName());
				if (entity == null) {
					entity = new Entity("mapsto");
				}
				entity.setProperty("name", table.getName());
				entity.setProperty("data", new Blob(data));
				getSystemService().getDatastore().put(entity);
				getSystemService().getCache().put(getTableDataKey(
						table.getName()), data);
			}
		}
	}

	private void loadTables() {
		getBusiness().getDao().getMapsto().clear();
		for (String name : getBusiness().getDao().getMapsto().getTables()
				.keySet()) {
			byte[] data = getTableData(name);
			if (data != null) {
				logger.info("table " + name + " " + data.length);
				getBusiness().getDao().getMapsto().getTable(name).put(
					(Collection)StreamUtil.toObject(data));
			}
		}
		getBusiness().getDao().getMapsto().clear();
	}
	
	private static String getTableDataKey(String name) {
		return "mapsto:" + name;
	}
	
	private byte[] getTableData(String name) {
		byte[] result = (byte[])getBusiness().getSystemService().getCache().get(
				getTableDataKey(name));
		if (result == null) {
			result = loadTableData(name);
		}
		return result;
	}

	private byte[] loadTableData(String name) {
		Entity entity = getDataEntity(name);
		if (entity == null) {
			return null;
		}
		return ((Blob)entity.getProperty("data")).getBytes();
	}
	
	private Entity getDataEntity(String name) {
		Query query = new Query("mapsto");
		query.addFilter("name", FilterOperator.EQUAL, name);
		PreparedQuery p = getSystemService().getDatastore().prepare(query);
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		List<Entity> list = p.asList(withLimit(limit));
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
}
