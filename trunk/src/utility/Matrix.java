package utility;

public class Matrix {
	
	int p[][];
	private int row;
	private int col;
	
	
	public Matrix multiply(int n) {
		Matrix r = new Matrix(row, col);
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				r.set(i, j, get(i, j) * n);
			}
		}
		return r;
	}
	
	public static Matrix getI(int row) {
		Matrix m = new Matrix(row, row);
		for (int i=0; i<row; i++) {
			m.set(i, i, 1);
		}
		return m;
	}
	/**
	 * Create a matrix with row * column elements.
	 */
	public Matrix(int row, int col) {
		this.row = row;
		this.col = col;
		this.p = new int[row][col];
	}
	
	public Matrix(int[][] data) {
		this.p = data;
		this.row = data.length;
		this.col = data[0].length;
	}
	
	public Matrix(int[] data) {
		this.row = 1;
		this.col = data.length;
		this.p = new int[1][data.length];
		for (int i=0; i<data.length; i++) {
			set(0, i, data[i]);
		}
	}
	
	
	public int get(int row, int col) {
		try {
			return p[row][col];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getRowCount() {
		return row;
	}
	
	public int getColCount() {
		return col;
	}
	
	public Matrix add(Matrix m) {
		if (col != m.getColCount()) {
			throw new RuntimeException("Col count of 2 matrixes must match! " + col + " " + m.getColCount());
		}
		if (row != m.getRowCount()) {
			throw new RuntimeException("Row count of 2 matrixes must match!");
		}
		
		Matrix r = new Matrix(row, col);
		for (int i=0; i<row; i++) {
			for (int j=0; j<row; j++) {
				r.set(i, j, get(i, j) + m.get(i, j));
			}
		}
		return r;
	}
	
	public Matrix multiply(Matrix m) {
		if (col != m.getRowCount()) {
			throw new RuntimeException("Row/Col count of 2 matrixes must match!");
		}
		Matrix r = new Matrix(row, m.getColCount());
		for (int i=0; i<row; i++) {
			for (int j=0; j<m.getColCount(); j++) {
				int z = 0;
				for (int k=0; k<col; k++) {
					z = z + get(i, k) * m.get(k, j);
				}
				r.set(i, j, z);
			}
		}
		return r;
	}
	
	public void set(int row, int col, int toBe) {
		p[row][col] = toBe;
	}
	
	public Matrix inverse() {
		Matrix r = new Matrix(col, row);
		for (int i=0; i<col; i++) {
			for (int j=0; j<row; j++) {
				r.set(i, j, p[j][i]);
			}
		}
		return r;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				s.append(p[i][j] + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}
}
 