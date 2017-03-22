package convolutional;

public class FastTanh {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double i = -1.0;
		while (i <= 1) {
			double r = Math.tanh(i);
			double k = tanh(i);
			System.out.println(k/r);
			i += 0.001;
		}

		long l0 = System.nanoTime();
		i = -1.0;
		while (i <= 1) {
			double r = Math.tanh(i);
			i += 0.001;
		}
		System.out.println(System.nanoTime() - l0);
		
		l0 = System.nanoTime();
		i = -1.0;
		while (i <= 1) {
			double r = tanh(i);
			i += 0.001;
		}
		System.out.println(System.nanoTime() - l0);
	}

	private static double tanh(double x) {
		double a = (((x*x + 378) * x*x + 17325)*x*x + 135135)*x;
		double b = ((28*x*x + 3150)*x*x + 62370)*x*x + 135135;
		return a/b;
	}

}
