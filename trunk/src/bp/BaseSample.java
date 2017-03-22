package bp;

import java.util.Arrays;

public class BaseSample implements ISample {
	private double[] inputs;
	private double[] outputs;

	public BaseSample(double[] inputs, double[] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public double[] getInputs() {
		return inputs;
	}

	public double[] getOutputs() {
		return outputs;
	}

	public String toString() {
		return " Input: " + Arrays.toString(inputs) + " Expected: " + Arrays.toString(outputs);
	}

	public static void output(double[] r, ISample iSample) {
		System.out.println(" Input: " + Arrays.toString(iSample.getInputs()));
		System.out.println(" Expected: " + Arrays.toString(iSample.getOutputs()));
		System.out.println(" Output: " + Arrays.toString(r));
		System.out.println(" --------------------------- ");
	}
}
