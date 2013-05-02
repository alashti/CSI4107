package ir.vsr;

import java.io.*;
import java.util.*;
import java.lang.*;

import ir.utilities.*;

/**
 * Gets and stores information about relevance feedback from the user and computes
 * an updated query based on original query and retrieved documents that are
 * rated relevant and irrelevant.
 *
 * @author Ray Mooney
 */

public class Feedback {

  /**
   * A Rochio/Ide algorithm parameter
   */
  public static double ALPHA = 1;
  /**
   * A Rochio/Ide algorithm parameter
   */
  public static double BETA = 1;
  /**
   * A Rochio/Ide algorithm parameter
   */
  public static double GAMMA = 1;

  /**
   * The original query vector for this query
   */
  public HashMapVector queryVector;
  /**
   * The current list of ranked retrievals
   */
  public Retrieval[] retrievals;
  /**
   * The current InvertedIndex
   */
  public InvertedIndex invertedIndex;
  /**
   * The list of DocumentReference's that were rated relevant
   */
  public ArrayList<DocumentReference> goodDocRefs = new ArrayList<DocumentReference>();
  /**
   * The list of DocumentReference's that were rated irrelevant
   */
  public ArrayList<DocumentReference> badDocRefs = new ArrayList<DocumentReference>();

  /**
   * Create a feedback object for this query with initial retrievals to be rated
   */
  public Feedback(HashMapVector queryVector, Retrieval[] retrievals, InvertedIndex invertedIndex) {
    this.queryVector = queryVector;
    this.retrievals = retrievals;
    this.invertedIndex = invertedIndex;
  }

  /**
   * Add a document to the list of those deemed relevant
   */
  public void addGood(DocumentReference docRef) {
    goodDocRefs.add(docRef);
    
  }

  /**
   * Add a document to the list of those deemed irrelevant
   */
  public void addBad(DocumentReference docRef) {
    badDocRefs.add(docRef);
  }

  /**
   * Has the user rated any documents yet?
   */
  public boolean isEmpty() {
    if (goodDocRefs.isEmpty() && badDocRefs.isEmpty())
      return true;
    else
      return false;
  }

  /**
   * Prompt the user for feedback on this numbered retrieval
   */
  public void getFeedback(int showNumber) {
    // Get the docRef for this document (remember showNumber starts at 1 and is 1 greater than array index)
    DocumentReference docRef = retrievals[showNumber - 1].docRef;
    String response = UserInput.prompt("Is document #" + showNumber + ":" + docRef.file.getName() +
        " relevant (y:Yes, n:No, u:Unsure)?: ");
    if (response.equals("y"))
      goodDocRefs.add(docRef);
    else if (response.equals("n"))
      badDocRefs.add(docRef);
    else if (!response.equals("u"))
      getFeedback(showNumber);
  }

  /**
   * Has the user already provided feedback on this numbered retrieval?
   */
  public boolean haveFeedback(int showNumber) {
    // Get the docRef for this document (remember showNumber starts at 1 and is 1 greater than array index)
    DocumentReference docRef = retrievals[showNumber - 1].docRef;
    if (goodDocRefs.contains(docRef) || badDocRefs.contains(docRef))
      return true;
    else
      return false;
  }
  
  /**
   * Added by Victor Buchanan
   * Method to save goodDocRef and BadDocRef to files, along with associated query
   */
  
  public void storeFeedback(){
	  File check = new File("Util");
	  File[] contchk = check.listFiles();
	  BufferedWriter outputStream;
	  
	  for(int i=0;i<contchk.length;i++){
		  String filename = "Util/query"+ i;
		  if(!contchk[i].getName().equals(filename))
			try {
				outputStream = new BufferedWriter(new FileWriter(filename));
				for (DocumentReference docRef : goodDocRefs) {
					  String s = "good," +docRef.file.getName() + "\r";
					  outputStream.write(s);
				    }
				for (DocumentReference docRef : badDocRefs){
					String s = "bad," +docRef.file.getName() + "\r";
					  outputStream.write(s);
				}
				break;
			} catch (IOException e) {
					e.printStackTrace();
			}
	  }
	  
	 
  }
  
  
  /**
   * Added by Victor Buchanan, Reads Feedback stored in files to rerun a specific query
   */
  public void readOldFeedback(){
	  BufferedReader inputStream;
	  String readin;
	  String[]  temp;
	  File check = new File("Util");
	  File[] flist = check.listFiles();
	  for(int i=0;i<flist.length;i++){
		  if(flist[i].getName().startsWith("query")){
			  try {
				inputStream = new BufferedReader(new FileReader(flist[i].getName()));
				
				readin = inputStream.readLine();
				while(readin.length()>1){
				temp = readin.split(",");
				if(temp.length == 2){
					File f = new File(temp[1]);
					DocumentReference docRef = new DocumentReference(f,f.length());
					if(temp[0].equals("good")){
						this.goodDocRefs.add(docRef);
					}else{
							this.badDocRefs.add(docRef);
						}
					}
				readin = inputStream.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }
	  
  }

  /**
   * Use the Ide_regular algorithm to compute a new revised query.
   *
   * @return The revised query vector.
   */
  public HashMapVector newQuery() {
    // Start the query as a copy of the original
    HashMapVector newQuery = queryVector.copy();
    // Normalize query by maximum token frequency and multiply by alpha
    newQuery.multiply(ALPHA / newQuery.maxWeight());
    // Add in the vector for each of the positively rated documents
    for (DocumentReference docRef : goodDocRefs) {
      // Get the document vector for this positive document
      Document doc = docRef.getDocument(invertedIndex.docType, invertedIndex.stem);
      HashMapVector vector = doc.hashMapVector();
      // Multiply positive docs by beta and normalize by max token frequency
      vector.multiply(BETA / vector.maxWeight());
      // Add it to the new query vector
      newQuery.add(vector);
    }
    // Subtract the vector for each of the negatively rated documents
    for (DocumentReference docRef : badDocRefs) {
      // Get the document vector for this negative document
      Document doc = docRef.getDocument(invertedIndex.docType, invertedIndex.stem);
      HashMapVector vector = doc.hashMapVector();
      // Multiply negative docs by beta and normalize by max token frequency
      vector.multiply(GAMMA / vector.maxWeight());
      // Subtract it from the new query vector
      newQuery.subtract(vector);
    }
    return newQuery;
  }


}
