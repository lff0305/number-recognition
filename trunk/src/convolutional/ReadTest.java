package convolutional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Convolutional cn = new Convolutional();
		try {
			cn.getNN().load("5000.nn");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConvolutionalTester.test(cn, 1);
	}
}
