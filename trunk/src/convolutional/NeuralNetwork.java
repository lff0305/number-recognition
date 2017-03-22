package convolutional;

import java.io.*;
import java.util.*;

public class NeuralNetwork {

	List<NNLayer> m_Layers;
	private double m_etaLearningRate;
	private int m_cBackprops;
	
	
	public NeuralNetwork() {
		m_Layers = new ArrayList<NNLayer>();
		m_etaLearningRate = .001;  // arbitrary, so that brand-new NNs can be serialized with a non-ridiculous number
		m_cBackprops = 0;
		init();
		
	}
	
	public void addLayer(NNLayer pLayer) {
		m_Layers.add(pLayer);
		
	}
	
	void Calculate(double[] inputVector, long iCount, 
			  double[] outputVector /* =NULL */, long oCount /* =0 */,
			  List<List<Double>> pNeuronOutputs /* =NULL */ )
	{
		Iterator<NNLayer> lit = m_Layers.iterator();
//		VectorNeurons::iterator nit;

		// first layer is imput layer: directly set outputs of all of its neurons
		// to the input vector

		NNLayer l = lit.next();
		List<NNNeuron> neurons = l.getNeurons();
		int count = 0;

		//ASSERT( iCount == (*lit)->m_Neurons.size() );  // there should be exactly one neuron per input

		for(int i=0; i<neurons.size() && ( count < iCount ); i++ )
		{
			NNNeuron n = neurons.get(i);
			n.setOutput(inputVector[count]);
			count++;
		}
		
		for (int i=1; i<m_Layers.size(); i++) {
			NNLayer ll = m_Layers.get(i);
			ll.calculate();
		}

		// 	load up output vector with results

		if ( outputVector != null)
		{
			NNLayer outputLayer = m_Layers.get(m_Layers.size() - 1);
			
			for ( int ii=0; ii<oCount; ++ii )
			{
				outputVector[ ii ] = outputLayer.getNeuron(ii).getOutput();
			}
		}
		
		// 	load up neuron output values with results
		if ( pNeuronOutputs != null )
		{
			// 	check for first time use (re-use is expected)
			if (!pNeuronOutputs.isEmpty())
			{
				// 	it's empty, so allocate memory for its use
				pNeuronOutputs.clear(); // for safekeeping

				int ii = 0;
				for(int i=0; i<m_Layers.size(); i++ ) //lit=m_Layers.begin(); lit<m_Layers.end(); lit++ )
				{
					NNLayer lit1 = m_Layers.get(i);
					List<Double> layerOut = new ArrayList<Double>();

					for ( ii=0; ii<lit1.getNeurons().size(); ++ii )
					{
						layerOut.add(lit1.getNeuron(ii).getOutput());
					}
					pNeuronOutputs.add( layerOut);
				}
			}	
			else
			{
				// 	it's not empty, so assume it's been used in a past iteration and memory for
				//	it has already been allocated internally.  Simply store the values
				int ii, jj = 0;
				for(int i=0; i<m_Layers.size(); i++ )  // for( lit=m_Layers.begin(); lit<m_Layers.end(); lit++ )
				{
					NNLayer lit1 = m_Layers.get(i);
					for ( ii=0; ii<lit1.getNeurons().size(); ++ii ) // for ( ii=0; ii<(*lit)->m_Neurons.size(); ++ii )
					{
						List<Double> ll = pNeuronOutputs.get(jj);
						ll.set(ii, lit1.getNeuron(ii).getOutput());
						// (*pNeuronOutputs)[ jj ][ ii ] = (*lit)->m_Neurons[ ii ]->output ;
					}
					jj++;
				}
			}
		}
	}
	
	
	void backpropagate(double[] actualOutput, double [] desiredOutput, long count,
			  double[][] pMemorizedNeuronOutputs )
	{
		// backpropagates through the neural net
		assert( ( actualOutput != null ) && ( desiredOutput != null ) && ( count < 256 ) );
		assert( m_Layers.size() >= 2 );  // there must be at least two layers in the net

		if ( ( actualOutput == null ) || ( desiredOutput == null ) || ( count >= 256 ) ) {
			return;
		}


		// check if it's time for a weight sanity check
		m_cBackprops++;
		if ( (m_cBackprops % 10000) == 0 )
		{
			// every 10000 backprops
			PeriodicWeightSanityCheck();
		}


		// proceed from the last layer to the first, iteratively
		// We calculate the last layer separately, and first, since it provides the needed derviative
		// (i.e., dErr_wrt_dXnm1) for the previous layers

		// nomenclature:
		//
		// Err is output error of the entire neural net
		// Xn is the output vector on the n-th layer
		// Xnm1 is the output vector of the previous layer
		// Wn is the vector of weights of the n-th layer
		// Yn is the activation value of the n-th layer, i.e., the weighted sum of inputs BEFORE the squashing function is applied
		// F is the squashing function: Xn = F(Yn)
		// F' is the derivative of the squashing function
		//   Conveniently, for F = tanh, then F'(Yn) = 1 - Xn^2, i.e., the derivative can be calculated from the output, without knowledge of the input


		//VectorLayers::iterator lit = m_Layers.end() - 1;
		NNLayer outputLayer = m_Layers.get(m_Layers.size() - 1);

		double[] dErr_wrt_dXlast = new double[outputLayer.getNeurons().size()]; // ( (*lit)->m_Neurons.size() );
		double[][] differentials;

		int iSize = m_Layers.size();

		differentials = new double[iSize][];

		int ii;

		// start the process by calculating dErr_wrt_dXn for the last layer.
		// for the standard MSE Err function (i.e., 0.5*sumof( (actual-target)^2 ), this differential is simply
		// the difference between the target and the actual

		for ( ii=0; ii<outputLayer.getNeurons().size(); ++ii )
		{
			dErr_wrt_dXlast[ ii ] = actualOutput[ ii ] - desiredOutput[ ii ];
		}


		// store Xlast and reserve memory for the remaining vectors stored in differentials

		differentials[iSize - 1] = dErr_wrt_dXlast;  // last one

		for ( ii=0; ii<iSize-1; ++ii )
		{
			differentials[ ii ] = new double[m_Layers.get(ii).getNeurons().size()];
		}

		// now iterate through all layers including the last but excluding the first, and ask each of
		// them to backpropagate error and adjust their weights, and to return the differential
		// dErr_wrt_dXnm1 for use as the input value of dErr_wrt_dXn for the next iterated layer

		boolean bMemorized = ( pMemorizedNeuronOutputs != null );

		//lit = m_Layers.end() - 1;  // re-initialized to last layer for clarity, although it should already be this value

		ii = iSize - 1;
		for (int i=m_Layers.size() -1; i>0; i--) { // for ( lit; lit>m_Layers.begin(); lit--)
			NNLayer l = m_Layers.get(i);
			if ( bMemorized != false )
			{
				l.Backpropagate(differentials[ii], differentials[ ii - 1 ], pMemorizedNeuronOutputs[ ii ], pMemorizedNeuronOutputs[ ii - 1 ], m_etaLearningRate );
				//(*lit)->Backpropagate( differentials[ ii ], differentials[ ii - 1 ], 
				//		&(*pMemorizedNeuronOutputs)[ ii ], &(*pMemorizedNeuronOutputs)[ ii - 1 ], m_etaLearningRate );
			}
			else
			{
				l.Backpropagate( differentials[ ii ], differentials[ ii - 1 ], null, null, m_etaLearningRate );
			}
			--ii;
		}
		
		differentials = null;
	}


