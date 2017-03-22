package convolutional;

import java.util.ArrayList;
import java.util.List;

public class NNLayer {

	private List<NNNeuron> m_Neurons;
	private List<NNWeight> m_Weights;
	private String label;
	private NNLayer m_pPrevLayer;
	
	
	public NNLayer(String label, NNLayer pLayer) {
		m_Neurons = new ArrayList<NNNeuron>();
		m_Weights = new ArrayList<NNWeight>();
		m_pPrevLayer = pLayer;
		this.label = label;
	}

	public List<NNWeight> getWeights() {
		return m_Weights;
	}
	
	public void addNeuron(NNNeuron nnNeuron) {
		m_Neurons.add(nnNeuron);
	}

	public void addWeight(NNWeight nnWeight) {
		m_Weights.add(nnWeight);
	}

	public NNNeuron getNeuron(int i) {
		return m_Neurons.get(i);
	}

	public List<NNNeuron> getNeurons() {
		return m_Neurons;
	}

	public void calculate() {
		assert( m_pPrevLayer != null );
		
		//VectorNeurons::iterator nit;
		//VectorConnections::iterator cit;
		
		double dSum;
		
		for (int i=0; i<m_Neurons.size(); i++)// for( nit=m_Neurons.begin(); nit<m_Neurons.end(); nit++ )
		{
			NNNeuron n = m_Neurons.get(i);  // to ease the terminology
			
			// cit = n.m_Connections.begin();
			NNConnection cit = n.getConnections().get(0);
			// ASSERT( (*cit).WeightIndex < m_Weights.size() );
			
			dSum = m_Weights.get(cit.getWeightIndex()).getValue();  // weight of the first connection is the bias; neuron is ignored
			
			for (int j=1; j<n.m_Connections.size(); j++) // cit++ ; cit<n.m_Connections.end(); cit++ )
			{
				NNConnection conn = n.getConnections().get(j);
				//ASSERT( (*cit).WeightIndex < m_Weights.size() );
				// ASSERT( (*cit).NeuronIndex < m_pPrevLayer->m_Neurons.size() );
				
				dSum += ( m_Weights.get(conn.getWeightIndex()).getValue()) * 
					( m_pPrevLayer.
					getNeurons().
					get(conn.getNeuronIndex())
					.getOutput() );
			}
			
			n.setOutput(SIGMOID(dSum));
			
		}		
	}

	private double SIGMOID(double x) {
		return (1.7159*Math.tanh(0.66666667*x));
	}

