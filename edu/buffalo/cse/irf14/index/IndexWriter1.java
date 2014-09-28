/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.buffalo.cse.irf14.analysis.*;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter1 {
	
	String indexDir;
	static HashMap<String,Integer> docMap = new HashMap<String,Integer>();
	static HashMap<Integer,String> revDocMap = new HashMap<Integer,String>();
	static HashMap<String,ArrayList<ArrayList<Integer>>> indexMap = new HashMap<String,ArrayList<ArrayList<Integer>>>();
	static int docCounter=0;
	static String docNameKey;
	public static int termCounter;
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter1(String indexDir) {
		indexDir=this.indexDir;
		//TODO : YOU MUST IMPLEMENT THIS
	}
	
	/**
	 * Method to add the given Document to the index
	 * This method should take care of reading the filed values, passing
	 * them through corresponding analyzers and then indexing the results
	 * for each indexable field within the document. 
	 * @param d : The Document to be added
	 * @throws IndexerException : In case any error occurs
	 * @throws TokenizerException 
	 */
	public void addDocument(Document d) throws IndexerException, TokenizerException {
		String s;
		Token token;
		Tokenizer tokenizer = new Tokenizer(" ");
		TokenFilter filter;
		TokenStream stream,stream2;
		
		AnalyzerFactory aFactory=AnalyzerFactory.getInstance();
		try {
			stream = tokenizer.consume(d.getField(FieldNames.CONTENT)[0]);
			filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.CONTENT, stream); // Default: Date Filter
			stream=filter.getStream();
			filter= new NumericTokenFilter(stream);
			stream=filter.getStream();
			filter= new SpecialCharsTokenFilter(stream);
			stream=filter.getStream();
			addDocID(d);
			index(stream);			
//			printIndex();
//			System.out.println(stream.next().getTermText());

//			
//			filter=new StopwordTokenFilter(stream);
//			stream=filter.getStream();
//			filter=new SymbolTokenFilter(stream);
//			stream=filter.getStream();




				
		} catch (TokenizerException e) {
			throw new IndexerException();
		}
		//TODO : YOU MUST IMPLEMENT THIS
	}
	
	/**
	 * Method that indicates that all open resources must be closed
	 * and cleaned and that the entire indexing operation has been completed.
	 * @throws IndexerException : In case any error occurs
	 */
	public int addDocID(Document d){
//		if(d.getField(FieldNames.CATEGORY)[0]==null) 

		docNameKey=/*d.getField(FieldNames.CATEGORY)[0]+"*"+*/d.getField(FieldNames.FILEID)[0];
		
		if(docMap.get(docNameKey)==null){
			docCounter++;
			docMap.put(docNameKey, docCounter);
			revDocMap.put(docCounter, docNameKey);
			
			return docCounter;			//which is docID
		}
		else return docMap.get(docNameKey);
	}
	
	public void index(TokenStream s){
		String text;
		ArrayList<ArrayList<Integer>> bothList=new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> postingList=new ArrayList<Integer>();
		ArrayList<Integer> freqList=new ArrayList<Integer>();
		while(s.hasNext()){
			text=s.next().getTermText();
			
			if(!indexMap.containsKey(text)){
				postingList.add(docCounter);
				freqList.add(1);
				bothList.add(0, postingList);
				bothList.add(1, freqList);
				indexMap.put(text,bothList);
				termCounter++;
			}
			else{
				bothList=indexMap.get(text);
				postingList=bothList.get(0);
				freqList=bothList.get(1);
				if(!postingList.contains(docCounter)){
					postingList.add(docCounter);
					freqList.add(1);
					}
				else{
					int index=postingList.indexOf(docCounter);
					freqList.add(index, freqList.get(index)+1);
				}
				bothList.add(0,postingList);
				bothList.add(1,freqList);
				indexMap.put(text, bothList);
			}
		}
	}
	
	public void printIndex(){
		ArrayList<Integer> l;
		int counter=0;
		for(String s:indexMap.keySet()){
			System.out.print(s+"> ");
			l=indexMap.get(s).get(0);
			for(int i=0;i<l.size();i++){
				System.out.print(l.get(i));
				System.out.print(", ");
				if(i>15){
					System.out.print("..and more..");
					break;
				}
			}
			System.out.println();
			if(counter>20){System.out.println("....."); break;}
		}
		System.out.println("Total Terms: "+termCounter);
	}
	public void close() throws IndexerException {
		//TODO
	}
}
