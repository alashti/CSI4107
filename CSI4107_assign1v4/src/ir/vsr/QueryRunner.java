package ir.vsr;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class QueryRunner {
	
	LinkedList<String> queries;
	File qFile;
	BufferedReader reader;
	
	public QueryRunner(File f){
		queries = new LinkedList<String>();
		this.qFile = f;
		getQueries();
	}
	
	public LinkedList<String> getQueryList(){
		return this.queries;
	}

	void getQueries(){
		try {
			reader = new BufferedReader(new FileReader(qFile.getPath()));
			String q;
			while((q = reader.readLine()) != null){
				queries.add(q);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
