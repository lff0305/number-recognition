package bp;

import java.util.*;

public class Node {


	private double theta = 0;
	private double value = 0;
	private double functionValue;
	private double delta;
		
	public Node(int level, int sequence) {
		
	}
	
	public void setTheta(double d) {
		this.theta  = d;
	}
	
	public double getTheta() {
		return this.theta;
	}

	public void setValue(double d) {
		this.value = d;
	}

	public double getValue() {
		return value;
	}

	public void setFunction(double sum) {
		this.functionValue = Function.sigmoid(sum);
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	public double getDelta() {
		return this.delta;
	}

	public double getFunction() {
		return this.functionValue;
	}

	public String getValueStr() {
		return String.format("%5g", value);
	}

	

}
