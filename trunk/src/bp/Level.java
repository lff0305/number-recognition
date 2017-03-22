package bp;

import java.util.*;

public class Level {

	private List<Node> nodes = null;
	private int levelNumber = -1;
	
	public Level(int nodeSize, int levelNumber) {
		this.nodes = new ArrayList<Node>();
		this.levelNumber = levelNumber;
		for (int i=0; i<nodeSize; i++) {
			nodes.add(new Node(levelNumber, i));
		}
	}
	
	public List<Node> getNodes() {
		return this.nodes;
	}
	
	public Node getNode(int index) {
		return this.nodes.get(index);
	}
	
	public int getLevelNumber() {
		return this.levelNumber;
	}

}
