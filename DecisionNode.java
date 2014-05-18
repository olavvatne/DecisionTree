

/**
 * Subclass of Node. Used in interior of the decision tree, representing
 * a decision on an attribute.
 * 
 * Contain variable telling what attribute decision should be based on.
 * @author Olav
 *
 */
public class DecisionNode extends Node {
	Attribute attribute;
	
	
	public DecisionNode(Node p, Attribute attribute) {
		super(p);
		this.attribute = attribute;
	}
	
	
}
