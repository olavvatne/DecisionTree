import java.util.ArrayList;
import java.util.List;

/**
 * General node in in a tree. Contain basic variables and method to
 * support a a tree data structure. 
 * Level is not used by algorithm, only for graphical purposes.
 * @author Olav
 *
 */
public class Node {
	private Node parent;
	private List<Node> children;
	private int Level = 0;
	
	public Node(Node parent) {
		setParent(parent);
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
		if(parent != null) {
			this.Level =this.parent.Level +1;
		}
	}
	public Node getParent() {
		return this.parent;	
	}
	
	public  void addChild(Node node) {
		if(children == null) {
			this.children = new ArrayList<Node>();
		}
		this.children.add(node);
	}
	
	public Node getChild(int i) {
		if(children != null && i<children.size()){
			return children.get(i);
		}
		else {
			return null;
		}
		
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	//GUI purpose
	public int getLevel() {
		return this.Level;
	}
}
