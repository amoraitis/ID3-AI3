//Data source http://archive.ics.uci.edu/ml/datasets/Abalone
package app;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dt.DecisionTree;

public class Main {
	private final static File currentFilePath = new File(Main.class.getProtectionDomain()
			.getCodeSource().getLocation().getPath());
	
	private static String DATAFOLDER = currentFilePath.getParentFile().getParentFile()+ File.separator+"data"+File.separator;
	private static String[] attributes;
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Give train filename:");
		String trainDataFile = "SPECT.train"; //sc.nextLine();
		System.out.println("Give test filename:");
		String testDataFile = "SPECT1.test"; //sc.nextLine();
		ArrayList<Integer[]> trainData = importData(trainDataFile);
		ArrayList<Integer[]> testData = importData(testDataFile);
		/*trainData.forEach((Integer[] val)->{
			for(int i=0; i<val.length; i++){
				System.out.print((i==val.length-1? val[i] : val[i]+",")+"");
			}
			System.out.println();
		});
		System.out.println("--------------------------------------");*/
		DecisionTree dTree = new DecisionTree();
		boolean[] usedAttributes = new boolean[attributes.length];
		Arrays.fill(usedAttributes, false);
		Arrays.asList(attributes).remove(attributes[0]);
		System.out.println(attributes[0]);
		dTree.ID3(trainData, attributes, dTree.hightestIG(trainData, attributes).values().parallelStream().findFirst().get(),usedAttributes);
		dTree.print(dTree.root, 0);
		//testData.forEach(System.out::println);
		/*testData.forEach((Integer[] val)->{
			for(int i=0; i<val.length; i++){
				System.out.print((i==val.length-1? val[i] : val[i]+",")+"");
			}
			System.out.println();
		});*/
		
	}
	
	private static <T> ArrayList<Integer[]> importData(String filename) throws FileNotFoundException{
		ArrayList<Integer[]> res = new ArrayList<Integer[]>();
		Scanner scanner = new Scanner(new FileReader(DATAFOLDER+filename));
		String line = scanner.nextLine();
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
	
	private static double getDouble(String field){
		return Double.parseDouble(field);
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
