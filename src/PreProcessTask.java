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
			ArrayList<Integer> encodedData = new ArrayList<Integer>(); //ignoring patient ID
			for(int j = 1; j < lines.get(i).length; j++){
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
		for(int i = 0; i < lines.size(); i++){
			ArrayList<Integer> encodedData = new ArrayList<Integer>();
			for(int j = 1; j < lines.get(i).length-1; j++){
				int[] bArray;
				if(j == 1){
					bArray = new int[4];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-1.5)*100));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 2){
					bArray = new int[740];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-10)/.01));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 3){
					bArray = new int[45];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j]))/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 4){
					bArray = new int[36];
					int index = (int)(Math.floor(((Double.parseDouble(lines.get(i)[j])-.29)/.1)));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 5){
					bArray = new int[65];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-69)/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 6){
					bArray = new int[63];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j]))/.1));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 7){
					bArray = new int[23];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-5)/.5));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 8){
					bArray = new int[316];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j]))/.01));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 9){
					bArray = new int[52];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j]))/.01));
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
	}
	
	public void processIris(){
			
		for(int i = 0; i < lines.size(); i++){
			ArrayList<Integer> encodedData = new ArrayList<Integer>();
			for(int j = 0; j < lines.get(i).length-1; j++){
				int[] bArray;
				if(j == 0){
					bArray = new int[400];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-4)/.01));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 1){
					bArray = new int[250];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-2)/.01));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if(j == 2){
					bArray = new int[600];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])-1)/.01));
					bArray[index] = 1;
					encodedData = fillArray(bArray, encodedData);
				}else if (j == 3){
					bArray = new int[260];
					int index = (int)(Math.floor((Double.parseDouble(lines.get(i)[j])/.01)));
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
