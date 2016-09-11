import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataProcessor {
	
	String[] dataSets = {"SoyBean","Iris","GlassID","BreastCancer","VoteCount"};
	String[] unProcessedFiles = {"soybeanunprocessed.txt","irisunprocessed.txt", "glassunprocessed.txt", 
			"breast-cancer-wisconsinunprocessed.txt","house-votes-84unprocessed.txt"};
	String[] processedFiles = {"soybeanprocessed.txt","irisprocessed.txt", "glassprocessed.txt", 
			"breastcancerprocessed.txt","house-votes-84processed.txt"};
	ArrayList<PreProcessTask> tasks = new ArrayList<PreProcessTask>();
	
	/************************************************************
	
	processData() takes the unprocessed files from the unProcessedFiles
	array and processes them into binary arrays for each data entry and
	stores them in the corresponding processedFiles array element.
	 * @throws IOException 
	
	************************************************************/
	public void processData() throws IOException{
		for(int i = 0 ; i < dataSets.length; i++){
			PreProcessTask task = new PreProcessTask(dataSets[i]);
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
				e.printStackTrace();
			}finally{
				writer.close();
			}
		}
	}
}
