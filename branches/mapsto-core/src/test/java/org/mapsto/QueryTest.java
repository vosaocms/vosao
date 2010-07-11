package org.mapsto;

import junit.framework.TestCase;

public class QueryTest extends TestCase {
	
	private MyTable table;
	
	@Override
	public void setUp() {
		table = new MyTable();
	}
	
	public void testSelectOne() {
		table.clear();
		table.save(new MyEntity("alex1"));
		table.save(new MyEntity("alex2"));
		table.save(new MyEntity("alex3"));
		MyEntity e = table.newQuery().addFilter("name", 
				Filter.EQUAL, "alex").selectOne();
		assertNull(e);
		e = table.newQuery().addFilter("name", 
				Filter.EQUAL, "alex2").selectOne();
		assertNotNull(e);
		assertEquals("alex2", e.getName());
	}
	
	public void testSelect() {
		table.clear();
		table.save(new MyEntity("alex1"));
		table.save(new MyEntity("alex2"));
		table.save(new MyEntity("alex3"));
		assertEquals(3, table.newQuery().select().size());
		assertEquals(2, table.newQuery()
				.addFilter("name", Filter.MORE, "alex1").select().size());
	}

}
