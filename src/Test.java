import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {
	
	static String[] dataTypes = {"SoyBean","Iris","GlassID","BreastCancer","VoteCount"};
	static String[] unProcessedFiles = {"soybeanunprocessed.txt","irisunprocessed.txt", "glassunprocessed.txt", 
			"breast-cancer-wisconsinunprocessed.txt","house-votes-84unprocessed.txt"};
	static String[] processedFiles = {"soybeanprocessed.txt","irisprocessed.txt", "glassprocessed.txt", 
			"breastcancerprocessed.txt","house-votes-84processed.txt"};
	
	public static void main(String[] args) throws FileNotFoundException {
		
		processData();
		Trainer myTrainer = new Trainer("glassprocessed.txt",6,"GlassID");
		try {
			myTrainer.splitData();
			myTrainer.winnow2(myTrainer.trainingData, 1);
			//for(int i = 0; i < myTrainer.classifiers.get(0).length; i++){
				//System.out.print(myTrainer.classifiers.get(0)[i] + " ");
			//}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				writer.close();
			}
		}
	}
	
}
