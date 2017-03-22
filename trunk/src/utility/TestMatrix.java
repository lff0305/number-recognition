package utility;

import junit.framework.TestCase;


public class TestMatrix extends TestCase {
	
	public void testInverse() {
		Matrix m = new Matrix(new int[][]{{1, 0, 2}, {-1, 3, 1}});
		Matrix i = m.inverse();
		System.out.println(i);
		Matrix m1 = new Matrix(new int[][]{{1, 2}, {3, 4}, {5, 6}});
		Matrix i1 = m1.inverse();
		System.out.println(i1);
	}
	
	public void testMultiply2() {
		Matrix m = new Matrix(new int[][]{{1, 0, 2}, {-1, 3, 1}});
		Matrix r = m.multiply(new Matrix(new int[][]{{3,1}, {2, 1}, {1, 0}}));
		System.out.println(r);
	}
	
	public void testMultiply1() {
		Matrix m = new Matrix(2, 3);
		m.set(0, 0, 1);
		m.set(0, 1, 0);
		m.set(0, 2, 2);
		m.set(1, 0, -1);
		m.set(1, 1, 3);
		m.set(1, 2, 1);
		Matrix m1 = new Matrix(3, 2);
		m1.set(0, 0, 3);
		m1.set(0, 1, 1);
		m1.set(1, 0, 2);
		m1.set(1, 1, 1);
		m1.set(2, 0, 1);
		m1.set(2, 1, 0);
		Matrix r = m.multiply(m1);
		System.out.println(r);
	}
}
