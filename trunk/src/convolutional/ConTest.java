package convolutional;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ConTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Convolutional cn = new Convolutional();
		try {
			cn.getNN().load("60000_9.nn");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConvolutionalTester.test(cn, 10000);
	}

}
