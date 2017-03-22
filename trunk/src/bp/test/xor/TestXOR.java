package bp.test.xor;

import java.util.*;

import bp.BP;
import bp.BaseSample;
import bp.ISample;


public class TestXOR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//2 middle levels, 2 inputs, 1 output
        //learning rate is 0.6
		BP bp = new BP(new int[]{4, 4}, 2, 1, 0.6);
		bp.init();

		//Train source
		List<ISample> list = new ArrayList<ISample>();
		list.add(new XORSample(new double[]{0,0}, new double[]{0}));
		list.add(new XORSample(new double[]{0,1}, new double[]{1}));
		list.add(new XORSample(new double[]{1,0}, new double[]{1}));
		list.add(new XORSample(new double[]{1,1}, new double[]{0}));
		
		//Train it
		bp.train(list, 3200000, 0.00001);
		//After train, test and see the error
		
		double [] r ;
		for (int i=0; i<list.size(); i++) {
			r = bp.run(list.get(i));
			BaseSample.output(r, list.get(i));
		}
		
		// bp.outputNN();

	}

}
