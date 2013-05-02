package addedUtilities;

import java.io.File;
import java.util.LinkedList;

public class DirectoryReader {
	  LinkedList<File> fileList = null;
	  int spc_count=-1;

	  public DirectoryReader(){
		  fileList = new LinkedList<File>();
	  }
	  
	  public void Process(File aFile) {
	    spc_count++;
	    String spcs = "";
	    for (int i = 0; i < spc_count; i++)
	      spcs += " ";
	    
	    if(aFile.isFile()){
	      fileList.add(aFile);
	      
	    }else if (aFile.isDirectory()) {
	      File[] listOfFiles = aFile.listFiles();
	      if(listOfFiles!=null) {
	        for (int i = 0; i < listOfFiles.length; i++)
	          Process(listOfFiles[i]);
	      } else {
	        System.out.println(spcs + " [ACCESS DENIED]");
	      }
	    }
	    spc_count--;
	  }
	  
	  public LinkedList<File> getFileList(){
		  return fileList;
	  }

	}