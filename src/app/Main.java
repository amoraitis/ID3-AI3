//Data source http://archive.ics.uci.edu/ml/datasets/Abalone
package app;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.javatuples.Pair;

import dt.DecisionTree;
import dt.DecisionTreeNode;

public class Main {
	private final static File currentFilePath = new File(Main.class.getProtectionDomain()
			.getCodeSource().getLocation().getPath());
	
	private static String DATAFOLDER = currentFilePath.getParentFile().getParentFile()+ File.separator+"data"+File.separator;
	private static String[] attributes;
	private static List<Integer> categorials;
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Give train filename:");
		String trainDataFile = "SPECT.train"; //sc.nextLine();
		System.out.println("Give test filename:");
		String testDataFile = "SPECT1.test"; //sc.nextLine();
		ArrayList<Integer[]> trainData = importData(trainDataFile);
		categorials = new ArrayList<>();
		ArrayList<Integer[]> testData = importData(testDataFile);
		Arrays.asList(trainData.get(0)).forEach((Integer a)->System.out.print(a+","));
		System.out.println();
		ArrayList<Integer[]> tmp = new ArrayList<>();
		for (Integer[] a : trainData) {
			categorials.add(a[0]);
			a = Arrays.copyOfRange(a, 1, a.length);
			tmp.add(a);
		}
		trainData = tmp;
		
		Arrays.asList(trainData.get(0)).forEach((Integer a)->System.out.print(a+","));
		DecisionTree dTree = new DecisionTree(attributes, categorials);
		Pair<String, Boolean>[] usedAttributes = new Pair[attributes.length];
		for(int i=0; i<attributes.length; i++){
			usedAttributes[i] = new Pair<String, Boolean>(attributes[i], false);
		}
		String[] nonCategorialAttributes = new String[attributes.length-1];
		for(int i=0; i<attributes.length-1; i++)nonCategorialAttributes[i]=attributes[i+1];
		
		DecisionTreeNode root = dTree.ID3(trainData, nonCategorialAttributes, dTree.hightestIG(trainData, nonCategorialAttributes).values().parallelStream().findFirst().get(),usedAttributes);
		dTree.root = root;
		dTree.print(dTree.root, "");
		//testData.forEach(System.out::println);
		/*testData.forEach((Integer[] val)->{
			for(int i=0; i<val.length; i++){
				System.out.print((i==val.length-1? val[i] : val[i]+",")+"");
			}
			System.out.println();
		});*/
		
	}
	
	private static ArrayList<Integer[]> importData(String filename) throws FileNotFoundException{
		ArrayList<Integer[]> res = new ArrayList<Integer[]>();
		Scanner scanner = new Scanner(new FileReader(DATAFOLDER+filename));
		String line = scanner.nextLine();
		if(filename.contains("train"))
			attributes= line.trim().split(",");
		System.out.println(attributes.length);
		line = scanner.nextLine();
		int ct =0 ;
		while(scanner.hasNextLine()){
			Integer[] fields = new Integer[attributes.length];
			
			Arrays.asList(line.split(","))
						.stream().map((v)->Integer.parseInt(v)).collect(Collectors.toList()).toArray(fields);
			res.add(fields);
			ct++;
			line = scanner.nextLine();
		}
		System.out.println(ct);
		scanner.close();
		return res;
	}
	
	private static void correctTest() throws IOException {
		Scanner scin = new Scanner(new FileInputStream(new File(DATAFOLDER+"SPECT.test")));
		FileWriter scout = new FileWriter(DATAFOLDER+"SPECT1.test",false);
		String line = scin.nextLine();
		line.replaceAll("OVERALL_DIAGNOSIS,", "");
		scout.write(line+"\n");
		line = scin.nextLine();
		while(scin.hasNextLine()){
			line=line.substring(2);
			scout.write(line+"\n");
			line = scin.nextLine();
		}
		scout.flush();
		scin.close();
		
	}
}
