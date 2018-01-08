package dt;

import java.util.*;
import java.util.stream.Collectors;

/** @author tasos */
public class DecisionTree {

  public DecisionTreeNode root;

  public DecisionTree() {}

  public DecisionTreeNode ID3(
      List<Integer[]> examples,
      String[] attributes,
      String presetCategory,
      boolean[] usedAttributes) {
    int sameCategory = 0;
    if (examples.isEmpty()) {
      return null;
    } else if (checkSameCategory(examples, sameCategory)) {
      return sameCategory == 1 ? null : null;
    } else if (attributes.length == 0) {
      return findFrequently(examples, 0) >= 0.5 ? null : null;
    } else {
      if(attributes.length==0)return null;
      String bestAttr = hightestIG(examples, attributes).values().parallelStream().findFirst().get();
      int bestAttrIndex = Arrays.asList(attributes).indexOf(bestAttr);
      usedAttributes[bestAttrIndex]=true;
      
      root = new DecisionTreeNode(bestAttr, bestAttrIndex);
      
      for(int i=0; i<2; i++){
    	  final int index = i;
    	  List<Integer[]> examplesi = examples.stream().filter((Integer[] v)-> v[bestAttrIndex]==index)
    			  .collect(Collectors.toList());
    	  final String[] tmpAttributes = attributes;
    	  if(index==0){    		  //local attributes
    		  attributes = Arrays.asList(attributes).stream().filter((String a)->(!a.equals(tmpAttributes[bestAttrIndex]))).toArray(String[]::new);
    		  root.setLeft((DecisionTreeNode) ID3(examplesi, 
    				  attributes, presetCategory,usedAttributes));
    		  try{
    			  root.getLeft().setValue(index);
    		  }catch (Exception e) {
    			  print(root, 0);
    		  }
    	  }else{
    		  attributes = Arrays.asList(attributes).stream().filter((String a)->!a.equals(tmpAttributes[bestAttrIndex])).toArray(String[]::new);
    		  root.setRight((DecisionTreeNode) ID3(examplesi, attributes , presetCategory,usedAttributes));
    		  root.getRight().setValue(index);
    	  }
      }
    }

    return null;
  }
  
  public void print(DecisionTreeNode node, int ct) {
	  if(node.getLeft()!=null){
		  print(node.getLeft(), ct);
		  print(node.getRight(), ct);
		  ct++;
	  }else{
		  System.out.println();
		  for(int i=0; i<ct; i++){
			  System.out.print("-");
		  }
		  System.out.print(node.getTag() + "=" + node.getValue());
		  
	  }
	  
	 
  }

  public Map<Double, String> hightestIG(List<Integer[]> examples, String[] attributes) {
	  Map<Double, String> attrIG = new TreeMap<Double, String>(new Comparator<Double>(){
		  @Override
		  public int compare(Double o1, Double o2) {
			  return o2.compareTo(o1);
		  };
	  });
	  for(int i=0; i<attributes.length; i++){
		  double currentIG = IG(examples, attributes, i);
		  attrIG.put(currentIG, attributes[i]);
	  }
	  return attrIG;
  }

  /**
   * @param examples
   * @param attr attribute to find probability
   * @return the probability to find 1 in a category/attribute in the examples
   */
  private double findFrequently(List<Integer[]> examples, int attr) {
    int sum = examples.stream().map((Integer[] a) -> a[attr]).mapToInt(Integer::intValue).sum();

    return (sum * 1.0) / (examples.size() * 1.0);
  }

  private DecisionTreeNode maketree(List<Integer[]> examples, boolean[] usedAttributes) {
    // TODO Auto-generated method stub
    return null;
  }

  private boolean checkSameCategory(List<Integer[]> examples, int sameCategory) {

    boolean result =
        examples.get(0)[0] == 0
            ? examples
                    .stream()
                    .filter((Integer[] val) -> val[0] == 0)
                    .collect(Collectors.toList())
                    .size()
                == examples.size()
            : examples
                    .stream()
                    .filter((Integer[] val) -> val[1] == 0)
                    .collect(Collectors.toList())
                    .size()
                == examples.size();
    if (examples.get(0)[0] == 0 && result) {
      sameCategory = 0;
    } else if (examples.get(0)[0] == 1 && result) {
      sameCategory = 1;
    }
    return result;
  }

  private double IG(List<Integer[]> examples, String[] attributes, int attr) {
    double ig=0;
    double p=findFrequently(examples, attr)*examples.size();
    double n = examples.size()-p;
    ig = (p/(p+n))*Math.log((p/(p+n)))/Math.log(2) + (n/(p+n))*Math.log(n/(p+n))/Math.log(2);
    return ig;
  }
  
}
