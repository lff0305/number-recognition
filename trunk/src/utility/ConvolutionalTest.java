package utility;

public class ConvolutionalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i=0; i<29; i=i+2) {
			for (int j=i; j<i+5 && i+5 <= 29; j++) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
	}

}
