package bp;

import java.util.*;

import bp.test.xor.XORSample;


public class BP {

	private int middleLevel = 0;
	private double beta = 0.6;
	
	
	private Map<String, Double> weight = new HashMap<String, Double>();
	private Map<String, Double> nn = new HashMap<String, Double>(); 
	private Map<String, Double> th = new HashMap<String, Double>(); 
	private List<Level> levels = null;
	
	
	public BP(int[] middleLevels, int input, int output) {
		this(middleLevels, input, output, 0.6);
	}
	
	public BP(int[] middleLevels, int input, int output, double beta) {
		this.middleLevel  = middleLevels.length;
		this.levels = new ArrayList<Level>(middleLevel + 2);
		int num = 0;
		//add input layer
		levels.add(new Level(input, num++));
		//add hider layer(s)
		for (int i=0; i<middleLevels.length; i++) {
			levels.add(new Level(middleLevels[i], num++));
		}
		//add output layer
		levels.add(new Level(output, num++));
		this.beta = beta;
	}
	
	
	/**
	 * Output the wight of the NN.
	 */
	public void outputNN() {
		System.out.println();
		Set<String> set = weight.keySet();
		Iterator<String> i = set.iterator();
		while (i.hasNext()) {
			String k = i.next();
			System.out.println(" " + k + " - " + weight.get(k));
		}
	}

	public void train(List<ISample> samples, long maxCount, double error) {
		int count = 0;
		while(true) {
			double r = 0;
			for (int i=0; i<samples.size(); i++) {
				ISample s = samples.get(i);
			//	System.out.print("Train [" + count + "] " + s);
				r += train(s);
			//	System.out.println(" error is " + r);
				count++;
			}
			
			// System.out.println("All sample trained. error is " + r);
			
			if (count >= maxCount) {
				System.out.println(" ===== " + count + " iteration finished. ==== ");
				System.out.println();
				return;
			}
			// System.out.println("Total error is " + r);
			if (r <= error) {
				System.out.println(" ===== After " + count + " iterations, E is less than " + error + "  ==== ");
				System.out.println();
				return;
			}
		}
	}

	private double train(ISample s) {
		run(s);
		
		Level output = levels.get(levels.size() - 1);
		
//		for (int i=0; i<output.getNodes().size(); i++) {
//			Node n = output.getNode(i);
//			if (i>0) {
//				System.out.println(" ");
//			}
//			System.out.print(n.getValueStr());
//		} 
		
		//Feedback!
		double r = 0;
		for (int i=0; i<output.getNodes().size(); i++) {
			Node n = output.getNode(i);
			double expect = s.getOutputs()[i];
			double delta = n.getValue() * (1 - n.getValue()) * (expect - n.getValue());
			n.setDelta(delta);
			r += (expect - n.getValue()) * (expect - n.getValue());
		}
		r = r / 2.0;
		
		for (int i = levels.size() - 2; i >= 0; i--) {
			Level level = levels.get(i);
			Level nextLevel = levels.get(i + 1);

			for (int ii=0; ii<level.getNodes().size(); ii++) {
				Node n = level.getNode(ii);
				double delta = 0;
				for (int jj=0; jj<nextLevel.getNodes().size(); jj++) {
					delta += getWeight(level.getLevelNumber(), ii, nextLevel.getLevelNumber(), jj) 
					                   * nextLevel.getNode(jj).getDelta();
				}
				n.setDelta(n.getValue() * delta * (1 - n.getValue()));
			}
		}
		
		for (int i=0; i <= levels.size() - 2; i++) {
			Level level = levels.get(i);
			Level nextLevel = levels.get(i + 1);
			for (int j=0; j<level.getNodes().size(); j++) {
				Node currentNode = level.getNode(j);
				for (int k=0; k<nextLevel.getNodes().size(); k++) {
					Node n = nextLevel.getNode(k);
					double oldWeight = getWeight(i, j, i+1, k);
					double newWeight = oldWeight + n.getDelta() * beta * currentNode.getValue() *
					                   Function.sigmoidDx(n.getValue());
					setWeight(i, j, i + 1, k, newWeight);
				}
			}
		}
		return r;
	}
	