	public void Backpropagate(double[] dErr_wrt_dXn, double[] dErr_wrt_dXnm1, double[] thisLayerOutput,
			double[] prevLayerOutput, double etaLearningRate) {

		{
			// nomenclature (repeated from NeuralNetwork class):
			//
			// Err is output error of the entire neural net
			// Xn is the output vector on the n-th layer
			// Xnm1 is the output vector of the previous layer
			// Wn is the vector of weights of the n-th layer
			// Yn is the activation value of the n-th layer, i.e., the weighted sum of inputs BEFORE the squashing function is applied
			// F is the squashing function: Xn = F(Yn)
			// F' is the derivative of the squashing function
			//   Conveniently, for F = tanh, then F'(Yn) = 1 - Xn^2, i.e., the derivative can be calculated from the output, without knowledge of the input

			assert( dErr_wrt_dXn.length == m_Neurons.size() );
			assert( m_pPrevLayer != null );
			assert( dErr_wrt_dXnm1.length == m_pPrevLayer.m_Neurons.size() );

			int ii, jj;
			int kk;
			int nIndex;
			double output;

			double[] dErr_wrt_dYn = new double[( m_Neurons.size() )];
			//
			//	std::vector< double > dErr_wrt_dWn( m_Weights.size(), 0.0 );  // important to initialize to zero
			//////////////////////////////////////////////////
			//
			///// DESIGN TRADEOFF: REVIEW !!
			// We would prefer (for ease of coding) to use STL vector for the array "dErr_wrt_dWn", which is the 
			// differential of the current pattern's error wrt weights in the layer.  However, for layers with
			// many weights, such as fully-connected layers, there are also many weights.  The STL vector
			// class's allocator is remarkably stupid when allocating large memory chunks, and causes a remarkable 
			// number of page faults, with a consequent slowing of the application's overall execution time.

			// To fix this, I tried using a plain-old C array, by new'ing the needed space from the heap, and 
			// delete[]'ing it at the end of the function.  However, this caused the same number of page-fault
			// errors, and did not improve performance.

			// So I tried a plain-old C array allocated on the stack (i.e., not the heap).  Of course I could not
			// write a statement like 
			//    double dErr_wrt_dWn[ m_Weights.size() ];
			// since the compiler insists upon a compile-time known constant value for the size of the array.  
			// To avoid this requirement, I used the _alloca function, to allocate memory on the stack.
			// The downside of this is excessive stack usage, and there might be stack overflow probelms.  That's why
			// this comment is labeled "REVIEW"

			double[] dErr_wrt_dWn = new double[m_Weights.size()];

			//VectorNeurons::iterator nit;
			//VectorConnections::iterator cit;


			boolean bMemorized = ( thisLayerOutput != null ) && ( prevLayerOutput != null );


			// calculate dErr_wrt_dYn = F'(Yn) * dErr_wrt_Xn

			for ( ii=0; ii<m_Neurons.size(); ++ii )
			{
				assert( ii<dErr_wrt_dYn.length);
				assert( ii<dErr_wrt_dXn.length );

				if ( bMemorized != false ) {
					output = (thisLayerOutput)[ ii ];
				} else {
					output = m_Neurons.get(ii).getOutput();
				}
				dErr_wrt_dYn[ ii ] = DSIGMOID( output ) * dErr_wrt_dXn[ ii ];
			}

			// calculate dErr_wrt_Wn = Xnm1 * dErr_wrt_Yn
			// For each neuron in this layer, go through the list of connections from the prior layer, and
			// update the differential for the corresponding weight

			ii = 0;
			for (int i=0; i<m_Neurons.size(); i++) { // nit=m_Neurons.begin(); nit<m_Neurons.end(); nit++ )
				NNNeuron n = m_Neurons.get(i);  // for simplifying the terminology
				for(int j=0; j<n.m_Connections.size(); j++ ) { // for ( cit=n.m_Connections.begin(); cit<n.m_Connections.end(); cit++ )
					NNConnection c = n.getConnections().get(j);
					kk = c.getNeuronIndex();
					if ( kk == Integer.MIN_VALUE )
					{
						output = 1.0;  // this is the bias weight
					}
					else
					{
						assert( kk<m_pPrevLayer.m_Neurons.size() );
	
						if ( bMemorized != false )
						{
							output = prevLayerOutput[kk];
						}
						else
						{
							output = m_pPrevLayer.getNeuron(kk).getOutput();
						}
					}

					////////////	ASSERT( (*cit).WeightIndex < dErr_wrt_dWn.size() );  // since after changing dErr_wrt_dWn to a C-style array, the size() function this won't work
					assert( ii< dErr_wrt_dYn.length);
					dErr_wrt_dWn[c.getWeightIndex()] += dErr_wrt_dYn[ ii ] * output;
				}

				ii++;
			}
			
			
			// calculate dErr_wrt_Xnm1 = Wn * dErr_wrt_dYn, which is needed as the input value of
			// dErr_wrt_Xn for backpropagation of the next (i.e., previous) layer
			// For each neuron in this layer

			ii = 0;
			for (int i=0; i<m_Neurons.size(); i++) { // ( nit=m_Neurons.begin(); nit<m_Neurons.end(); nit++ )
				NNNeuron n = m_Neurons.get(i);  // for simplifying the terminology
				for(int j=0; j<n.m_Connections.size(); j++ ) { // for ( cit=n.m_Connections.begin(); cit<n.m_Connections.end(); cit++ )
					NNConnection c = n.getConnections().get(j);
					kk = c.getNeuronIndex();
					if ( kk != Integer.MIN_VALUE ) {
						// we exclude ULONG_MAX, which signifies the phantom bias neuron with
						// constant output of "1", since we cannot train the bias neuron
						nIndex = kk;
	
						assert( nIndex<dErr_wrt_dXnm1.length);
						assert( ii<dErr_wrt_dYn.length );
						assert( c.getWeightIndex() < m_Weights.size() );
	
						dErr_wrt_dXnm1[ nIndex ] += dErr_wrt_dYn[ ii ] * m_Weights.get(c.getWeightIndex()).getValue();
					}
				}
				ii++;  // ii tracks the neuron iterator
			}
			

			double dMicron = 0.10;
			
			for ( jj=0; jj<m_Weights.size(); ++jj )
			{
				double divisor = m_Weights.get(jj).getDiagHessian() + dMicron ; 
				
				
				// the following code has been rendered unnecessary, since the value of the Hessian has been
				// verified when it was created, so as to ensure that it is strictly
				// zero-positve.  Thus, it is impossible for the diagHessian to be less than zero,
				// and it is impossible for the divisor to be less than dMicron
				/*
				if ( divisor < dMicron )  
				{
				// it should not be possible to reach here, since everything in the second derviative equations 
				// is strictly zero-positive, and thus "divisor" should definitely be as large as MICRON.
				
				  ASSERT( divisor >= dMicron );
				  divisor = 1.0 ;  // this will limit the size of the update to the same as the size of gloabal eta
				  }
				*/
				
				double epsilon = etaLearningRate / divisor;
				double oldValue = m_Weights.get(jj).getValue();
				double newValue = oldValue - epsilon * dErr_wrt_dWn[ jj ];
				m_Weights.get(jj).setValue(newValue);
			}
		}
		
	}

	private double DSIGMOID(double S) {
		return (0.66666667/1.7159*(1.7159+(S))*(1.7159-(S)));  // derivative of the sigmoid as a function of the sigmoid's output;
	}

	public void PeriodicWeightSanityCheck() {
		
	}

	public String toString() {
		return label;
	}
}

