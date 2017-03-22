package utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import convolutional.*;


public class SamplesReader {
	public static void main(String[] argu) {
		// train(1, 1000);
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
		for (int i=0; i<10; i++) {
			train(cn, 60000, 1000, i);
		}
	}
	
	public static void train(Convolutional cn, int trainCount, int testCount, int epch) {
		File f = new File("train-images.idx3-ubyte");
		if (!f.exists()) {
			System.out.println(f.getName() + " does not exist.");
			return;
		}
		
		File fl = new File("train-labels.idx1-ubyte");
		if (!fl.exists()) {
			System.out.println(fl.getName() + " does not exist.");
			return;
		}
		
		
		try {
			FileInputStream fr = new FileInputStream(f);
			int magic = Utility.readInt(fr);
			System.out.println("Read magic number : 0x" + Integer.toHexString(magic));
			int count = Utility.readInt(fr);
			System.out.println("Sample count " + count);
			final int rows = Utility.readInt(fr);
			final int cols = Utility.readInt(fr);
			System.out.println("Rows = " + rows + " Cols = " + cols);
			
			FileInputStream fls = new FileInputStream(fl);
			magic = Utility.readInt(fls);
			System.out.println("Read magic number : 0x" + Integer.toHexString(magic));
			count = Utility.readInt(fls);
			System.out.println("Sample count " + count);
			
			double dTotalMSE = 0;

			double[] input = new double[29 * 29];
			double[] targetOutputVector = new double[10];
			
			for (int i=0; i<trainCount && i<count; i++) {
//			    BufferedImage image = new BufferedImage(rows, cols,BufferedImage.TYPE_INT_RGB);
//			    Graphics2D g = image.createGraphics();
				
				// one is white, -one is black
				for (int j=0; j< 29 * 29; j++) {
					input[j] = -1.0;
				}
				
				int label = fls.read() & 0xff;

				for (int ii=0; ii<10; ++ii )
				{
					targetOutputVector[ ii ] = -1.0;
				}
				targetOutputVector[ label ] = 1.0;
				
				// System.out.println(Arrays.toString(targetOutputVector));
				
				for (int row=0; row<rows; row++) {
					for (int col=0; col<cols; col++) {
						int grey = fr.read() & 0xff;
//						System.out.print(grey + " ");
						if (grey > 230) {
							input[1 + col + 29 * (row + 1)] = 1;
						}
						int rgb = grey * 0x00010101;
//						g.setColor(new Color(rgb));
//						g.drawLine(col, row, col, row);
					}
				}
				

				
				double[][] memorizedNeuronOutputs = null;
				
				double dPatternMSE = 0.0;
			//	do {
					dPatternMSE = 0.0;
					double[] actualOutputVector = cn.calculate(input);
					cn.backpropagate(actualOutputVector, targetOutputVector, 10, memorizedNeuronOutputs);
					for (int ii=0; ii<10; ++ii ) {
						dPatternMSE += ( actualOutputVector[ii]-targetOutputVector[ii] ) * ( actualOutputVector[ii]-targetOutputVector[ii] );
					}
					dPatternMSE /= 2.0;
			//	} while (dPatternMSE > 0.1); 
				
				dTotalMSE += dPatternMSE;
			
				System.out.println("Pattern " + i + ", mse = " + dPatternMSE);
//				ImageIO.write(image, "jpg", new File("images/" + i + ".jpg"));
//				g.dispose();
			}
			
			cn.save(trainCount + "_d" + epch + ".nn");
			
			ConvolutionalTester.test(cn, testCount);
			
			fls.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
