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
public class IndexWriter {
	
	String indexDir;
	static HashMap<String,Integer> docMap = new HashMap<String,Integer>();
	static HashMap<Integer,String> revDocMap = new HashMap<Integer,String>();
	static HashMap<String,ArrayList<Integer>> indexMap=null;
	static HashMap<String,ArrayList<Integer>> aa_an = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> ao_az = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> ca_cj = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> ck_cz = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> sa_si = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> sj_sz = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> b = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> d = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> e = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> f = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> g = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> h = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> i = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> j = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> k = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> l = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> m = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> n = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> o = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> p = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> q = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> r = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> t = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> u = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> vwxyz = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,ArrayList<Integer>> symbol = new HashMap<String,ArrayList<Integer>>();
	static HashMap<String,String> list = new HashMap<String,String>();
	static int docCounter=0;
	static String docNameKey;
	public static int termCounter;
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
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
			revDocMap.put(docCounter,docNameKey);
//			System.out.println(docNameKey);
			return docCounter;			//which is docID
		}
		else return docMap.get(docNameKey);
	}
	
	public void index(TokenStream s){
		String text;
		String textLowerCase;
		while(s.hasNext()){
			text=s.next().getTermText();
			textLowerCase=text.toLowerCase();
			if(textLowerCase.equals("a")) indexMap=aa_an;
			else if(textLowerCase.charAt(0)=='a'){
				if((textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='n'))
				{
					indexMap=aa_an;
				}
				else{
					indexMap=ao_az;
				}
				
			}
		
			if(textLowerCase.equals("c")) indexMap=ca_cj;
			else if(textLowerCase.charAt(0)=='c'){
				if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='j'){
					indexMap=ca_cj;
				}
				else{
					indexMap=ck_cz;
				}
			}
			if(textLowerCase.equals("s")) indexMap=sa_si;
			else if(textLowerCase.charAt(0)=='s'){
				if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='i'){
					indexMap=sa_si;
				}
				else{
					indexMap=sj_sz;
				}
			}
			
			else if(textLowerCase.charAt(0)=='b'){
				indexMap=b;
			}
			else if(textLowerCase.charAt(0)=='d'){
				indexMap=d;
			}
			else if(textLowerCase.charAt(0)=='e'){
				indexMap=e;
			}
			else if(textLowerCase.charAt(0)=='f'){
				indexMap=f;
			}
			else if(textLowerCase.charAt(0)=='g'){
				indexMap=g;
			}
			else if(textLowerCase.charAt(0)=='h'){
				indexMap=h;
			}
			else if(textLowerCase.charAt(0)=='i'){
				indexMap=i;
			}
			else if(textLowerCase.charAt(0)=='j'){
				indexMap=j;
			}
			else if(textLowerCase.charAt(0)=='k'){
				indexMap=k;
			}
			else if(textLowerCase.charAt(0)=='l'){
				indexMap=l;
			}
			else if(textLowerCase.charAt(0)=='m'){
				indexMap=m;
			}
			else if(textLowerCase.charAt(0)=='n'){
				indexMap=n;
			}
			else if(textLowerCase.charAt(0)=='o'){
				indexMap=o;
			}
			else if(textLowerCase.charAt(0)=='p'){
				indexMap=p;
			}
			else if(textLowerCase.charAt(0)=='q'){
				indexMap=q;
			}
			else if(textLowerCase.charAt(0)=='r'){
				indexMap=r;
			}
			else if(textLowerCase.charAt(0)=='t'){
				indexMap=t;
			}
			else if(textLowerCase.charAt(0)=='u'){
				indexMap=u;
			}
			else if(textLowerCase.charAt(0)=='v' ||textLowerCase.charAt(0)=='w' ||textLowerCase.charAt(0)=='x' ||textLowerCase.charAt(0)=='y' ||textLowerCase.charAt(0)=='z'){
				indexMap=vwxyz;
			}
			else{
				indexMap=symbol;
			}
			
			
			if(!indexMap.containsKey(text)){
				ArrayList<Integer> postingList=new ArrayList<Integer>();
				postingList.add(docCounter);
				postingList.add(1);
				indexMap.put(text, postingList);
				termCounter++;
			}
			else{
				ArrayList<Integer> postingList=indexMap.get(text);
				if(!postingList.contains(docCounter))
					{
					postingList.add(docCounter);
					postingList.add(1);
					}
				else{
					int freq=postingList.get((postingList.indexOf(docCounter)+1))+1;
					postingList.add(postingList.indexOf(docCounter)+1,freq);
				}
				indexMap.put(text, postingList);
			
			}
//			System.out.println(text);
		}
	}
	
	public void printIndex(){
		ArrayList<Integer> l;
		int counter=0;
		for(String s:indexMap.keySet()){
			System.out.print(s+"> ");
			l=indexMap.get(s);
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