	public void init() {
		for (int i=0; i<getLevels().size() - 1; i++) {
			Level level = getLevels().get(i);
			Level nextLevel = getLevels().get(i + 1);
			for (int ii=0; ii<level.getNodes().size(); ii++) {
				for (int jj=0; jj<nextLevel.getNodes().size(); jj++) {
					setWeight(level.getLevelNumber(), ii, nextLevel.getLevelNumber(), jj, (Math.random() * 2.0 - 1.0)/10.0);
				}
			}
		}
		
//		bp.setWeight(0, 0, 1, 0, 0.0543);
//		bp.setWeight(0, 0, 1, 0, 0.0543);
//		bp.setWeight(0, 0, 1, 1, 0.0579);
//		bp.setWeight(0, 1, 1, 0, -0.0291);
//		bp.setWeight(0, 1, 1, 1, 0.0999);	
//		bp.setWeight(1, 0, 2, 0, 0.0801);
//		bp.setWeight(1, 1, 2, 0, 0.0605);
//		
//		bp.setTheta(1, 0, 0.0801);
//		bp.setTheta(1, 1, 0.0605);
//		bp.setTheta(2, 0, -0.0109);		
		
	}

	private void setTheta(int level, int sequence, double d) {
		Node node = getNode(level, sequence);
		node.setTheta(d);
	}

	private void setWeight(int fromLevel, int fromSeq, int toLevel, int toSeq, double d) {
		String key = String.valueOf(fromLevel) + "," + String.valueOf(fromSeq) + "-" +
					 String.valueOf(toLevel) + "," + String.valueOf(toSeq);
		weight.put(key, d);
	}
	
	private double getWeight(int fromLevel, int fromSeq, int toLevel, int toSeq) {
		String key = String.valueOf(fromLevel) + "," + String.valueOf(fromSeq) + "-" +
					 String.valueOf(toLevel) + "," + String.valueOf(toSeq);
		Double d = weight.get(key);
		if (d == null) {
			return 0;
		}
		return d.doubleValue();
	}

	private Node getNode(int toLevel, int toSeq) {
		Level l = this.getLevels().get(toLevel);
		return l.getNode(toSeq);
	}

	private List<Level> getLevels() {
		return this.levels;
	}

	private Map<String, Double> getTh() {
		return this.th;
	}

	public Map<String, Double> getNN() {
		return this.nn;
	}


	public double[] run(ISample sample) {
		for (int i=0; i<levels.size(); i++) {
			if (i == 0) { //input level, only set it
				for (int j=0; j<sample.getInputs().length; j++) {
					Node n = getNode(i, j);
					n.setValue(sample.getInputs()[j]);
				}
 			} else {
 				Level fromLevel = levels.get(i - 1);
 				Level level = levels.get(i);
 				double sum = 0;
 				List<Node> fromNodesList = fromLevel.getNodes();
 				List<Node> nodesList = level.getNodes();
 				for (int ii=0; ii<nodesList.size(); ii++) {
 					sum = 0;
 					for (int jj=0; jj<fromNodesList.size(); jj++) {
 						sum += fromNodesList.get(jj).getValue() * getWeight(fromLevel.getLevelNumber(), jj, 
 								                                            level.getLevelNumber(), ii);
 					}
 					Node n = nodesList.get(ii);	
 					n.setValue(Function.sigmoid(sum));
 				}
 				
 			}
		} //end levels
		
		Level output = levels.get(levels.size() - 1);
		double[] r = new double[output.getNodes().size()];
		for (int i=0; i<r.length; i++) {
			r[i] = output.getNode(i).getValue();
		}
		return r;
	}

}
