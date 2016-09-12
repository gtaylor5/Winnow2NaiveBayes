import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class BayesTrainer {
	
	String dataSetName;
	int numClasses;
	PreProcessTask task;
	
	ArrayList<int[]> trainingData = new ArrayList<int[]>();
	ArrayList<int[]> testData = new ArrayList<int[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	ArrayList<Integer> classNumbers = new ArrayList<Integer>();
	ArrayList<Classification> classifications = new ArrayList<Classification>();
	
	public BayesTrainer(PreProcessTask task){
		this.booleanizedFile = task.booleanizedFile;
		this.numClasses = task.numClasses;
		this.dataSetName = task.dataSetName;
		this.task = task;
	}
	
	/************************************************************
	
	Train naive bayes takes in a training set and a class number
	and trains according to the naivebayes algorithm. It first
	sets the probability of the class then sets the probability
	for each attribute in that class. Classifications are added
	to arraylist.
	
	************************************************************/
	
	public void trainNaiveBayes(ArrayList<int[]> trainSet, int classNum){
		Classification _class = new Classification(trainSet, classNum);
		_class.setClassProbability();
		_class.setAttributeProbabilities();
		classifications.add(_class);
		classNumbers.add(classNum);
	}
	
	/************************************************************
	
	method tests naive bayes algorithm.
	 * @throws IOException 
	
	************************************************************/
	
	public void testNaiveBayes(ArrayList<int[]> testSet) throws IOException{
		PrintWriter writer = new PrintWriter(dataSetName+"Bayes.txt");
		PrintWriter resultsWriter = new PrintWriter(new FileWriter(Main.results, true)); 
		ArrayList<Double> values = new ArrayList<Double>();
		for(int i = 0; i < classifications.size(); i++){
			writer.println("Classification function for: "+task.classes.get(classNumbers.get(i)));
			for(int j = 0; j < classifications.get(i).attributeProbabilities.length; j++){
				writer.print(classifications.get(i).attributeProbabilities[j]+", ");
			}
			writer.println();
			writer.println();
		}
		double count = 0;
		double totalCount = 0;
		for(int i = 0; i < testSet.size(); i++){
			double max = Double.MIN_VALUE;
			int maxIndex = 0;
			for(int j = 0; j < classifications.size(); j++){
				double value = classifications.get(j).classProbability;
				for(int k = 0; k < classifications.get(j).attributeProbabilities.length; k++){
					if(testSet.get(i)[k] == 1){
						value*= classifications.get(j).attributeProbabilities[k];
					}else{
						value*= (1.0-classifications.get(j).attributeProbabilities[k]);
					}
				}
				if(value >= max){
					max = value;
					maxIndex = j;
				}
			}
			if(classNumbers.get(maxIndex) == testSet.get(i)[testSet.get(i).length-1]){
				writer.println("CORRECT: "+task.classes.get(classNumbers.get(maxIndex)) + " = " + task.classes.get(testSet.get(i)[testSet.get(i).length-1]));
				count++;
				totalCount++;
			}else{
				writer.println("INCORRECT: "+task.classes.get(classNumbers.get(maxIndex)) + " != " + task.classes.get(testSet.get(i)[testSet.get(i).length-1]));
				totalCount++;
			}
		}
		
		resultsWriter.printf("%s%s : %.2f%s","Bayes ",dataSetName, (count/totalCount)*100," %");
		resultsWriter.println();
		writer.close();
		resultsWriter.close();
	}
}


