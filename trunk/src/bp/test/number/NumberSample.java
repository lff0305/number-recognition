package bp.test.number;

import bp.BaseSample;
import bp.ISample;

public class NumberSample implements ISample {

	private String input;
	private int number;
	private double[] inputs;
	private double[] outputs;
	
	public NumberSample(int num, String s) {
		this.number = num;
		this.input = s;
		inputs = new double[s.length()];
		outputs = new double[10];
		for (int i=0; i<=9; i++) {
			outputs[i] = 0;
		}
		outputs[num] = 1;
	}

	public double[] getInputs() {
		return inputs;
	}

	public double[] getOutputs() {
		return outputs;
	}

}
