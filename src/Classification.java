import java.util.ArrayList;

public class Classification {
	
	int classNumber;
	double classProbability;
	double[] attributeProbabilities;
	int classFrequency;
	int dataSetCount;
	
	ArrayList<int[]> booleanizedFile = new ArrayList<int[]>();
	
	public Classification(ArrayList<int[]> booleanizedFile, int classNum){
		this.booleanizedFile = booleanizedFile;
		this.classNumber = classNum;
	}
	
	/************************************************************
	
	Sets class probability based on frequency of occurence.
	
	************************************************************/
	
	void setClassProbability(){
		for(int i = 0; i < booleanizedFile.size();i++){
			if(booleanizedFile.get(i)[booleanizedFile.get(i).length-1] == classNumber){
				classFrequency++;
			}
		}
		classProbability = (double)(classFrequency/((double)booleanizedFile.size()));
	}
	
	/************************************************************
	
	Sets attribute probability based on the frequency of occurence
	in a particular class. 0 probabilites are handle with the
	m-estimate technique.
	
	************************************************************/
	
	void setAttributeProbabilities(){
		double m = 1.0;
		double p = .001;
		attributeProbabilities = new double[booleanizedFile.get(0).length-1];
		for(int i = 0; i < attributeProbabilities.length; i++){
			int attributeCount = 0;
			for(int j = 0; j < booleanizedFile.size(); j++){
				if(booleanizedFile.get(j)[i] == 1 && booleanizedFile.get(j)[booleanizedFile.get(j).length-1] == classNumber){
					attributeCount++;
				}
			}
			attributeProbabilities[i] = ((double)(attributeCount/((double)classFrequency)) == 0) ? (double)((attributeCount+(m*p))/((double)(classFrequency+1))) : (double)(attributeCount/((double)classFrequency));
		}
	}
}
