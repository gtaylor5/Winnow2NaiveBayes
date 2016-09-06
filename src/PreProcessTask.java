import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class PreProcessTask {
	
	ArrayList<String[]> lines = new ArrayList<String[]>();
	ArrayList<String[]> linesCopy = new ArrayList<String[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	String dataSetName = "";
	HashMap<Integer, String> classes = new HashMap<Integer, String>();
	int numClasses = 0;
	
	public PreProcessTask(String name){
		this.dataSetName = name;
	}
	
	public void storeFileInArray(String fileLocation) throws FileNotFoundException{
		File file = new File(fileLocation);
		file.setReadable(true);
		Scanner input = new Scanner(file);
		
		while(input.hasNextLine()){
			String line = input.nextLine();
			String[] lineAsArray = line.split(",");
			lines.add(lineAsArray);
		}
		lines.trimToSize();
	}
	
	public void booleanizeData(){
		if(dataSetName == "VoteCount"){
			processVoteCount();
			numClasses = 2;
			classes.put(0, "Republican");
			classes.put(1, "Democrat");
		}else if(dataSetName.equalsIgnoreCase("BreastCancer")){
			processBreastCancer();
			numClasses = 2;
			classes.put(0, "Benign");
			classes.put(1, "Malignant");
		}else if(dataSetName.equalsIgnoreCase("GlassID")){
			processGlassID();
			numClasses = 6;
			classes.put(1, "Building Windows Float Processed");
			classes.put(2, "Building Windows Non-Float Processed");
			classes.put(3, "Vehicle Windows Float Processed");
			classes.put(5, "Containers");
			classes.put(6, "Tableware");
			classes.put(7, "Headlamps");
		}else if(dataSetName.equalsIgnoreCase("Iris")){
			processIris();
			numClasses = 3;
			classes.put(1, "Iris-setosa");
			classes.put(2, "Iris-versicolor");
			classes.put(3, "Iris-virginica");
		}else{
			processSoyBean();
			numClasses = 4;
			classes.put(1, "Diaporthe Stem Canker");
			classes.put(2, "Charcoal Rot");
			classes.put(3, "Rhizoctonia Root Rot");
			classes.put(4, "Phytophthora Rot");
		}
	}
	
	public void processBreastCancer(){
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[lines.get(i).length-1]; //ignoring patient ID
			for(int j = 1; j < lines.get(i).length; j++){
				if(!lines.get(i)[j].equalsIgnoreCase("?")){
					if(j == lines.get(i).length-1){
						if(Integer.parseInt(lines.get(i)[j]) == 4){
							booleanizedArray[j-1] = 1; //Malignant
							continue;
						}else{
							booleanizedArray[j-1] = 0; //benign
							continue;
						}
					}
					if(Integer.parseInt(lines.get(i)[j]) < 4){
						booleanizedArray[j-1] = 0; //Bucket of values less than 5 is a zero based on cursory glance of data
						continue;
					}
					booleanizedArray[j-1] = 1; //Anything greater than or equal to 5 is a flag.
				}else{
					booleanizedArray[j-1] = imputateData(i,j);
				}
			}
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public void processVoteCount(){
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[lines.get(i).length];
			for(int j = 1; j < lines.get(i).length; j++){
					if(lines.get(i)[j].equalsIgnoreCase("n")){
						booleanizedArray[j-1]= 0; // no = 0
					}else if(lines.get(i)[j].equalsIgnoreCase("y")){
						booleanizedArray[j-1] = 1; // yes = 1
					}else if(lines.get(i)[j].equals("?")){ //if "?"
						booleanizedArray[j-1] = imputateData(i, j);
				}
			}
			if(lines.get(i)[0].equalsIgnoreCase("republican")){
				booleanizedArray[booleanizedArray.length-1] = 0;
			}else{
				booleanizedArray[booleanizedArray.length-1] = 1;
			}
			booleanizedFile.add(booleanizedArray);
		}
	}

	public void processGlassID(){ //Glass has no missing values. No need to imputate.
		double[] means = {1.5184, 13.407, 2.6845, 1.4449, 72.6509, .4971, .9570, .1750, .0570};
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[755]; //There are 152 buckets each bucket holds a range of .5. So, 1.5 would go into index 2.
			for(int j = 1; j < lines.get(i).length-1; j++){
				double val = Double.parseDouble(lines.get(i)[j]);
				booleanizedArray[((int)(val/.1))] = 1;
			}
			booleanizedArray[booleanizedArray.length-1] = Integer.parseInt(lines.get(i)[lines.get(i).length-1]);
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public void processIris(){
		double[] slStats = {4.3, 7.9, 5.84, .83};
		double[] swStats = {2.0,4.4,3.02,.43};
		double[] plStats = {1.0, 6.9,3.76, 1.76};
		double[] pwStats = {.1, 2.5, 1.20, .76};
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[5];
			for(int j = 0; j < lines.get(i).length-1; j++){
				double val;
				switch(j){
				case 0:
					val = Double.parseDouble(lines.get(i)[j]);
					if(val >= slStats[2]+slStats[3]) {
						booleanizedArray[j] = 1;
					}else if(val <= slStats[2]-slStats[3]){
						booleanizedArray[j] = 0;
					}else{
						if(val >= slStats[2]){
							booleanizedArray[j] = 1;
						}else{
							booleanizedArray[j] = 0;
						}
					}
					break;
				case 1:
					val = Double.parseDouble(lines.get(i)[j]);
					if(val >= swStats[2]+swStats[3]) {
						booleanizedArray[j] = 1;
					}else if(val <= swStats[2]-swStats[3]){
						booleanizedArray[j] = 0;
					}else{
						if(val >= swStats[2]){
							booleanizedArray[j] = 1;
						}else{
							booleanizedArray[j] = 0;
						}
					}
					break;
				case 2:
					val = Double.parseDouble(lines.get(i)[j]);
					if(val >= plStats[2]+plStats[3]) {
						booleanizedArray[j] = 1;
					}else if(val <= plStats[2]-plStats[3]){
						booleanizedArray[j] = 0;
					}else{
						if(val >= plStats[2]){
							booleanizedArray[j] = 1;
						}else{
							booleanizedArray[j] = 0;
						}
					}
					break;
				case 3:
					val = Double.parseDouble(lines.get(i)[j]);
					if(val >= pwStats[2]+pwStats[3]) {
						booleanizedArray[j] = 1;
					}else if(val <= pwStats[2]-pwStats[3]){
						booleanizedArray[j] = 0;
					}else{
						if(val >= pwStats[2]){
							booleanizedArray[j] = 1;
						}else{
							booleanizedArray[j] = 0;
						}
					}
					break;
				}
			}
			if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("Iris-setosa")){
				booleanizedArray[booleanizedArray.length-1] = 1;
			}else if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("Iris-versicolor")){
				booleanizedArray[booleanizedArray.length-1] = 2;
			}else{
				booleanizedArray[booleanizedArray.length-1] = 3;
			}
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public void processSoyBean(){
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[(7*35)+1];
			for(int j = 0; j < lines.get(i).length-1; j++){
				booleanizedArray[(7*j)+(Integer.parseInt(lines.get(i)[j]))] = 1;
			}
			if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("D1")){
				booleanizedArray[booleanizedArray.length-1] = 1;
			}else if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("D2")){
				booleanizedArray[booleanizedArray.length-1] = 2;
			}else if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("D3")){
				booleanizedArray[booleanizedArray.length-1] = 3;
			}else{
				booleanizedArray[booleanizedArray.length-1] = 4;
			}
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public int imputateData(int listIndex, int arrayIndex){
		if(dataSetName == "VoteCount"){
			int y = 0, n = 0, count = 0;
			for(int i = 0; i < lines.size(); i++){
				if(lines.get(i)[0].equalsIgnoreCase(lines.get(listIndex)[0])){ //if the classes are the same (republican or democrat)
					if(!lines.get(i)[arrayIndex].equalsIgnoreCase("?")){
						count++;
						if(lines.get(i)[arrayIndex].equalsIgnoreCase("y")){
							y++; //increment yes count
							continue;
						}else if(lines.get(i)[arrayIndex].equalsIgnoreCase("n")){
							n++; //increment no count
						}
					}
				}
			}
			return (y/count >= n/count) ? 1 : 0;
		}else if(dataSetName.equalsIgnoreCase("BreastCancer")){
			int y = 0, n = 0, count = 0;
			for(int i = 0; i < lines.size(); i++){
				if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase(lines.get(listIndex)[lines.get(i).length-1])){
					if(!lines.get(i)[arrayIndex].equals("?")){
						count++;
						if(Integer.parseInt(lines.get(i)[arrayIndex]) >= 4){
							y++; //increment yes count
							continue;
						}else if(Integer.parseInt(lines.get(i)[arrayIndex]) < 4){
							n++; //increment no count
						}
					}
				}
			}
			return (y/count >= n/count) ? 1 : 0; 
		}
		return 0;
	}
}
