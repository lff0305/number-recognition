package convolutional;

public class NNConnection {

	private long weightIndex;
	private long neuronIndex;
	
	public int getWeightIndex() {
		return (int)weightIndex;
	}
	public void setWeightIndex(long weightIndex) {
		this.weightIndex = weightIndex;
	}
	
	public NNConnection(long neuronIndex, long weightIndex) {
		super();
		this.weightIndex = weightIndex;
		this.neuronIndex = neuronIndex;
	}
	
	public int getNeuronIndex() {
		return (int)neuronIndex;
	}
	public void setNeuronIndex(int neuronIndex) {
		this.neuronIndex = neuronIndex;
	}
	

}
