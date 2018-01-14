package dt;

import java.util.*;
import java.util.stream.Collectors;

import org.javatuples.Pair;

/** @author tasos */
public class DecisionTree {

	public DecisionTreeNode root = null;
	private String[] attributesAll;

	public DecisionTree(String[] attributes) {
		this.attributesAll = attributes;
	}

	public DecisionTreeNode ID3(List<Integer[]> examples, String[] attributes, String presetCategory,
			Pair<String, Boolean>[] usedAttributes) {
		DecisionTreeNode current;
		int sameCategory = checkSameCategory(examples);
		boolean hasNotUsed= Arrays.asList(usedAttributes).stream().map((Pair<String,Boolean> a)-> a.getValue1()).collect(Collectors.toList()).contains(false);
		if (examples.isEmpty()) {
			return new DecisionTreeNode("no", -1);
		} else if (sameCategory!=-1) {
			return sameCategory == 1 ? new DecisionTreeNode("yes", -1) : new DecisionTreeNode("no", -1);
		} else if (attributes.length==0) {
			return findFrequently(examples) >= 0.5 ? new DecisionTreeNode("yes", -1)
					: new DecisionTreeNode("no", -1);
		} else {
			String bestAttr = "";
			
			for (String attr : hightestIG(examples, attributes).values()) {
				
				if(usedAttributes[Arrays.asList(usedAttributes).stream().map((Pair<String,Boolean> a)->a.getValue0()).collect(Collectors.toList()).indexOf(attr)].getValue1())
					continue;
				bestAttr = attr;
				break;
				
			}
			int bestAttrAllIndex=Arrays.asList(attributesAll).indexOf(bestAttr);
			int bestAttrIndex = Arrays.asList(attributes).indexOf(bestAttr);
			usedAttributes[bestAttrAllIndex].setAt1(true);
			current= new DecisionTreeNode(bestAttr, bestAttrAllIndex);

			final String[] tmpAttributes = attributes;
			List<Integer[]> examplesi = new ArrayList<>();
			for(int i=0; i<examples.size(); i++){
				if(examples.get(i)[bestAttrAllIndex+1]==0){
					examplesi.add(examples.get(i));
				}
			}
			String[] attributesi = Arrays.asList(tmpAttributes).stream()
					.filter((String a) -> (!a.equals(tmpAttributes[bestAttrIndex]))).toArray(String[]::new);
			//current = new DecisionTreeNode(bestAttr, index)
			
			current.setLeft((DecisionTreeNode) ID3(examplesi, attributesi, presetCategory, usedAttributes));
			current.getLeft().setValue(0);
			examplesi = new ArrayList<>();
			for(int i=0; i<examples.size(); i++){
				if(examples.get(i)[bestAttrAllIndex+1]==1){
					examplesi.add(examples.get(i));
				}
			}
			
			current.setRight((DecisionTreeNode) ID3(examplesi, attributesi, presetCategory, usedAttributes));
			current.getRight().setValue(1);
			return current;
		}
		
	}

	public void print(DecisionTreeNode node, String character) {
		if(node.isSLeaf()){
			System.out.print(": "+node.getTag());
			return;
		}else{
			System.out.println();
			System.out.print(character + node.getTag()+ "=" +node.getLeft().getValue());
			print(node.getLeft(), character+"|  ");
			System.out.println();
			System.out.print(character + node.getTag()+ "=" +node.getRight().getValue());
			print(node.getRight(), character+"|  ");
		}
	}

	public Map<Double, String> hightestIG(List<Integer[]> examples, String[] attributes) {
		Map<Double, String> attrIG = new TreeMap<Double, String>(new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			};
		});
		for (int i = 0; i < attributes.length; i++) {
			int currentAttr = Arrays.asList(attributesAll).indexOf(attributes[i]);
			double currentIG = IG(examples, attributes, currentAttr);
			attrIG.put(currentIG, attributes[i]);
		}
		return attrIG;
	}
	
	public double accuracy(List<Integer[]> instances) {
		double accuracy =0.00;
		for (Integer[] instance : instances) {
			DecisionTreeNode tmp = this.root;
			while(!tmp.isSLeaf()){
				int instanceVal = instance[Arrays.asList(attributesAll).indexOf(tmp.getTag())+1];
				if(instanceVal==0){
					tmp = tmp.getLeft();
				}else {
					tmp = tmp.getRight();
				}
			}
			if(tmp.getTag().equals("yes") && instance[0]==1){
				accuracy++;
			}else if(tmp.getTag().equals("no") && instance[0]==0){
				accuracy++;
			}
		}
		accuracy = (accuracy*1.00)/(instances.size()*1.00)*100;
		return accuracy;
	}

	/**
	 * @param examples
	 * @param attr
	 *            attribute to find probability
	 * @return the probability to find 1 in a category/attribute in the examples
	 */
	private double findFrequently(List<Integer[]> examples) {
		double sum = examples.stream().map((Integer[] a) -> a[0]).mapToInt(Integer::intValue).sum();

		return (sum * 1.0) / (examples.size() * 1.0);
	}

	/**
	 * @param examples
	 * @param attr
	 *            attribute to find probability
	 * @param value
	 *            of attribute
	 * @return the probability to find 1(positive examples while a
	 *         category/attribute==value in the examples
	 */
	private double findFrequently(List<Integer[]> examples, int attr, int value) {
		double sum = 0.00;
		for(int i=0; i<examples.size(); i++){
			if(examples.get(i)[0]==1 && examples.get(i)[attr+1]==value){
				sum++;
			}
		}
		return (sum * 1.00) / (examples.size() * 1.00);
	}

	private int checkSameCategory(List<Integer[]> examples) {
		
		if(findFrequently(examples)==1.0)return 1;
		else if(findFrequently(examples)==0)return 0;
		else return -1;
	}

	private double entropy(List<Integer[]> examples, String[] attributes, int attr, int value) {
		double entropy = 0;
		double p = findFrequently(examples, attr, value);
		double n = 1 - p;
		entropy = p * (Math.log(p) / Math.log(2))
				+ n * (Math.log(n) / Math.log(2));
		return - entropy;
	}

	private double entropy(List<Integer[]> examples, String[] attributes) {
		double entropy = 0;
		double p = findFrequently(examples);
		double n = 1 - p;
		entropy = p * (Math.log(p) / Math.log(2))
				+ n * (Math.log(n) / Math.log(2));
		return - entropy;
	}

	private double IG(List<Integer[]> examples, String[] attributes, int attr) {
		double ig = 0;
		double p = findFrequently(examples);
		// ig = entropy(examples, attributes);// -
		// (p/(p+n))*Math.log((p/(p+n)))/Math.log(2) +
		// (n/(p+n))*Math.log(n/(p+n))/Math.log(2);
		double entropyi = entropy(examples, attributes, attr, 0);
		ig = entropy(examples, attributes) - p * entropy(examples, attributes, attr, 1)
				- (1 - p) * entropyi;

		return ig;
	}

}
