package convolutional;

import java.io.FileInputStream;
import java.io.IOException;

public class Utility {
	
	public static int readInt(FileInputStream fr) throws IOException {
		int b0 = fr.read() & 0xff;
		int b1 = fr.read() & 0xff;
		int b2 = fr.read() & 0xff;
		int b3 = fr.read() & 0xff;
		int r = (b0 << 24) + (b1 << 16) + (b2 << 8) + b3;
		return r;
	}
	
	public static int getMax(double[] actualOutputVector) {
		double max = -1000;
		int result = -1;
		for (int i=0; i<actualOutputVector.length; i++) {
			if (actualOutputVector[i] > max) {
				max = actualOutputVector[i];
				result = i;
			}
		}
		return result;
	}
}
