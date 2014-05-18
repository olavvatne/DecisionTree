import java.io.ObjectInputStream.GetField;
import java.util.List;
import java.util.Random;

/**
 * Contains method to find importance using random selection and 
 * information gain by expected reduction of entropy.
 * @author Olav
 *
 */
public class Attribute {
	private int attribute;
	public double gain;
	
	public Attribute(int a) {
		this.attribute = a;
	}
	
	public int GetAttribute() {
		return attribute;
	}
	
	/**
	 * Random selection of attribute from list.
	 * @param attrs - list of attributes
	 * @param examples examples - not used
	 * @return Attribute selected randomly.
	 */
	public static Attribute importance(List<Attribute> attrs, List<DecisionData> examples) {
		Random r = new Random();
		return attrs.get(r.nextInt(attrs.size()));
	}
	
	/**
	 * Using expected reduction of entropy, finds the attribute that will 
	 * split the example data best. 
	 * 
	 * Calculates gain for all attributes in list supplied in arguments.
	 * Finds attribute with highest gain.
	 * 
	 * @param attrs - attributes list. 
	 * @param examples examples data
	 * @return Attribute with highest gain, or expected reduction of entropy
	 */
	public static Attribute importanceInfoGain(List<Attribute> attrs, List<DecisionData> examples) {
		
		for(Attribute a: attrs) {
			a.gain = gain(examples, a);
			
		}
		
		Attribute highestGain = attrs.get(0);
		for(int j = 1; j< attrs.size(); j++) {
			if(attrs.get(j).gain > highestGain.gain) {
				highestGain = attrs.get(j);
				
			}		
		}
		
		return highestGain;
	}
	
	
	/**
	 * Calculates gain for one attribute, using supplied example data.
	 * Only support binary choices. A bit messy:-(
	 * @param examples -example data.
	 * @param a -attribute to which gain will be calculated
	 * @return gain, or expected reduction of entropy
	 */
	public static double gain(List<DecisionData> examples, Attribute a) {
		int att = a.GetAttribute();
		double[] vPos = new double[2];
		double[] vNeg = new double[2];
		double p = 0;
		
		for(DecisionData d: examples) {
			if(d.classification == 1) {
				p ++;
			}
			
			if(d.attributes[att-1] == 1) {
				if(d.classification == 1) {
					vPos[0] ++;
				}
				else {
					vNeg[0] ++;
				}
			}
			else {
				if(d.classification == 1) {
					vPos[1] ++;
				}
				else {
					vNeg[1] ++;
				}
			}
		}
		
		double remainder = 0;
		double pn = examples.size();
		
		for(int j = 1; j<2; j++) {
			if((vPos[j-1] +vNeg[j-1]) == 0) {
				remainder += 0;
			}
			else {
				remainder += ((vPos[j-1] +vNeg[j-1])/pn)*b((vPos[j-1]/(vPos[j-1] +vNeg[j-1])));
			}
			
		}
		
		return b(p/pn) - remainder;
	}
	
	/**
	 * Math does not contain log2. 
	 * @param x log2(x)
	 * @return calcuated value
	 */
	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
	
	
	/**
	 * entropy of boolean random variable
	 * @param q - share of positive examples
	 * @return entropy of boolean random variable
	 */
	public static double b(double q) {
		return -((q*log2(q))+((1-q)*log2(1-q)));
	}
}
