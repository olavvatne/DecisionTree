/**
 * Classification Node extends a general node and contains class label info.
 * When classifing data tree is traversed to such an classif. node.
 * 
 * unanimous added to know if majority or all fell under classification.
 * This variable is not used.
 * 
 * @author Olav
 *
 */
public class ClassificationNode extends Node {
	int classification;
	boolean unanimous;
	
	public ClassificationNode(Node p,int classification, boolean unanimous) {
		super(p);
		this.classification = classification;
		this.unanimous = unanimous;
	}
}
