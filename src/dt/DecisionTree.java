package dt;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.javatuples.Pair;

/** @author tasos */
public class DecisionTree {

	public DecisionTreeNode root = null;
	private String[] attributesAll;
	private List<Integer> categorials;

	public DecisionTree(String[] attributes, List<Integer> categorials) {
		this.attributesAll = attributes;
		this.categorials = categorials;
	}

	public DecisionTreeNode ID3(List<Integer[]> examples, String[] attributes, String presetCategory,
			Pair<String, Boolean>[] usedAttributes) {
		DecisionTreeNode current;
		int sameCategory = 0;
		boolean hasNotUsed= Arrays.asList(usedAttributes).stream().map((Pair<String,Boolean> a)-> a.getValue1()).collect(Collectors.toList()).contains(false);
		if (examples.isEmpty()) {
			return new DecisionTreeNode("no", -1);
		} else if (checkSameCategory(examples, sameCategory)) {
			return sameCategory == 1 ? new DecisionTreeNode("yes", -1) : new DecisionTreeNode("no", -1);
		} else if (hasNotUsed==false) {
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
			if(bestAttr.equals(""))return null;
			int bestAttrIndex=Arrays.asList(attributes).indexOf(bestAttr);
			usedAttributes[bestAttrIndex].setAt1(true);
			current= new DecisionTreeNode(bestAttr, bestAttrIndex);

			final String[] tmpAttributes = attributes;
			
			List<Integer[]> examplesi = examples.stream().filter((Integer[] v) -> v[bestAttrIndex] == 0)
					.collect(Collectors.toList());// local attributes
			attributes = Arrays.asList(attributes).stream()
					.filter((String a) -> (!a.equals(tmpAttributes[bestAttrIndex]))).toArray(String[]::new);
			//current = new DecisionTreeNode(bestAttr, index)
			current.setTag(bestAttr);
			current.setLeft((DecisionTreeNode) ID3(examplesi, attributes, presetCategory, usedAttributes));
			if(current.getLeft()!=null)current.getLeft().setValue(0);

			examplesi = examples.stream().filter((Integer[] v) -> v[bestAttrIndex] == 1).collect(Collectors.toList());
			attributes = Arrays.asList(attributes).stream()
					.filter((String a) -> !a.equals(tmpAttributes[bestAttrIndex])).toArray(String[]::new);
			current.setRight((DecisionTreeNode) ID3(examplesi, attributes, presetCategory, usedAttributes));
			if(current.getRight()!=null)current.getRight().setValue(1);
			return current;
		}
		
	}

	public void print(DecisionTreeNode node, String character) {
		if(node==null)return;
		boolean hasChild = node.getLeft()!=null;
		String newCharacter;
		if(character.equals("")){
			newCharacter = ".. ";
		}else{
			newCharacter = "..." + character;
		}
		System.out.println(character + node.getTag()+"="+node.getValue());
		print(node.getLeft(), newCharacter);
		print(node.getRight(), newCharacter);		
	}

	public Map<Double, String> hightestIG(List<Integer[]> examples, String[] attributes) {
		Map<Double, String> attrIG = new TreeMap<Double, String>(new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			};
		});
		for (int i = 0; i < attributes.length; i++) {
			double currentIG = IG(examples, attributes, i);
			attrIG.put(currentIG, attributes[i]);
		}
		return attrIG;
	}

	/**
	 * @param examples
	 * @param attr
	 *            attribute to find probability
	 * @return the probability to find 1 in a category/attribute in the examples
	 */
	private double findFrequently(List<Integer[]> examples) {
		int sum = categorials.stream().map((Integer a) -> a).mapToInt(Integer::intValue).sum();

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
		int sum = 0;
		for(int i=0; i<examples.size(); i++){
			if(categorials.get(i)==1 && examples.get(i)[attr]==value) sum++;
		}
		return (sum * 1.0) / (examples.size() * 1.0);
	}

	private boolean checkSameCategory(List<Integer[]> examples, int sameCategory) {

		boolean result = categorials.get(0) == 0
				? categorials.stream().filter((Integer val) -> val == 0).collect(Collectors.toList())
						.size() == examples.size()
				: categorials.stream().filter((Integer val) -> val == 0).collect(Collectors.toList())
						.size() == examples.size();
		if (categorials.get(0) == 0 && result) {
			sameCategory = 0;
		} else if (categorials.get(0) == 1 && result) {
			sameCategory = 1;
		}
		return result;
	}

	private double entropy(List<Integer[]> examples, String[] attributes, int attr, int value) {
		double ig = 0;
		double p = findFrequently(examples, attr, value) * examples.size();
		double n = examples.size() - p;
		ig = (p / (p + n)) * Math.log((p / (p + n))) / Math.log(2)
				+ (n / (p + n)) * Math.log(n / (p + n)) / Math.log(2);
		return ig;
	}

	private double entropy(List<Integer[]> examples, String[] attributes) {
		double ig = 0;
		double p = findFrequently(examples) * examples.size();
		double n = examples.size() - p;
		ig = (p / (p + n)) * Math.log((p / (p + n))) / Math.log(2)
				+ (n / (p + n)) * Math.log(n / (p + n)) / Math.log(2);
		return ig;
	}

	private double IG(List<Integer[]> examples, String[] attributes, int attr) {
		double ig = 0;
		double p = findFrequently(examples) * examples.size();
		// ig = entropy(examples, attributes);// -
		// (p/(p+n))*Math.log((p/(p+n)))/Math.log(2) +
		// (n/(p+n))*Math.log(n/(p+n))/Math.log(2);
		ig = entropy(examples, attributes) - p * entropy(examples, attributes, attr, 1)
				- (1 - p) * entropy(examples, attributes, attr, 0);

		return ig;
	}

}
