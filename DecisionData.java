import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.border.EmptyBorder;

/**
 * Structuring class for training data and test data read from file.
 * classification variable are used to put in intended classification.
 * labelClass used when tree is used to classify examples.
 * 
 * When testing classification and labelClass can be compared.
 * @author Olav
 *
 */
public class DecisionData {
	public int[] attributes;
	public int classification;
	public int labelClass;
	
	
	public DecisionData(int[] attrs, int classification) {
		this.attributes = attrs;
		this.classification = classification;
	}
	
	/**
	 * Helper constructor that structure a line read from file into
	 * attributes and classification. Supports only examples in integer form.
	 * @param data - string read from file. 
	 */
	public DecisionData(String[] data) {
		int[] attrs = new int[data.length-1];
		for(int i = 0; i<data.length-1; i++) {
			attrs[i] = Integer.parseInt(data[i]);
		}
		this.attributes = attrs;
		this.classification = Integer.parseInt(data[data.length -1]);
	}
	
	
	/**
	 * Helper method that decides if all data examples contain same classification.
	 * 
	 * @param examples
	 * @return
	 */
	public static boolean hasSameClassification(List<DecisionData> examples) {

		Integer classifier = null;
		for(DecisionData d: examples) {
			if(classifier == null) 
				classifier = d.classification;

			if(classifier != d.classification) 
				return false;
		}
		return true;
	}
	
	/**
	 * Returns node containing the class label of the majority of the examples
	 * @param list of data examples 
	 * @param p parent node
	 * @return ClassificationNode with class label of majority of examples
	 */
	public static ClassificationNode getPluralityValue(List<DecisionData> examples, Node p) {
		int[] classCount = new int[2]; //generalize
		for(DecisionData d : examples) {
			classCount[d.classification-1] ++;
		}
		int vote = 0;
		for(int i=0; i<classCount.length; i++) {
			if(classCount[i]>vote) {
				vote = i;
			}
			else if(classCount[i] == vote) {
				vote = (Math.random() > 0.5) ? i : vote;
			}
		}
		
		return new ClassificationNode(p, vote+1, false);
	}
	
	/**
	 * If all examples have same the class label, then method will return classificationNode
	 * with that class label. Must check beforehand if unanimous.
	 * @param examples - list of example data
	 * @param p parent node
	 * @return classification node - with the unanimous value
	 */
	public static ClassificationNode getUnanimousValue(List<DecisionData> examples, Node p) {
		return new ClassificationNode(p, examples.get(0).classification, true);
	}
	
	
	/**
	 * Split examples using an attribute value. subset of examples agreeing
	 * with attribute value is returned.
	 * @param data -list of examples to be split
	 * @param attr - attribute to split on
	 * @param value - attribute value used for finding subset of list
	 * @return subset of examples provided
	 */
	public static List<DecisionData> splitOnAttributeValue( List<DecisionData> data , Attribute attr, int value) {
		List<DecisionData> subList = new ArrayList<DecisionData>(data);
	
		for(DecisionData d: data) {
			if(d.attributes[attr.GetAttribute()-1] != value) {
				subList.remove(d);
			}
		}
		return subList;
	}
}
