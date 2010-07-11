package org.mapsto;

public interface Filter<T> {

	public static final int EQUAL = 1;
	public static final int MORE = 2;
	public static final int LESS = 3;
	public static final int MORE_EQUAL = 4;
	public static final int LESS_EQUAL = 5;

	boolean check(T entity);
	
}
