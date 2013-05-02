import java.io.File;
import java.io.IOException;

import ir.vsr.InvertedIndex;
import addedUtilities.DeSerializer;
import addedUtilities.Serializer;

public class SearchEngine {
	
	
	public static void main(String [] args){
		
		InvertedIndex index = null;
		File f = new File("en");
		File in = new File("Util");
		
		File[] indexed = null;
		indexed = in.listFiles();
		
		//Added by Victor Buchanan This method checks if there is a file representaiton of the object, if so use it instead of rebuilding index from scratch
		
		if(indexed!=null){
			for(int i=0; i<indexed.length;i++){
				if(indexed[i].getName().equals("index1.ser")){
					index = new DeSerializer().deserialzeInvertedIndex(indexed[i].getName());
					break;
				}else {
					index = new InvertedIndex(f,Short.valueOf("1"),true,true);
					Serializer S = new Serializer(index);
					S.serializeIndex();
					break;
				}
			}
		} else {
			index = new InvertedIndex(f,Short.valueOf("1"),true,true);
			Serializer S = new Serializer(index);
			S.serializeIndex();
		}
		
		System.out.println(index.size());
	
		for(int i=0; i<indexed.length;i++){
			if(indexed[i].getName().equals("TestQueries.txt")){
			System.out.println("Processing queries");
			index.processQueries(indexed[i]);
		}
			try {
				
				Evaluation eval = new Evaluation();
				for (int ii = 0; ii < 50 ; ii++){
					System.out.println("Query #"+ (ii+1)+ ": F-meausre = " + eval.fMeasure[i]);
				}
				System.out.println("Average F-meausre: "+ eval.avgFMeasure);
				System.out.println("Evaluation COMPLETE");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}

	}
		
}
	

}

