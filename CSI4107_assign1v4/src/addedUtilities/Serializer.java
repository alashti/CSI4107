package addedUtilities;

import ir.vsr.InvertedIndex;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
 
public class Serializer {
 
	InvertedIndex index = null;
	
	public Serializer(InvertedIndex index){
		this.index = index;
	}
 
   public boolean serializeIndex(){
 	   
	   try{
 
		FileOutputStream fout = new FileOutputStream("Util/index.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(index);
		oos.close();
		System.out.println("Succesfully Recorded Object");
		return true;
 
	   }catch(Exception ex){
		     ex.printStackTrace();
		     return false;
	   }
   }
}