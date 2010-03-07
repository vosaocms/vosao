package org.vosao.webdav.sysfile;

import org.vosao.business.Business;
import org.vosao.common.AbstractServiceBean;

public abstract class AbstractFileFactory extends AbstractServiceBean 
		implements FileFactory {

	public AbstractFileFactory(Business business) {
		super(business);
	}
	
}
