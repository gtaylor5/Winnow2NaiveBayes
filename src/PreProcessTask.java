import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PreProcessTask {
	
	ArrayList<String[]> lines = new ArrayList<String[]>();
	ArrayList<String[]> linesCopy = new ArrayList<String[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	private String dataSetName = "";
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
		}else if(dataSetName.equalsIgnoreCase("BreastCancer")){
			processBreastCancer();
			numClasses = 2;
		}else if(dataSetName.equalsIgnoreCase("GlassID")){
			processGlassID();
			numClasses = 6;
		}else if(dataSetName.equalsIgnoreCase("Iris")){
			processIris();
			numClasses = 3;
		}else{
			processSoyBean();
			numClasses = 4;
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
					if(Integer.parseInt(lines.get(i)[j]) < 5){
						booleanizedArray[j-1] = 0; //Bucket of values less than 5 is a zero based on cursory glance of data
						continue;
					}
					booleanizedArray[j-1] = 1; //Anything greater than or equal to 5 is a flag.
				}else{
					booleanizedArray[j-1] = imputateData(i,j);
				}
			}
			System.out.println();
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public void processVoteCount(){
		
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[lines.get(i).length];
			for(int j = 0; j < lines.get(i).length; j++){
				if(j == 0){
					if(lines.get(i)[j].equalsIgnoreCase("republican")) {
						booleanizedArray[j] = 0; //republican
						continue;
					}else{
						booleanizedArray[j] = 1; //democrat
					}
				}else {
					if(lines.get(i)[j].equalsIgnoreCase("n")){
						booleanizedArray[j]= 0; // no = 0
					}else if(lines.get(i)[j].equalsIgnoreCase("y")){
						booleanizedArray[j] = 1; // yes = 1
					}else{ //if "?"
						booleanizedArray[j] = imputateData(i, j);
					}
				}
			}
			booleanizedFile.add(booleanizedArray);
		}
	}
	public void processGlassID(){ //Glass has no missing values. No need to imputate.
		
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[(10*8)+1]; //There are 10 buckets that hold integers from 0-99. Ignoring RI values. Last index is class value
			for(int j = 2; j < lines.get(i).length-1; j++){
				for(int k = (j-2)*10; k < ((j+1)-2)*10; k++){
					if((int)(Math.floor(Double.parseDouble(lines.get(i)[j]))%10) == (k-(j-2)*10)){ //use modulus encoding of attributes to make sequences more differentiable
						booleanizedArray[k] = 1;
					}
				}
			}
			booleanizedArray[booleanizedArray.length-1] = Integer.parseInt(lines.get(i)[lines.get(i).length-1]);
			booleanizedFile.add(booleanizedArray);
		}
	}
	
	public void processIris(){
		for(int i = 0; i < lines.size(); i++){
			int[] booleanizedArray = new int[(10*4)+1];
			for(int j = 0; j < lines.get(i).length-1; j++){
				booleanizedArray[(int) (j*10 + Math.round(Double.parseDouble(lines.get(i)[j])))] = 1;
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
}