	private void PeriodicWeightSanityCheck() {
		
		for (int i=0; i<m_Layers.size(); i++) {
			NNLayer l = m_Layers.get(i);
			l.PeriodicWeightSanityCheck();
		}
	}
	
	public void save(String file) {
		File f = new File(file);
		
		int[] length = new int[]{156, 7800, 125100, 1010};

		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			DataOutputStream dos = new DataOutputStream(fos);
			
			for (int i=0; i<4; i++) {
				NNLayer layer = m_Layers.get(i+1);
				assert layer.getWeights().size() == length[i];
				for (int j=0; j<layer.getWeights().size(); j++) {
					NNWeight w = layer.getWeights().get(j);
					dos.writeDouble(w.getValue());
				}
			}
			dos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void init() {
	    
	    int ii, jj, kk;
	    double initWeight;
	    
	    int icNeurons = 0;
		int icWeights = 0;
		
	    // layer zero, the input layer.
	    // Create neurons: exactly the same number of neurons as the input
	    // vector of 29x29=841 pixels, and no weights/connections
	    
	    NNLayer pLayer = new NNLayer("Layer00", null);
	    addLayer( pLayer );
	    
	    for ( ii=0; ii<841; ++ii )  {
			String label = String.format("Layer00_Neuron%04d_Num%06d", ii, icNeurons );
			pLayer.addNeuron(new NNNeuron(label));
			icNeurons++;
	    }

	    
	    // layer one:
	    // This layer is a convolutional layer that
	    // has 6 feature maps.  Each feature 
	    // map is 13x13, and each unit in the
	    // feature maps is a 5x5 convolutional kernel
	    // of the input layer.
	    // So, there are 13x13x6 = 1014 neurons, (5x5+1)x6 = 156 weights

	    
	    pLayer = new NNLayer("Layer01", pLayer);
	    addLayer( pLayer );
	    
	    for ( ii=0; ii<1014; ++ii )
	    {
			String label = String.format("Layer01_Neuron%04d_Num%06d", ii, icNeurons );
			pLayer.addNeuron(new NNNeuron(label));
			icNeurons++;
	    }
	    
	    for ( ii=0; ii<156; ++ii )
	    {
	        initWeight = 0.05 * getRandom();
	        // uniform random distribution
	        pLayer.addWeight(new NNWeight( initWeight ) );
	    }
	    
	    // interconnections with previous layer: this is difficult
	    // The previous layer is a top-down bitmap
	    // image that has been padded to size 29x29
	    // Each neuron in this layer is connected
	    // to a 5x5 kernel in its feature map, which 
	    // is also a top-down bitmap of size 13x13. 
	    // We move the kernel by TWO pixels, i.e., we
	    // skip every other pixel in the input image

	    
	    int kernelTemplate[] = new int[]{
	        0,  1,  2,  3,  4,
	        29, 30, 31, 32, 33,
	        58, 59, 60, 61, 62,
	        87, 88, 89, 90, 91,
	        116,117,118,119,120 };
	        
	    int iNumWeight;
	    int fm;  // "fm" stands for "feature map"
	        
	    for ( fm=0; fm<6; ++fm)  {
	        for ( ii=0; ii<13; ++ii ) {
	            for ( jj=0; jj<13; ++jj ) {
	                // 26 is the number of weights per feature map
	                iNumWeight = fm * 26;
	                NNNeuron n = pLayer.getNeuron(jj + ii*13 + fm*169);
	                n.AddConnection( Integer.MIN_VALUE, iNumWeight++ );  // bias weight
	                for ( kk=0; kk<25; ++kk )
	                {
	                    // note: max val of index == 840, 
	                    // corresponding to 841 neurons in prev layer
	                    n.AddConnection( 2*jj + 58*ii + 
	                    kernelTemplate[kk], iNumWeight++ );
	                }
	            }
	        }
	    }
	    
	    
	    // layer two:
	    // This layer is a convolutional layer
	    // that has 50 feature maps.  Each feature 
	    // map is 5x5, and each unit in the feature
	    // maps is a 5x5 convolutional kernel
	    // of corresponding areas of all 6 of the
	    // previous layers, each of which is a 13x13 feature map
	    // So, there are 5x5x50 = 1250 neurons, (5x5+1)x6x50 = 7800 weights

	    
	    pLayer = new NNLayer( "Layer02", pLayer );
	    addLayer(pLayer);
	    
	    for (ii=0; ii<1250; ++ii )
	    {
			String label = String.format("Layer02_Neuron%04d_Num%06d", ii, icNeurons );
			pLayer.addNeuron(new NNNeuron(label));
			icNeurons++;
	    }
	    
	    for ( ii=0; ii<7800; ++ii )
	    {
	        initWeight = getRandom();
	        pLayer.addWeight(new NNWeight(initWeight ));
	    }
	    
	    // Interconnections with previous layer: this is difficult
	    // Each feature map in the previous layer
	    // is a top-down bitmap image whose size
	    // is 13x13, and there are 6 such feature maps.
	    // Each neuron in one 5x5 feature map of this 
	    // layer is connected to a 5x5 kernel
	    // positioned correspondingly in all 6 parent
	    // feature maps, and there are individual
	    // weights for the six different 5x5 kernels.  As
	    // before, we move the kernel by TWO pixels, i.e., we
	    // skip every other pixel in the input image.
	    // The result is 50 different 5x5 top-down bitmap
	    // feature maps

	    int kernelTemplate2[] = {
	        0,  1,  2,  3,  4,
	        13, 14, 15, 16, 17, 
	        26, 27, 28, 29, 30,
	        39, 40, 41, 42, 43, 
	        52, 53, 54, 55, 56   };
	        
	        
	    for ( fm=0; fm<50; ++fm)
	    {
	        for ( ii=0; ii<5; ++ii )
	        {
	            for ( jj=0; jj<5; ++jj )
	            {
	                // 26 is the number of weights per feature map
	                iNumWeight = fm * 26;
	                NNNeuron n = (pLayer.getNeuron(jj + ii*5 + fm*25));
	                n.AddConnection( Integer.MIN_VALUE, iNumWeight++ );  // bias weight

	                
	                for ( kk=0; kk<25; ++kk )
	                {
	                    // note: max val of index == 1013,
	                    // corresponding to 1014 neurons in prev layer
	                    n.AddConnection(       2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                    n.AddConnection( 169 + 2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                    n.AddConnection( 338 + 2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                    n.AddConnection( 507 + 2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                    n.AddConnection( 676 + 2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                    n.AddConnection( 845 + 2*jj + 26*ii + 
	                     kernelTemplate2[kk], iNumWeight++ );
	                }
	            }
	        }
	    }
	            
	    
	    // layer three:
	    // This layer is a fully-connected layer
	    // with 100 units.  Since it is fully-connected,
	    // each of the 100 neurons in the
	    // layer is connected to all 1250 neurons in
	    // the previous layer.
	    // So, there are 100 neurons and 100*(1250+1)=125100 weights

	    
	    pLayer = new NNLayer("Layer03", pLayer );
	    addLayer(pLayer);
	    
		for ( ii=0; ii<100; ++ii )
		{
			String label = String.format("Layer03_Neuron%04d_Num%06d", ii, icNeurons );
			pLayer.addNeuron(new NNNeuron(label) );
			icNeurons++;
		}
		
		for ( ii=0; ii<125100; ++ii )
		{
			String label = String.format("Layer03_Weight%04d_Num%06d", ii, icWeights );
			initWeight = 0.05 * getRandom();
			pLayer.addWeight(new NNWeight(label, initWeight));
		}
		
	    
	    // Interconnections with previous layer: fully-connected

	    iNumWeight = 0;  // weights are not shared in this layer
	    for ( fm=0; fm<100; ++fm )
	    {
	        NNNeuron n = pLayer.getNeuron(fm);
	        n.AddConnection(Integer.MIN_VALUE, iNumWeight++ );  // bias weight
	        for ( ii=0; ii<1250; ++ii )
	        {
	            n.AddConnection( ii, iNumWeight++ );
	        }
	    }
	    
	    // layer four, the final (output) layer:
	    // This layer is a fully-connected layer
	    // with 10 units.  Since it is fully-connected,
	    // each of the 10 neurons in the layer
	    // is connected to all 100 neurons in
	    // the previous layer.
	    // So, there are 10 neurons and 10*(100+1)=1010 weights

	    pLayer = new NNLayer("Layer04", pLayer );
	    addLayer( pLayer );
	    
	    for ( ii=0; ii<10; ++ii )
	    {
			String label = String.format("Layer04_Neuron%04d_Num%06d", ii, icNeurons );
			pLayer.addNeuron(new NNNeuron(label) );
			icNeurons++;
	    }
	    
	    for ( ii=0; ii<1010; ++ii )
	    {
			String label = String.format("Layer04_Weight%04d_Num%06d", ii, icWeights );
	        initWeight = 0.05 * getRandom();
			pLayer.addWeight(new NNWeight(label, initWeight ));	        
	    }
	    
	    // Interconnections with previous layer: fully-connected
	    iNumWeight = 0;  // weights are not shared in this layer

	    
	    for ( fm=0; fm<10; ++fm )
	    {
	        NNNeuron n = pLayer.getNeuron(fm);
	        n.AddConnection( Integer.MIN_VALUE, iNumWeight++ );  // bias weight
	        
	        for ( ii=0; ii<100; ++ii )
	        {
	            n.AddConnection( ii, iNumWeight++ );
	        }
	    }		
	}

	private double getRandom() {
		return (double)(2.0 * Math.random()) - 1.0;
	}
	
	public void load(String file) throws FileNotFoundException, IOException {
		File f = new File(file);
		
		int[] length = new int[]{156, 7800, 125100, 1010};

		
		try {
			FileInputStream fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);
			
			for (int i=0; i<4; i++) {
				NNLayer layer = m_Layers.get(i+1);
				assert layer.getWeights().size() == length[i];
				for (int j=0; j<layer.getWeights().size(); j++) {
					NNWeight w = layer.getWeights().get(j);
					double d = dis.readDouble();
					w.setValue(d);
				}
			}
			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}	
	}
}