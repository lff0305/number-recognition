package bp;

public class Function {
	
	
	
	public static double sigmoid(double x) {
		// return 1.0/(1.0 + Math.pow(Math.E, -x));
		double slope = 0.014;
		double result=(double)((2/(1+Math.exp(-1*slope*x)))-1);		//Bipolar			
		return result;
	}
	
	public static double sigmoidDx(double x) {
		// return sigmoid(x)*(1.0-sigmoid(x));
		double derivative=(double)(0.5F*(1-Math.pow(x,2)));			//Bipolar			
		return derivative;
	}
	
	public double sigmoid(double f_net, double slope)
	{						
		//float result=(float)(1/(1+Math.Exp (-1*slope*f_net)));		//Unipolar
		double result=(double)((2/(1+Math.exp(-1*slope*f_net)))-1);		//Bipolar			
		return result;
	}
	public double sigmoid_derivative(double result)
	{
		//float derivative=(float)(result*(1-result));					//Unipolar
		double derivative=(double)(0.5F*(1-Math.pow(result,2)));			//Bipolar			
		return derivative;
	}
}
