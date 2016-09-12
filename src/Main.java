import java.io.File;
import java.io.IOException;

public class Main {
	/************************************************************
	
	Main method to run the whole program.
	
	************************************************************/
	static File results;
	public static void main(String[] args) throws IOException {
		results = new File("Results.txt");
		DataProcessor processor = new DataProcessor();
		processor.processData();
		for(int i = 0; i < processor.dataSets.length; i++){
			WinnowTrainer myTrainer = new WinnowTrainer(processor.tasks.get(i)); // enter a number 0 through 4 to run the algorithm for the corresponding dataSet
			BayesTrainer bTrainer = new BayesTrainer(processor.tasks.get(i));
			myTrainer.splitData();
			for(int key : processor.tasks.get(i).classes.keySet()){ // enter a number 0 through 4 to run the algorithm for the corresponding dataSet (numbers must match)
				myTrainer.winnow2(myTrainer.trainingData, key);
				bTrainer.trainNaiveBayes(myTrainer.trainingData, key);
			}
			bTrainer.testNaiveBayes(myTrainer.testData);
			myTrainer.testWinnow2();
		}
	}
}
