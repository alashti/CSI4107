package ir.csi4107;
import ir.utilities.Porter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * 
 * This class is used to parse DOM files of the xml files to associate a tag, vocabulary word, and xml file path.
 * This class will be used to add weights relative to tags where tokens are found in the xml file
 * Implemented by Carol-Ann Renaud
 *
 */

public class Parser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8229865478717346048L;
	private LinkedList<File> documentPathList;
	private LinkedList<FileData> data;
	
	//CONSTRUCTOR
	public Parser(String rootFolderPath){
		this.documentPathList = new LinkedList<File>();
		this.initDocumentPathList(rootFolderPath);
		this.data = new LinkedList<FileData>();
	}
	
	
	//initialize the list of paths for every xml file to be parsed.
	void initDocumentPathList(String pathName){
		File dir = new File(pathName);
		System.out.println(dir.listFiles());
		File listdir[] = dir.listFiles();
		
		for(int i = 0; i < listdir.length; i++){
			if(listdir[i].isDirectory()){
				initDocumentPathList(pathName + "\\" +listdir[i].getName());
			}else if(listdir[i].isFile()){
				this.documentPathList.add(listdir[i]);
			}
		}
		
	}
	


	//Reformat path string to have proper format
	public String getPath(File f){
		String s = f.getPath();
		String newString = "";
		for(int i = 0;  i < s.length(); i++){
			if(s.charAt(i) == '\\'){
				newString = newString.concat("\\\\");
			}else{
				newString = newString.concat(s.substring(i, i+ 1));
			}
		}
		return newString;
	}
	
	//create DOm object to be travered from a string which represents
	public void paseFile() throws JDOMException, IOException{
		File f = this.documentPathList.pop();
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(f);
		Element root = document.getRootElement();
		this.parseFile(root, f);
	}
	
	//associate a tag with a vocabulary word for every xml file
	private void parseFile(Element e, File f){
		StringTokenizer st;
		List<Element> children = e.getChildren();
		String tagName = e.getName();
		String elementText = e.getTextNormalize().toLowerCase();
		String nextToken;
		Porter p = new Porter();
		
		if(children.isEmpty() && e.getTextTrim().length()>1){			
			st = new StringTokenizer(elementText, " 	,.<>?;:'\"[]{}\\+=-_)(*&^%$#@!|1234567890°/");
			while(st.hasMoreTokens()){
				nextToken = st.nextToken().toString();
				nextToken = p.stripAffixes(nextToken);
				this.updateFileData(f, f.getPath(), tagName, nextToken);
			}
		}else{
			while(!children.isEmpty()){
				parseFile(children.remove(0), f);
			}
		}
	}
	
	//add associated FileData object 
	private void updateFileData(File file, String path, String tag, String vocabularyWord){
		FileData fData = new FileData(file, path, tag, vocabularyWord);
		this.data.add(fData);
	}
	
	//estimate weight of tag.  find all X vocabulary word, and pick the highest weight found
	public int getWeight(String word, String path){
		int weight = 1;
		LinkedList<String> list = new LinkedList<String>();
		int i = 0;
		
		while (i < this.data.size()){
			if(word.compareTo(this.data.get(i).getVocabularyWord())==0 && path.compareTo(this.data.get(i).getPath())==0){
				list.add(this.data.remove(i).getTag());
			}
			i++;
		}
		
		while(!list.isEmpty()){
			String tag = list.pop();
			int temp = 0 ;
			if(tag.equals("title")){
				temp = 10;
			}else if(tag.equals("h1")){
				temp = 9;
			}else if(tag.equals("h2")){
				temp = 8;
			}else if(tag.equals("h3")){
				temp = 8;
			}else if(tag.equals("h4")){
				temp = 7;
			}else if(tag.equals("h5")){
				temp = 6;
			}else if(tag.equals("h6")){
				temp = 6;
			}else if(tag.equals("h7")){
				temp = 5;
			}else if(tag.equals("li")){
				temp = 4;
			}else if(tag.equals("li")){
				temp = 3;
			}else if(tag.equals("ol")){
				temp = 2;
			}else if(tag.equals("p")){
				temp = 1;
			}else if(tag.equals("span")){
				temp = 1;
			}
		
			weight = Math.max(weight, temp);
		}
		
		return weight;
	}
}

		
		
		
	
