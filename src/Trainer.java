import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Trainer {
	
	File file;
	int numClasses = 0;
	String dataSetName = "";
	ArrayList<double[]> classifiers = new ArrayList<double[]>();
	ArrayList<int[]> trainingData = new ArrayList<int[]>();
	ArrayList<int[]> testData = new ArrayList<int[]>();
	ArrayList<String> dataAsString = new ArrayList<String>();
	
	public Trainer(String fileLocation, int numberOfClasses, String dataSetName){
		this.file = new File(fileLocation);
		this.numClasses = numberOfClasses;
		this.dataSetName = dataSetName;
	}
	
	public void splitData() throws IOException{
		int length = 0;
		int lineLength = 0;
		Scanner lineReader;
		Scanner reader = new Scanner(file);
		while(reader.hasNextLine()){
			String[] line = reader.nextLine().split(" ");
			line = line.trim();
			line = line.replaceAll(" ", "");
			length++;
			dataAsString.add(line);
			System.out.println(dataAsString.get(0).length());
			lineReader = new Scanner(line);
			while(lineReader.hasNextInt()){
				lineLength++;
				lineReader.nextInt();
			}
		}
		
		for(int i = 0; i < length; i++){
			ArrayList<Integer> chosen = new ArrayList<Integer>();
			int[] trainingDataArray = new int[lineLength];
			int[] testDataArray = new int[lineLength];
			if(i < (int)((2*length)/3)){
				int index = (int)(Math.random()*length);
				while(chosen.contains(index)){
					index = (int)(Math.random()*length);
				}
				chosen.add(index);
				lineReader = new Scanner(dataAsString.get(index));
					int j = 0;
					while(lineReader.hasNextInt()){
						trainingDataArray[j] = lineReader.nextInt();
						j++;
					}
				trainingData.add(trainingDataArray);
				trainingData.trimToSize();
			}else{
				
				int index = (int)(Math.random()*length);
				while(chosen.contains(index)){
					index = (int)(Math.random()*length);
				}
				chosen.add(index);
				lineReader = new Scanner(dataAsString.get(index));
				for(int j = 0; j < lineLength; j++){
					while(lineReader.hasNextInt()){
						testDataArray[j] = lineReader.nextInt();
					}
				}
				testData.add(trainingDataArray);
			}
		}
	}
	
	public void winnow2(ArrayList<int[]> dataSet, int classNum){
		double theta = 1;
		int alpha = 2;
		dataSet.trimToSize();
		System.out.println(dataSet.get(0).length);
		double[] weights = new double[dataSet.get(0).length];
		System.out.println(weights.length);
		Arrays.fill(weights, 1.0);
		for(int i = 0; i < dataSet.size(); i++){
			int f_correct = (dataSet.get(i)[dataSet.get(i).length-1] == classNum) ? 1 : 0;
			int f_actual = (int)(dot(weights, dataSet.get(i)));
			int h = (f_actual > theta) ? 1 : 0;
			//System.out.println(h + " " + f_correct);
			if(h == 1 && f_correct == 0){
				weights = demotion(weights, dataSet.get(i), alpha);
			}else if(h == 0 && f_correct == 1){
				weights = promotion(weights, dataSet.get(i), alpha);
			}
		}
		classifiers.add(weights);
	}
	
	public double dot(double[] weights, int[] values){
		double sum = 0.0;
		for(int i = 0; i < weights.length; i++){
			sum+=weights[i]*values[i];
		}
		return sum;
	}
	
	public double[] demotion(double[] weights, int[] values, int alpha){
		double[] temp = weights;
		for(int i = 0; i < temp.length; i++){
			if(values[i] == 1){
				temp[i] = temp[i]/alpha;
			}
		}
		return temp;
	}
	
	public double[] promotion(double[] weights, int[] values, int alpha){
		
		double[] temp = weights;
		
		for(int i = 0; i < temp.length; i++){
			if(values[i] == 1){
				temp[i] = temp[i]*alpha;
			}
		}
		return temp;
	}
}
