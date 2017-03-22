package convolutional;


public class Convolutional {

	private NeuralNetwork nn;
	
	public Convolutional() {
		nn = new NeuralNetwork();
	}
	
	public NeuralNetwork getNN() {
		return this.nn;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	public double[] calculate(double[] input) {
		double[] outputVector = new double[10];
		nn.Calculate(input, 841, outputVector, 10, null);
		return outputVector;
	}
	
	public void backpropagate(double[] actualOutput, double [] desiredOutput, long count,
			  double[][] pMemorizedNeuronOutputs ) {
		nn.backpropagate(actualOutput, desiredOutput, count, pMemorizedNeuronOutputs);
	}
	
	



	public void save(String file) {
		nn.save(file);
	}

}
