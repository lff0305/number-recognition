package bp.test.number;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import bp.BP;
import bp.BaseSample;
import bp.ISample;
import bp.test.xor.XORSample;

public class TestNumberReg {
	
	public static void main(String[] argu) {
		
		BufferedImage image;
		StringBuffer sb = new StringBuffer();
		try {
			image = ImageIO.read(new File("images/number/5.bmp"));
			 int[] p = image.getRGB(0,0,image.getWidth(),image.getHeight(),new int[image.getWidth()*image.getHeight()],0,image.getWidth());
		     for(int i=0;i<image.getHeight();i++) {
				boolean f = false;
			  	for(int j=0;j<image.getWidth();j++) {
			   		if (p[i*image.getWidth()+j] == -1) {
			   			f = true;
			   			break;
			   		}
			   	}
			   	if (!f) {
			   		continue;
			   	}
			   	for(int j=0;j<image.getWidth();j++) {
			   		System.out.print(p[i*image.getWidth()+j]==-1?" ":"*"+(j==image.getWidth()-1?"\n":""));
			   		sb.append(p[i*image.getWidth()+j]==-1?"0":"1");
			   	}
			   	System.out.println();
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//one middle level, 256 inputs, 10 output
		BP bp = new BP(new int[]{320}, 256, 10);
		bp.init();

		//Train source
		List<ISample> list = new ArrayList<ISample>();
		for (int i=0; i<=9; i++) {
			List<NumberSample> e = NumberData.getSample(i);
			list.addAll(e);
		}
		//Train it
		bp.train(list, 30000, 0.01);
		//After train, test and see the error
		//bp.outputNN();
		
		NumberSample e = new NumberSample(3, sb.toString());
		double[] r = bp.run(e);
		System.out.println(Arrays.toString(r));
	   
	}

}
