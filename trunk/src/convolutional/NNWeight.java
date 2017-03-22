package convolutional;

public class NNWeight {

	private double value;
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getDiagHessian() {
		return diagHessian;
	}

	public void setDiagHessian(double diagHessian) {
		this.diagHessian = diagHessian;
	}

	private double diagHessian;
	private String label;

	public NNWeight(double initWeight) {
		this.value = initWeight;
		this.diagHessian = 0;
		this.label = "";
	}

	public NNWeight(String label, double initWeight) {
		this.value = initWeight;
		this.diagHessian = 0;
		this.label = label;		
	}


}
