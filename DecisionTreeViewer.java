import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;


import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DecisionTreeViewer extends JPanel {
	private Node model;
	private HashMap<Node, Point> map = new HashMap<Node, Point>();
	
	public DecisionTreeViewer(Node node) {
		setModel(node);
	}
	
	public void setModel(Node node) {
		this.model = node;
		createMapOfNodes();
		this.repaint();
		
		
	}
	
	/**
	 * Breadth first search through decision tree, adding them to a 
	 * hashmap with node as key and Point on screen as position onscreen.
	 * Works only for binary trees. 
	 */
	public void createMapOfNodes() {
		Queue<Node> queue = new LinkedList<Node>() ;
    	HashMap<Node, Point> map = new HashMap<Node, Point>();
    	queue.clear();
    	queue.add(model);
    	int coord = 500;
    	int i = 0;
    	while(!queue.isEmpty()){
    		coord = -coord;
    		Node node = queue.remove();
    		i++;
    		
    		Point p = map.get(node.getParent());
    		if(p == null) {
    			
    			p = new Point(1500, 0);
    		}
    		
    		Point dp = new Point(p.x + (int)(coord/(node.getLevel()+1)), p.y +80);
    		
    		map.put(node, dp);
    		
    		if(node.getChild(0) != null) queue.add(node.getChild(0));
    		if(node.getChild(1) != null) queue.add(node.getChild(1));
    	}
    	this.map = map;
    	System.out.println(i + " nodes");
    }
	
	/**
	 * The overrided paintComponent uses hashmap of of key= node and
	 * value=Point to draw the tree onscreen. Hashmap is iterated two times,
	 * first time to draw lines beetween children and parents and second time
	 * to draw 
	 * 		Decision Node - oval
	 * 		Classification Node - Rectangle
	 * 
	 */
	public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        Graphics2D g2d = (Graphics2D)g; 
        g2d.setRenderingHint(
        	    RenderingHints.KEY_ANTIALIASING,
        	    RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3,
        		 BasicStroke.CAP_ROUND, 
                 BasicStroke.JOIN_ROUND));
        
        
        if(model != null) {
        	g2d.setFont(new Font(g.getFont().getName(), Font.PLAIN, 18));
        	
        	for (Map.Entry<Node, Point> entry : map.entrySet()) {
        		Node node = (Node)entry.getKey();
        		Point p = this.map.get(node.getParent());
        		Point dp = (Point)entry.getValue();
        		if(p != null) {
        			
        		
        		g2d.setColor(new Color(0,0,10));
        		g2d.drawLine(dp.x, dp.y, p.x, p.y);
        		
        		}
        	};
        	
        	for (Map.Entry<Node, Point> entry : map.entrySet()) { 
        		
        		if (entry.getKey() instanceof DecisionNode) {
        			DecisionNode node = (DecisionNode)entry.getKey();
        			Point dp = (Point)entry.getValue();
        			
        			g2d.setColor(new Color(148,199,182));
        			g2d.fillOval(dp.x-25, dp.y-25, 50, 50);
        			g2d.setColor(new Color(0,0,10));
        			g2d.drawString("A: " + node.attribute.GetAttribute(),
        					dp.x -10, dp.y+ 10);
        		}
        		else if (entry.getKey() instanceof ClassificationNode){
        			
        			ClassificationNode node = (ClassificationNode)entry.getKey();
        			Point dp = (Point)entry.getValue();
        			
        			g2d.setColor(new Color(148,199,182));
        			g2d.fillRect(dp.x-25, dp.y-25, 50, 50);
        			g2d.setColor(new Color(0,0,10));
        			g2d.drawString("C: " + node.classification,
        					dp.x -10, dp.y +10);
        		}
        	}
        }
        	
        g2d.dispose();
    }  
	
	
	public static void main(String[] args) {
		List<DecisionData> data = DecisionTree.readData("data/training.txt");
		//generalize attribute and its values
		List<Attribute> attrs = new ArrayList<Attribute>();
		attrs.add(new Attribute(1));
		attrs.add(new Attribute(2));
		attrs.add(new Attribute(3));
		attrs.add(new Attribute(4));
		attrs.add(new Attribute(5));
		attrs.add(new Attribute(6));
		attrs.add(new Attribute(7));
		
		Node parentInTree = DecisionTree.treeLearning(data, attrs, data, null);
		Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Decision Node viewer");
		DecisionTreeViewer panel = new DecisionTreeViewer(parentInTree);
		frame.add(panel);
		frame.setPreferredSize(sz);
		frame.pack();
		frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    List<DecisionData> tests = DecisionTree.readData("data/test.txt");
	    DecisionTree.labelData(parentInTree, tests);
	}
}
