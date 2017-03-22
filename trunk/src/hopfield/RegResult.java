package hopfield;

import utility.Matrix;

public class RegResult implements Comparable<RegResult> {
	private int number;
	private int diff;
	
	public RegResult(int[] t, Matrix matrix) {
		// TODO Auto-generated constructor stub
	}
	public RegResult(int number, int diff) {
		this.number = number;
		this.diff = diff;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}

	public int compareTo(RegResult o) {
		return Integer.valueOf(diff).compareTo(Integer.valueOf(o.getDiff()));
	}
	
	public String toString() {
		return " " + number + " = " + diff;
	}
	
}
