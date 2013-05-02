package ir.csi4107;
import java.io.File;
import java.io.Serializable;

import java.util.LinkedList;


/**
 * 
 * Representation of tag, vocabulary word, and xml file.  This is used for add weights to tokens in the index.
 *
 */

public class FileData implements Comparable<FileData>, Serializable{
	private File file;
	private String path;
	private String tag;
	private String vocabularyWord;
			
	public FileData(File f, String p, String t, String v){
		this.file = f;
		this.path = p;
		this.tag = t;
		this.vocabularyWord = v;
	}
		
	public File getFile(){
		return this.file;
	}
		
	public String getPath(){
		return this.path;
	}
				
	public String getTag(){
		return this.tag;
	}

	public String getVocabularyWord(){
		return this.vocabularyWord;
	}

	public int compareTo(FileData f) {
		String v1 = this.vocabularyWord;
		String v2 = f.vocabularyWord;
	
		return v1.compareTo(v2);
	}
	
	public String toString(){
		return vocabularyWord +"	"+tag+"		"+ path;
	}
}
	