import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class PreProcessTask {
	
	ArrayList<String[]> lines = new ArrayList<String[]>();
	ArrayList<String[]> linesCopy = new ArrayList<String[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	String dataSetName = "";
	HashMap<Integer, String> classes = new HashMap<Integer, String>();
	int numClasses = 0;
	int[] classSizes;
	
	public PreProcessTask(String name){
		this.dataSetName = name;
	}
	
	//parses file and stores in space separated array then added to arraylist.
	
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
		input.close();
	}
	/************************************************************
	
	booleanizeData() converts comma separated data into boolean 
	data based on the input dataSet name. It also stores the classes
	in a hashmap.
	
	************************************************************/
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
			classSizes = new int[]{70,76,17,13,9,29};
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
	/************************************************************
	
	processBreastCancer places corresponding values into buckets.
	Which ever integer value is shown gets stored in value-1 elemtn
	in the array. Malignant = 1 and benign = 0
	
	************************************************************/
	
	public void processBreastCancer(){
		for(int i = 0; i < lines.size(); i++){
			ArrayList<Integer> encodedData = new ArrayList<Integer>(); 
			for(int j = 1; j < lines.get(i).length; j++){//ignoring patient ID-
				int[] bArray;
				if(!lines.get(i)[j].equalsIgnoreCase("?")){
					if(j == lines.get(i).length-1){
						if(Integer.parseInt(lines.get(i)[j]) == 4){
							encodedData.add(1); //Malignant
							break;
						}else{
							encodedData.add(0); //benign
							break;
						}
					}
					bArray = new int[10];
					int index = Integer.parseInt(lines.get(i)[j]);
					bArray[index-1] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else{
					bArray = new int[10];
					int index = imputateData(i,j);
					bArray[index-1] = 1;
					encodedData = fillArray(bArray, encodedData);
				}
			}
			encodedData.trimToSize();
			Integer[] encodedDataAsArray = new Integer[encodedData.size()];
			int[] encodedDataAsArray1 = new int[encodedData.size()];
			encodedDataAsArray = encodedData.toArray(encodedDataAsArray);
			for(int j = 0; j < encodedDataAsArray1.length; j++){
				encodedDataAsArray1[j] = encodedDataAsArray[j].intValue();
			}
			booleanizedFile.add(encodedDataAsArray1);
			Collections.shuffle(booleanizedFile);
		}
	}
	
	
	/************************************************************
	
	processVoteCount processes the voting dataset. It imputates
	data accordingly. republican = 0, democrat = 1
	
	************************************************************/	
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
		Collections.shuffle(booleanizedFile);
	}
	
	/************************************************************
	
	processGlassID processes all of the data and places it into buckets
	that fit the corresponding value range. It was difficult to fit the
	buckets. Once a proper bucket scheme is found, the error in the 
	winnow2 and naivebayes algos would decrease.
	
	************************************************************/	

	public void processGlassID(){ //Glass has no missing values. No need to imputate.
		for(int i = 0; i < lines.size(); i++){
			ArrayList<Integer> encodedData = new ArrayList<Integer>();
			for(int j = 1; j < lines.get(i).length-1; j++){
				int[] bArray;
				if(j == 1){
					bArray = new int[4];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j])-1.5112)*100));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 2){
					bArray = new int[8];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j])-10.73)));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 3){
					bArray = new int[5];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j]))));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 4){
					bArray = new int[5];
					int index = (int)(Math.round(((Double.parseDouble(lines.get(i)[j])-.29))));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 5){
					bArray = new int[57];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j])-69.81)/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 6){
					bArray = new int[7];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j]))));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 7){
					bArray = new int[13];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j])-5.43)));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 8){
					bArray = new int[4];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j]))));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 9){
					bArray = new int[52];
					int index = (int)(Math.round((Double.parseDouble(lines.get(i)[j])/.01)));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}
			}
			encodedData.add(Integer.parseInt(lines.get(i)[lines.get(i).length-1]));
			encodedData.trimToSize();
			Integer[] encodedDataAsArray = new Integer[encodedData.size()];
			int[] encodedDataAsArray1 = new int[encodedData.size()];
			encodedDataAsArray = encodedData.toArray(encodedDataAsArray);
			for(int j = 0; j < encodedDataAsArray.length; j++){
				encodedDataAsArray1[j] = encodedDataAsArray[j].intValue();
			}
			booleanizedFile.add(encodedDataAsArray1);
		}
		Collections.shuffle(booleanizedFile);
	}
	
	/************************************************************
	
	processIris uses a similar scheme to the GlassID method above.
	However, the buckets are more correct because there is less
	correlation between the classes.
	
	************************************************************/	
	
	public void processIris(){
			
		for(int i = 0; i < lines.size(); i++){
			ArrayList<Integer> encodedData = new ArrayList<Integer>();
			for(int j = 0; j < lines.get(i).length-1; j++){
				int[] bArray;
				if(j == 0){
					bArray = new int[40];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-4)/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 1){
					bArray = new int[25];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-2)/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 2){
					bArray = new int[60];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-1)/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 3){
					bArray = new int[26];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])/.1)));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}
			}
			if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("Iris-setosa")){
				encodedData.add(1);
			}else if(lines.get(i)[lines.get(i).length-1].equalsIgnoreCase("Iris-versicolor")){
				encodedData.add(2);
			}else{
				encodedData.add(3);
			}
			encodedData.trimToSize();
			Integer[] encodedDataAsArray = new Integer[encodedData.size()];
			int[] encodedDataAsArray1 = new int[encodedData.size()];
			encodedDataAsArray = encodedData.toArray(encodedDataAsArray);
			for(int j = 0; j < encodedDataAsArray.length; j++){
				encodedDataAsArray1[j] = encodedDataAsArray[j].intValue();
			}
			booleanizedFile.add(encodedDataAsArray1);
		}
		Collections.shuffle(booleanizedFile);
	}
	
	/************************************************************
	
	processSoyBean stores all of the data for the soybean data.
	the booleanized array is the the max value per attribute(6) 
	plus 1 times 35 attributes and add 1 for the class value.
	
	************************************************************/
	
	
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
		Collections.shuffle(booleanizedFile);
	}
	
	/************************************************************
	
	imputateData fills missing data while maintaining the overall
	probability.
	
	************************************************************/
	
	
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
						if(Integer.parseInt(lines.get(i)[arrayIndex]) >= 5){
							y++; //increment yes count
							continue;
						}else if(Integer.parseInt(lines.get(i)[arrayIndex]) < 5){
							n++; //increment no count
						}
					}
				}
			}
			return (y/count >= n/count) ? 1 : 0; 
		}
		return 0;
	}
	
	public ArrayList<Integer> fillArray(int[] array, ArrayList<Integer> list){
		for(int i = 0; i < array.length; i++){
			list.add(array[i]);
		}
		return list;
	}
	
}
