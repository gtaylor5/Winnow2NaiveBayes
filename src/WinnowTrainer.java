import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class WinnowTrainer {
	
	double theta;
	PrintWriter writer;
	String dataSetName = "";
	int numClasses;
	
	ArrayList<double[]> classifiers = new ArrayList<double[]>();
	ArrayList<Integer> classNumbers = new ArrayList<Integer>();
	ArrayList<int[]> trainingData = new ArrayList<int[]>();
	ArrayList<int[]> testData = new ArrayList<int[]>();
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	ArrayList<int[]> copyFile;
	
	PreProcessTask task;
	
	/************************************************************
	
	Constructor. This method takes in a PreProcessTask and sets
	the coorresponding attributes.
	
	************************************************************/
	public WinnowTrainer(PreProcessTask task){
		this.booleanizedFile = task.booleanizedFile;
		this.numClasses = task.numClasses;
		this.dataSetName = task.dataSetName;
		this.task = task;
	}
	
	/************************************************************
	
	splitData() takes the booleanizedFile and splits it randomly.
	It splits the data such that 2/3s of the data is for training
	and 1/3 is used for testing. Each time split is run, new 
	training and tests sets are created. This helps ensure that 
	there is a random distribution of classes in each of the sets.
	
	************************************************************/
	
	public void splitData() throws IOException{
		int originalSize = booleanizedFile.size();
		if(dataSetName.equalsIgnoreCase("GlassID")){
			for(int i = 0; i < task.classSizes.length;i++){
				int count = 0;
				while(count < task.classSizes[i]/3){
					int randomIndex = new Random().nextInt(booleanizedFile.size());
					switch(i){
					case 0:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 1){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					case 1:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 2){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					case 2:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 3){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					case 3:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 5){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					case 4:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 6){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					case 5:
						if(booleanizedFile.get(randomIndex)[booleanizedFile.get(randomIndex).length-1] == 7){
							testData.add(booleanizedFile.get(randomIndex));
							booleanizedFile.remove(randomIndex);
							booleanizedFile.trimToSize();
							count++;
							break;
						}else{
							continue;
						}
					}
				}
			}
			Collections.shuffle(testData);
			while(booleanizedFile.size() != 0){
				int randomIndex = new Random().nextInt(booleanizedFile.size());
				trainingData.add(booleanizedFile.get(randomIndex));
				booleanizedFile.remove(randomIndex);
				booleanizedFile.trimToSize();
			}
			return;
		}
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
		booleanizedFile = copyFile;
	}
	
	
	/************************************************************
	
	testWinnow2 tests the classifiers that were created in the
	winnow2() method against the test dataset, testData. The 
	results are printed to a file named dataSetName+ResultsWinnow2.txt
	The first double nested for-loop prints the learned models for
	each class at the to of the file as a comma separated array.
	
	The next double-nested for-loop operates as follows:
	
	The outer for loop, loops overall the enteries in the dataSet.
	
	The inner loop iterates over all of the classifier functions
	created in the winnow2 method.
	
	fcorrect is either 1 or 0 depending on if the classNumber of 
	the classifier matches the class number from the data.
	
	factual returns the dot product of the current classifier and
	the current data entry.
	
	h is 1 if factual is greater than theta and 0 otherwise.
	
	The remainder of the method simply prints if the algorithm
	correctly predicted the correct outcome based on the combinations
	of fcorrect and h.
	
	************************************************************/
	
	public void testWinnow2() throws FileNotFoundException{
		String fileText = dataSetName + "ResultsWinnow2.txt";
		writer = new PrintWriter(fileText);
		for(int i = 0; i < classifiers.size(); i++){
			writer.println("The following is the classifier function for: " + task.classes.get(classNumbers.get(i)));
			for(int j = 0; j < classifiers.get(i).length; j++){
				if(j == 0){
					writer.print("["+(int)classifiers.get(i)[j] + ",");
				}else if(j == classifiers.get(i).length-1){
					writer.print((int)classifiers.get(i)[j] + "]");
					writer.println();
				}else{
					writer.print((int)classifiers.get(i)[j] + ",");
				}
			}
			writer.println();
		}
		for(int i = 0; i < testData.size(); i++){
			for(int j = 0; j < classifiers.size(); j++){
				int fcorrect = (testData.get(i)[testData.get(i).length-1] == classNumbers.get(j)) ? 1 : 0;
				int factual = (int)(dot(classifiers.get(j), testData.get(i)));
				int h = (factual > theta) ? 1 : 0;
				if(dataSetName.equalsIgnoreCase("GlassID")){
					writer.println(factual);
					writer.println(theta);
				}
				if(h == fcorrect && fcorrect == 1){
					writer.println("Data Set: "+dataSetName);
					writer.println("Class Number being tested: " + classNumbers.get(j));
					writer.println("Correct Class Number from Data: " + testData.get(i)[testData.get(i).length-1]);
					writer.println("Correct Class from Data: " + task.classes.get(testData.get(i)[testData.get(i).length-1]));
					writer.println("Winnow2 tested the classifier for " + task.classes.get(classNumbers.get(j)) + " which resulted in h(x) = " + h + 
							".");
					writer.println("Since the current class and the correct class match, "
							+ "fcorrect = 1. Therefore, the algorithm correctly predicted that "+task.classes.get(classNumbers.get(j))+ " was correct.");
					writer.println();
				}else if(h == fcorrect && fcorrect == 0){
					writer.println("Data Set: "+dataSetName);
					writer.println("Current class number being tested: " + classNumbers.get(j));
					writer.println("Correct Class Number from Data: " + testData.get(i)[testData.get(i).length-1]);
					writer.println("Correct Class from Data: " + task.classes.get(testData.get(i)[testData.get(i).length-1]));
					writer.println("Winnow2 tested the classifier for " + task.classes.get(classNumbers.get(j)) + " which resulted in h(x) = " + h + 
							".");
					writer.println("Since the current class and the correct class do not match, "
							+ "fcorrect = 0. Therefore, the algorithm correctly predicted that "+ task.classes.get(classNumbers.get(j))+ " was incorrect.");
					writer.println();
				}else if(h == 1 && fcorrect == 0){
					writer.println("Data Set: "+dataSetName);
					writer.println("Current class number being tested: " + classNumbers.get(j));
					writer.println("Correct Class Number from Data: " + testData.get(i)[testData.get(i).length-1]);
					writer.println("Correct Class from Data: " + task.classes.get(testData.get(i)[testData.get(i).length-1]));
					writer.println("Winnow2 tested the classifier for " + task.classes.get(classNumbers.get(j)) + " which resulted in h(x) = " + h + 
							".");
					writer.println("Since the current class and the correct class do not match, "
							+ "fcorrect = 0. Therefore, the algorithm incorrectly predicted that "+task.classes.get(classNumbers.get(j))+ " was correct.");
					writer.println();
				}else if(h == 0 && fcorrect == 1){
					writer.println("Data Set: "+dataSetName);
					writer.println("Current class number being tested: " + classNumbers.get(j));
					writer.println("Correct Class Number from Data: " + testData.get(i)[testData.get(i).length-1]);
					writer.println("Correct Class from Data: " + task.classes.get(testData.get(i)[testData.get(i).length-1]));
					writer.println("Winnow2 tested the classifier for " + task.classes.get(classNumbers.get(j)) + " which resulted in h(x) = " + h + 
							".");
					writer.println("Since the current class and the correct class match, "
							+ "fcorrect = 1. Therefore, the algorithm incorrectly predicted that "+task.classes.get(classNumbers.get(j))+ " was incorrect.");
					writer.println();
				}
				
				if(j == classifiers.size()-1){
					writer.println("*****************************************");
				}
			}
		}
		writer.close();
	}
	
	/************************************************************
	
	winnow2 is the method used to generate the classifiers used
	to predict the outcome given a particular dataSet.
	
	alpha is set to 2. 3 for GlassID.
	
	numIterations is how many times we want the algorithm to run.
	Think of this as "practice makes perfect".
	
	theta is set to the length of the attributes array.
	
	************************************************************/
	public void winnow2(ArrayList<int[]> dataSet, int classNum){
		double alpha = 2;
		theta = dataSet.get(0).length;
		int numIterations = 50;
		if(dataSetName.equalsIgnoreCase("GlassID")){
			alpha = 2;
			theta = dataSet.size()/3;
			numIterations = 100;
		}
		dataSet.trimToSize();
		double[] weights = new double[dataSet.get(0).length-1];
		Arrays.fill(weights, 1.0); //initialize weights to all 1s.
		for(int j = 0; j < numIterations; j++){
			for(int i = 0; i < dataSet.size(); i++){
				int f_correct = (dataSet.get(i)[dataSet.get(i).length-1] == classNum) ? 1 : 0; //set fcorrect
				int f_actual = (int)(dot(weights, dataSet.get(i))); // calculate factual
				int h = (f_actual > theta) ? 1 : 0; //set h
				if(h == 1 && f_correct == 0){ //demotion case
					weights = demotion(weights, dataSet.get(i), alpha);
				}else if(h == 0 && f_correct == 1){ //promotion case
					weights = promotion(weights, dataSet.get(i), alpha);
				}
			}
		}
		//add classNum and weights to respective arraylists.
		classNumbers.add(classNum);
		classifiers.add(weights);
	}

	
	/************************************************************
	
	dot() returns the dot product of two integer arrays. In this
	particular case, since the class value is the last elements 
	of the data arrays, we have to ignore the last element of the
	array. Hence that's why we iterate from 0 to length-1. In 
	the context of this assignment the dot product is the value 
	for f_actual which is then in turn used to calculate h(x) given
	a value theta.
	
	************************************************************/
	
	public double dot(double[] weights, int[] values){
		double sum = 0.0;
			for(int i = 0; i < values.length-1; i++){
				sum+=weights[i]*values[i];
			}
		return sum;
	}
	
	/************************************************************
	
	demotion is used to update the weights based on an incorrect
	guess (h(x). The method takes in the weights, values and the
	value for alpha. The only weights values that are demoted are 
	those corresponding to a 1 in the values array. This is per
	the winnow2 algorithm for demotion. The weights are demoted
	by dividing the correct value by alpha. The updated weights 
	are then returned.
	
	************************************************************/
	
	public double[] demotion(double[] weights, int[] values, double alpha){
		double[] temp = weights;
		for(int i = 0; i < temp.length-1; i++){
			if(values[i] == 1){
				temp[i] = temp[i]/(alpha);
			}
		}
		return temp;
	}
	
	/************************************************************
	
	promotion is used to update the weights based on an incorrect
	guess (h(x)). The method takes in the weights, values and the
	value for alpha. The only weights values that are promoted are 
	those corresponding to a 1 in the values array. This is per
	the winnow2 algorithm for promotion. The weights are promoted
	by multiplying the correct value by alpha. The updated weights 
	are then returned.
	
	************************************************************/
	
	public double[] promotion(double[] weights, int[] values, double alpha){
		
		double[] temp = weights;
		
		for(int i = 0; i < temp.length-1; i++){
			if(values[i] == 1){
				temp[i] = temp[i]*alpha;
			}
		}
		return temp;
	}
}
