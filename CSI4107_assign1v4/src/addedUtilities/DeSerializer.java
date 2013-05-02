package addedUtilities;

import ir.vsr.InvertedIndex;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
 
public class DeSerializer{
 
InvertedIndex index = null;
	
  public InvertedIndex deserialzeInvertedIndex(String fileName){
 
 
	   try{
 
		   FileInputStream fin = new FileInputStream("Util/index.ser");
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   index = (InvertedIndex) ois.readObject();
		   ois.close();
		   System.out.println("Successfully imported index of Size: "+ index.size());
		   return index;
 
	   }catch(Exception ex){
		   ex.printStackTrace();
		   return null;
	   } 
   } 
}