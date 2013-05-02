import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


/**
 * 
 * This class evaluates the precision and recall for every query.
 *
 */

public class Evaluation {
	private String relevenceJudgementFilePath = "Util\\RelevenceJudgment.txt";//path for expected results file
	private String resultsFilePath = "Util\\Results.txt";//path for  results file
	private double[] presicion;
	private double[] recall;
	private double[] totalReleventDocuments;
	private double[] totalRetreivedDocumetns;
	public double[] fMeasure;
	public double avgFMeasure = 0;
	private LinkedList<String[]> rel = new LinkedList<String[]>();
	private LinkedList<String[]> res = new LinkedList<String []>();
	
	
	//constructor
	public Evaluation() throws IOException{
		System.out.println("Begin Evaluation");
		this.presicion = new double[50];
		this.recall = new double[50];
		this.totalReleventDocuments = new double[50];
		this.totalRetreivedDocumetns = new double[50];
		this.fMeasure = new double[50];
		for(int i = 0; i < 50; i++){
			this.presicion[i] = 0;
			this.recall[i] = 0;
			this.totalReleventDocuments[i] = 0;
			this.totalRetreivedDocumetns[i] = 0;
			this.fMeasure[i] = 0;
		}
		this.initTotalReleventDocuments();
		this.initResults();
		this.initPrecisionandRecall();
		this.calculatedFmeasure();
	}
	
	//Process expected results
	private void initTotalReleventDocuments() throws IOException{
		BufferedReader in1 = new BufferedReader(new FileReader(this.relevenceJudgementFilePath));
		String line = "";
		String [] rel = new String[2];
		Integer index;
		
		do{
			line = in1.readLine();
			if(line == null){
				break;
			}
			rel = line.split("\\s+");
			index = new Integer(rel[0].substring(rel[0].length()-2));//count total expected retrievals for each query
			this.rel.add(rel);
			this.totalReleventDocuments[index-1]++;
		}while(true);
	}
	//proccess results file
	private void initResults() throws IOException{
		BufferedReader in1 = new BufferedReader(new FileReader(this.resultsFilePath));
		String line = "";
		String [] res = new String[6];
		Integer index;
		do{
			line = in1.readLine();
			if(line == null){
				break;
			}
			res = line.split("\\s+");
			index = new Integer(res[0].substring(res[0].length()-2));
			this.res.add(res);
			this.totalRetreivedDocumetns[index-1]++;//count total retrievals in each query run
		}while(true);
	}
	
	//calculate precision and recall
	private void initPrecisionandRecall(){
		String id;
		Integer index = 1;
		for(int i = 0; i < res.size(); i++){
			id = res.get(i)[0];
			index = new Integer(id.substring(id.length()-2));
			for(int j = 0; j< rel.size(); j++){
				if(this.res.get(i).equals(this.rel.get(j))){
					if(this.res.get(i)[2].equals(this.rel.get(j)[1])){
						this.recall[index.intValue()-1]++;
						this.presicion[index.intValue()-1]++;
					}
				}
			}
		}
		for(int i = 0; i< 50; i++){
			this.presicion[i] = this.presicion[i] / this.totalRetreivedDocumetns[i];
			this.recall[i] = this.recall[i] / this.totalReleventDocuments[i];
		}
	}
	
	//calculate f-measure
	private void calculatedFmeasure(){
		for (int i = 0; i < 50 ; i++){
			if(this.presicion[i] == 0 || this.recall[i] == 0){
				this.fMeasure[i] = 0;
			}else{
				this.fMeasure[i] = 2 / ((1/this.presicion[i]) + (1/this.recall[i]));
			}
			this.avgFMeasure = this.avgFMeasure + this.fMeasure[i];
		}
		this.avgFMeasure = this.avgFMeasure / 50;
	}
	
	
	
	//main used for test cases
	public static void main(String [] args){
		try {
			Evaluation e = new Evaluation();
			for (int i = 0; i < 50 ; i++){
				System.out.println("Query #"+ (i+1)+ ": F-meausre = " + e.fMeasure[i]);
			}
			System.out.println("Average F-meausre: "+ e.avgFMeasure);
			System.out.println("Evaluation COMPLETE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
