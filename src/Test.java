import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Test {
	
	static String[] dataTypes = {"SoyBean","Iris","GlassID","BreastCancer","VoteCount"};
	static String[] unProcessedFiles = {"soybeanunprocessed.txt","irisunprocessed.txt", "glassunprocessed.txt", 
			"breast-cancer-wisconsinunprocessed.txt","house-votes-84unprocessed.txt"};
	static String[] processedFiles = {"soybeanprocessed.txt","irisprocessed.txt", "glassprocessed.txt", 
			"breastcancerprocessed.txt","house-votes-84processed.txt"};
	static ArrayList<PreProcessTask> tasks = new ArrayList<PreProcessTask>();
	
	public static void main(String[] args) throws FileNotFoundException {
		processData();
		
			Trainer myTrainer = new Trainer(tasks.get(4));
			try {
				myTrainer.splitData();
				for(int key : tasks.get(4).classes.keySet()){
					myTrainer.winnow2(myTrainer.trainingData, key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			myTrainer.test();
	}
	
	public static void processData() throws FileNotFoundException{
		for(int i = 0 ; i < dataTypes.length; i++){
			PreProcessTask task = new PreProcessTask(dataTypes[i]);
			PrintWriter writer = new PrintWriter(processedFiles[i]);
			try {
				task.storeFileInArray(unProcessedFiles[i]);
				task.booleanizeData();
				for(int j = 0; j < task.booleanizedFile.size();j++){
					for(int k = 0; k < task.booleanizedFile.get(j).length; k++){
						writer.print(task.booleanizedFile.get(j)[k] + " ");
					}
					writer.println();
				}
				tasks.add(task);
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				writer.close();
			}
		}
	}
}
