import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Trainer {
	
	int numClasses = 0;
	String dataSetName = "";
	ArrayList<double[]> classifiers = new ArrayList<double[]>();
	ArrayList<Integer> classNumbers = new ArrayList<Integer>();
	ArrayList<int[]> trainingData = new ArrayList<int[]>();
	static ArrayList<int[]> testData = new ArrayList<int[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	ArrayList<Integer> chosen = new ArrayList<Integer>();
	PreProcessTask task;
	
	public Trainer(PreProcessTask task){
		this.booleanizedFile = task.booleanizedFile;
		this.numClasses = task.numClasses;
		this.dataSetName = task.dataSetName;
		this.task = task;
	}
	
	public void splitData() throws IOException{
		int originalSize = booleanizedFile.size();
		while((testData.size() + trainingData.size())!=originalSize){
			int randomIndex = new Random().nextInt(booleanizedFile.size());
			if(testData.size() <= (originalSize/3)){
				testData.add(booleanizedFile.get(randomIndex));
				booleanizedFile.remove(randomIndex);
				booleanizedFile.trimToSize();
			}else{
				trainingData.add(booleanizedFile.get(randomIndex));
				booleanizedFile.remove(randomIndex);
				booleanizedFile.trimToSize();
			}
		}
	}
	
	public void test(){
		for(int i = 0; i < testData.size(); i++){
			double theta = testData.get(i).length;
			for(int j = 0; j < classifiers.size(); j++){
				int fcorrect = testData.get(i)[testData.get(i).length-1];
				int factual = (int)(dot(classifiers.get(j), testData.get(i)));
				int h = (factual >= theta) ? 1 : 0;
				if(classNumbers.get(j) == fcorrect){
					System.out.println("Data Set: "+dataSetName);
					System.out.println("Class Number: " + classNumbers.get(j));
					System.out.println("Correct Class Number from Data: " + fcorrect);
					System.out.println("Correct Class from Data: " + task.classes.get(fcorrect));
					System.out.println(h);
					if(dataSetName == "BreastCancer" || dataSetName == "VoteCount"){
						if(h == classNumbers.get(j)){
							System.out.println("The algorithm correctly predicted: " +task.classes.get(classNumbers.get(j)));
							System.out.println();
							break;
						}
					}else if(dataSetName == "Iris" || dataSetName == "SoyBean" || dataSetName == "GlassID"){
						if(h == 1){
							System.out.println("The algorithm correctly predicted: " +task.classes.get(classNumbers.get(j)));
							System.out.println();
							break;
						}
					}
					System.out.println();
				}
			}
		}
	}
	
	public void winnow2(ArrayList<int[]> dataSet, int classNum){
		double theta;
		double alpha;
		if(dataSetName == "VoteCount"){
			theta = dataSet.get(0).length/2;
			alpha = 2;
			
		}else{
			theta = dataSet.get(0).length;
			alpha = 2.5;
		}
		alpha = 2.5;
		dataSet.trimToSize();
		double[] weights = new double[dataSet.get(0).length-1];
		Arrays.fill(weights, 1.0);
		
		for(int i = 0; i < dataSet.size(); i++){
			int f_correct = (dataSet.get(i)[dataSet.get(i).length-1] == classNum) ? 1 : 0;
			int f_actual = (int)(dot(weights, dataSet.get(i)));
			int h = (f_actual > theta) ? 1 : 0;
			if(h == 1 && f_correct == 0){
				weights = demotion(weights, dataSet.get(i), alpha);
			}else if(h == 0 && f_correct == 1){
				weights = promotion(weights, dataSet.get(i), alpha);
			}
		}
		classNumbers.add(classNum);
		classifiers.add(weights);
	}
	
	public double dot(double[] weights, int[] values){
		double sum = 0.0;
			for(int i = 0; i < weights.length-1; i++){
				sum+=weights[i]*values[i];
			}
		return sum;
	}
	
	public double[] demotion(double[] weights, int[] values, double alpha){
		double[] temp = weights;
		for(int i = 0; i < temp.length; i++){
			if(values[i] == 1){
				temp[i] = temp[i]/alpha;
			}
		}
		return temp;
	}
	
	public double[] promotion(double[] weights, int[] values, double alpha){
		
		double[] temp = weights;
		
		for(int i = 0; i < temp.length; i++){
			if(values[i] == 1){
				temp[i] = temp[i]*alpha;
			}
		}
		return temp;
	}
}
