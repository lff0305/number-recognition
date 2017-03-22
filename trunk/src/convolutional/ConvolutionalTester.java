package convolutional;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ConvolutionalTester {

	
	public static void test(Convolutional cn, int maxCount) {
		File f = new File("t10k-images.idx3-ubyte");
		if (!f.exists()) {
			System.out.println(f.getName() + " does not exist.");
			return;
		}
		
		File fl = new File("t10k-labels.idx1-ubyte");
		if (!fl.exists()) {
			System.out.println(fl.getName() + " does not exist.");
			return;
		}
		try {
			FileInputStream fr = new FileInputStream(f);
			int magic = Utility.readInt(fr);
			System.out.println("Read test magic number : 0x" + Integer.toHexString(magic));
			int count = Utility.readInt(fr);
			System.out.println("Sample test count " + count);
			final int rows = Utility.readInt(fr);
			final int cols = Utility.readInt(fr);
			System.out.println("Rows = " + rows + " Cols = " + cols);
			
			FileInputStream fls = new FileInputStream(fl);
			magic = Utility.readInt(fls);
			System.out.println("Read test label magic number : 0x" + Integer.toHexString(magic));
			count = Utility.readInt(fls);
			System.out.println("Test label count " + count);
			
			int total = 0;
			int ok = 0;
			
			for (int i=0; i<count && i<maxCount ; i++) {
//			    BufferedImage image = new BufferedImage(rows, cols,BufferedImage.TYPE_INT_RGB);
//			    Graphics2D g = image.createGraphics();
				
				// one is white, -one is black
				double[] input = new double[29 * 29];
				for (int j=0; j<29 * 29; j++) {
					input[j] = -1.0;
				}
				
				int label = fls.read() & 0xff;
				
				double[] targetOutputVector = new double[10];
				
				for (int ii=0; ii<10; ++ii )
				{
					targetOutputVector[ ii ] = -1.0;
				}
				targetOutputVector[ label ] = 1.0;
				
				//System.out.println(Arrays.toString(targetOutputVector));
				
				for (int row=0; row<rows; row++) {
					for (int col=0; col<cols; col++) {
						int grey = fr.read() & 0xff;
						//input[1 + col + 29 * (row + 1)] = (grey - 128.0) / 128.0;
						if (grey > 230) {
							input[1 + col + 29 * (row + 1)] = 1;
						}						
						
						int rgb = grey * 0x00010101;
//						g.setColor(new Color(rgb));
//						g.drawLine(col, row, col, row);
//						
					}
				}
				
//				for (i=0; i<29; i++) {
//					for (int j=0; j<29; j++) {
//						System.out.print(input[i*29 + j] + " ");
//					}
//					System.out.println();
//				}
//				ImageIO.write(image, "jpg", new File("images/test.jpg"));
//				g.dispose();
				
				double[] actualOutputVector = cn.calculate(input);
				int r = Utility.getMax(actualOutputVector);
			//	System.out.println(Arrays.toString(actualOutputVector));
				System.out.println("Test " + i + " expect = " + label + " , actual = " + r + (label == r ? " OK": " ERROR"));
				total++;
				if (label == r) {
					ok ++;
				}
			}
			
			System.out.println("Total = " + total + " OK = " + ok + " " + 100.0* ok / total);
			
			fr.close();
			fls.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
