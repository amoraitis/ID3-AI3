//Data source http://archive.ics.uci.edu/ml/datasets/Abalone

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	private final static File currentFilePath = new File(Main.class.getProtectionDomain()
			.getCodeSource().getLocation().getPath());
	
	private static String DATAFOLDER = currentFilePath.getParentFile()+ File.separator+"data"+File.separator;
	
	private static ArrayList<Abalone> abalones;
	public static void main(String[] args) throws FileNotFoundException{
		abalones = new ArrayList<Abalone>();
		importData();
		
	}
	
	private static void importData() throws FileNotFoundException{
		Scanner scanner = new Scanner(new FileReader(DATAFOLDER+"abalone.data"));
		String line = scanner.nextLine();
		while(scanner.hasNextLine()){
			String[] fields = line.split(",");
			abalones.add(new Abalone(
					fields[0],
					getDouble(fields[1]),
					getDouble(fields[2]),
					getDouble(fields[3]),
					getDouble(fields[4]),
					getDouble(fields[5]),
					getDouble(fields[6]),
					getDouble(fields[7]),
					Integer.parseInt(fields[8])));
			line = scanner.nextLine();
		}
		scanner.close();
	}
	
	private static double getDouble(String field){
		return Double.parseDouble(field);
	}
}
