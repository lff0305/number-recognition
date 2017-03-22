package bp.test.xor;

import java.util.ArrayList;
import java.util.List;

import bp.BP;
import bp.BaseSample;
import bp.ISample;

public class TestXOR3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//one middle level, 3 inputs, 1 output
		BP bp = new BP(new int[]{8, 8}, 3, 1, 6);
		bp.init();

		//Train source
		List<ISample> list = new ArrayList<ISample>();
		for (int i=0; i<=1; i++) {
			for (int j=0; j<=1; j++) {
				for (int k=0; k<=1; k++) {
					list.add(new XORSample(new double[]{i, j, k}, new double[]{i ^ j ^ k}));
				}
			}
		}

		
		//Train it
		bp.train(list, 2560000, 0.001);
		//After train, test and see the error
		
		double [] r ;
		for (int i=0; i<list.size(); i++) {
			r = bp.run(list.get(i));
			BaseSample.output(r, list.get(i));
		}

	}

}
