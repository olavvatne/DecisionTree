import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * Contains algorithm for learning decision tree, and methods for reading 
 * training and test data from file. Based on psuodocode provided in
 * Artifical Intelligence, a modern approch (Stuart Russell, Peter Norvig)
 * @author Olav
 *
 */
public class DecisionTree {
	
	/**
	 * Recursive method that creates a decision tree using the object nodes, 
	 * decisionNode and ClassificationNode
	 * . Parent node added to method arguments
	 * to support graphical representation (lines). 
	 * @param examples - examples left to use to find good decision to split on.
	 * @param attributes - that can be used to split on.
	 * @param parentExamples - all examples provided the tree creation
	 * @param p
	 * @return
	 */
	public static Node treeLearning(List<DecisionData> examples ,List<Attribute> attributes, List<DecisionData> parentExamples, Node p) {
		
		if(examples.isEmpty()) {
			//returns classification node with value of the majority of all examples
			return DecisionData.getPluralityValue(parentExamples ,p);
		}
		else if(DecisionData.hasSameClassification(examples)) {
			//returns classification node with unanimous class.
			return DecisionData.getUnanimousValue(examples, p);
		}
		else if(attributes.isEmpty()) {
			//returns classification node with class of majority of examples left
			return DecisionData.getPluralityValue(examples, p);
		}
		else {
			//change to importance for random choice of attribute to split on.
			//importanceInfoGain finds best attribute to split examples on. Most reduction
			//of entropy.
			Attribute a = Attribute.importanceInfoGain(attributes, examples);
			List<Attribute> subAttributes = new ArrayList<Attribute>(attributes);
			subAttributes.remove(a);
			Node tree = new DecisionNode(p, a);
			
			for(int i = 1; i<=2; i++) {
				List<DecisionData> subExamples = DecisionData.splitOnAttributeValue(examples, a, i);
				Node subTree = treeLearning(subExamples, subAttributes, parentExamples, tree);
				tree.addChild(subTree);
			}
			return tree;
		}
		
	}
	
	
	
	
	/**
	 * used to label test example data. Traverses the tree to ClassificationNode
	 * is found. Only supports binary trees. I.E no decisions can have three options.
	 * @param root -root node of tree
	 * @param test - tests to be labelled
	 */
	public static void labelData(Node root, List<DecisionData> test) {
		double testSize = test.size();
		double positiveMatch = 0;
		for(DecisionData dd: test) {
			Node node = root;
			while(node instanceof DecisionNode) {
				DecisionNode dn = (DecisionNode)node;
				int decision = dn.attribute.GetAttribute();
				int ld = dd.attributes[decision-1];
				if(ld == 1) {
					node = node.getChild(0);
				}
				else if (ld ==2) {
					node = node.getChild(1);
				}
				
			}
			dd.labelClass = ((ClassificationNode)node).classification;
			if(dd.labelClass == dd.classification) {
				System.out.println("riktig " + dd.labelClass);
				positiveMatch ++;
			}
			else {
				System.out.println("feil label: " + dd.labelClass + " test: " + dd.classification);
				
			}
		}
		System.out.println(positiveMatch/testSize);
	}
	
	/**
	 * Reads example data from file and structure them into objects.
	 * @param url - path to file
	 * @return list with example objects
	 */
	public static List<DecisionData> readData(String url) {
		BufferedReader br;
		List<DecisionData> exampleList = new ArrayList<DecisionData>();
		try {
			FileReader r = new FileReader(new File(url));
			br = new BufferedReader(r);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		try {
			String line = null;
			while((line = br.readLine()) != null) {
				DecisionData example = new DecisionData(line.split("\t"));
				exampleList.add(example);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return exampleList;
	}
}
