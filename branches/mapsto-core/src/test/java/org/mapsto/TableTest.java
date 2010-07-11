package org.mapsto;

import junit.framework.TestCase;

public class TableTest extends TestCase {
	
	private MyTable table;
	
	@Override
	public void setUp() {
		table = new MyTable();
	}
	
	public void testPutGet() {
		table.clear();
		MyEntity alex = new MyEntity("alex");
		assertNull(alex.getId());
		table.save(alex);
		assertNotNull(alex.getId());
		MyEntity e = table.getById(alex.getId());
		assertNotNull(e);
		assertEquals("alex", e.getName());
	}
	
	public void testGetEntities() {
		table.clear();
		table.save(new MyEntity("alex1"));
		table.save(new MyEntity("alex2"));
		table.save(new MyEntity("alex3"));
		assertEquals(3, table.getEntites().size());
	}
	
	public void testId() {
		table.clear();
		MyEntity alex = new MyEntity("alex");
		table.save(alex);
		MyEntity roma = new MyEntity("alex");
		table.save(roma);
		assertTrue(alex.getId() != roma.getId());
	}

}
