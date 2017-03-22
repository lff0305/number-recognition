package convolutional;

import java.util.*;

public class NNNeuron {

	List<NNConnection> m_Connections;
	private double output;
	private String label;
	
	public NNNeuron(String label) {
		this.label = label;
		this.m_Connections = new ArrayList<NNConnection>();
	}

	public void AddConnection(long iNeuron, int iWeight) {
		m_Connections.add(new NNConnection( iNeuron, iWeight ) );
	}
	
	
	public void AddConnection(NNConnection conn) {
		m_Connections.add(conn);
	}

	public void setOutput(double d) {
		this.output = d;
	}

	public List<NNConnection> getConnections() {
		return m_Connections;
	}
	
	public double getOutput() {
		return output;
	}

}
