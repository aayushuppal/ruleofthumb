/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.*;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter {
	
	String indexDir;
	public  HashMap<String,Integer> docMap = new HashMap<String,Integer>();
	public  HashMap<Integer,String> revDocMap = new HashMap<Integer,String>();
	public  HashMap<String,ArrayList<Integer>> indexMap=null;
	public  TreeMap<String,Double> doc_length = new TreeMap<String,Double>();
	public  HashMap<String,ArrayList<Integer>> aa_an = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> ao_az = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> ca_cj = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> ck_cz = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> sa_si = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> sj_sz = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> b = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> d = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> e = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> f = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> g = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> h = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> i = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> j = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> k = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> l = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> m = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> n = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> o = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> p = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> q = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> r = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> t = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> u = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> vwxyz = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> authM = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> catM = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> placeM = new HashMap<String,ArrayList<Integer>>();
//	static HashMap<String,ArrayList<Integer>> newsM = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,ArrayList<Integer>> symbol = new HashMap<String,ArrayList<Integer>>();
	public  HashMap<String,String> list = new HashMap<String,String>();
	public  int docCounter;
	public  int termdocCounter;
	public  int authdocCounter;
	public  int catdocCounter;
	public  int placedocCounter;
	 String docNameKey;
	public  int termCounter;
	public  int authCounter;
	public  int catCounter;
	public  int placeCounter;
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		this.indexDir=indexDir;
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
	public void addDocument(Document d) throws IndexerException {
		String s;
		Token token;
		Tokenizer tokenizer = new Tokenizer(" ");
		TokenFilter filter;
		TokenStream stream,stream2;
		
		AnalyzerFactory aFactory=AnalyzerFactory.getInstance();
		addDocID(d);
		try {
			if(d.getField(FieldNames.CONTENT)!=null){
			stream = tokenizer.consume(d.getField(FieldNames.CONTENT)[0]);
			filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.CONTENT, stream);
			while(filter.increment()){
			}
			stream=filter.getStream();
			d.length=d.length+stream.arrListToken.size();
			index(stream,FieldNames.CONTENT);
			}
			
			if(d.getField(FieldNames.AUTHOR)!=null){
				authdocCounter++;
				String author=d.getField(FieldNames.AUTHOR)[0];
				ArrayList<Token >  a2=new ArrayList<Token >();
				Token t3;
				if(author.toLowerCase().contains(" and "))
				{// * Multiple Authors * //
					t3=new Token();
					t3.setTermText(author.split("(?i)and")[0]);
					t3=new Token();
					t3.setTermText(author.split("(?i)and")[1]);
					a2.add(t3);
//					System.out.println(author);
				}
				else{
					t3=new Token();
					t3.setTermText(author);
					a2.add(t3);
					
				}
				TokenStream authStream=new TokenStream(a2);
				d.length=d.length+authStream.arrListToken.size();
				index(authStream,FieldNames.AUTHOR);
				}
			
			if(d.getField(FieldNames.CATEGORY)!=null){
				catdocCounter++;
				ArrayList<Token >  a2=new ArrayList<Token >();
				Token t3 = new Token();
				t3.setTermText(d.getField(FieldNames.CATEGORY)[0]);
				a2.add(t3);
				index(new TokenStream(a2),FieldNames.CATEGORY);
				}
			if(d.getField(FieldNames.NEWSDATE)!=null){
				
				stream = tokenizer.consume(d.getField(FieldNames.NEWSDATE)[0]);
				filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.NEWSDATE, stream);
				while(filter.increment()){
				}
				stream=filter.getStream();				
				index(stream,FieldNames.NEWSDATE);
				}
			if(d.getField(FieldNames.PLACE)!=null){
				placedocCounter++;
				stream = tokenizer.consume(d.getField(FieldNames.PLACE)[0]);
				filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.PLACE, stream);
				while(filter.increment()){
				}
				stream=filter.getStream();		
				d.length=d.length+stream.arrListToken.size();
				index(stream,FieldNames.PLACE);
				}
			if(d.getField(FieldNames.TITLE)!=null){
				
				stream = tokenizer.consume(d.getField(FieldNames.TITLE)[0]);
				filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.TITLE, stream);
				
				while(filter.increment()){
					
				}
				stream=filter.getStream();			
				d.length=d.length+stream.arrListToken.size();
				index(stream,FieldNames.TITLE);
				}
//			System.out.println(d.length);
			addLengthIndex(d);
		} catch (Exception e) {
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
//		if(docNameKey.equals("0001882") || docNameKey.equals("0002646")|| docNameKey.equals("0001085") || docNameKey.equals("0000495")){
//			System.out.println(docNameKey+" "+docCounter);
//		}
		if(docMap.get(docNameKey)==null){
			docCounter++;
			docMap.put(docNameKey, docCounter);
			revDocMap.put(docCounter,docNameKey);
//			System.out.println(docNameKey);
			return docCounter;			//which is docID
		}
		else return docMap.get(docNameKey);
	}
	
	public void index(TokenStream s, FieldNames fieldname){
		String text;
		String textLowerCase;
		s.reset();
		while(s.hasNext()){
			text=s.next().getTermText();
			textLowerCase=text.toLowerCase();
	if(fieldname==FieldNames.CONTENT||fieldname==FieldNames.TITLE||fieldname==FieldNames.NEWSDATE){
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
				ArrayList<Integer> postingListTemp = new ArrayList<Integer>();
				for(int i=0;i<postingList.size();i++){
					if(i%2==0) postingListTemp.add(postingList.get(i));
				}
				if(!postingListTemp.contains(docCounter))
					{
					postingList.add(docCounter);
					postingList.add(1);
					}
				else{
					int freq=postingList.get((postingListTemp.indexOf(docCounter)*2+1))+1;
					postingList.add(postingListTemp.indexOf(docCounter)*2+1,freq);
				}
				indexMap.put(text, postingList);
			
			}
	}
	else if(fieldname==FieldNames.AUTHOR){
		indexMap=authM;
		if(!indexMap.containsKey(text)){
			ArrayList<Integer> postingList=new ArrayList<Integer>();
			postingList.add(docCounter);
			postingList.add(1);
			indexMap.put(text, postingList);
			authCounter++;
		}
		else{
//			ArrayList<Integer> postingList=indexMap.get(text);
//			if(!postingList.contains(docCounter))
//				{
//				postingList.add(docCounter);
//				postingList.add(1);
//				}
//			else{
//				int freq=postingList.get((postingList.indexOf(docCounter)+1))+1;
//				postingList.add(postingList.indexOf(docCounter)+1,freq);
//			}
//			indexMap.put(text, postingList);
			ArrayList<Integer> postingList=indexMap.get(text);
			ArrayList<Integer> postingListTemp = new ArrayList<Integer>();
			for(int i=0;i<postingList.size();i++){
				if(i%2==0) postingListTemp.add(postingList.get(i));
			}
			if(!postingListTemp.contains(docCounter))
				{
				postingList.add(docCounter);
				postingList.add(1);
				}
			else{
				int freq=postingList.get((postingListTemp.indexOf(docCounter)*2+1))+1;
				postingList.add(postingListTemp.indexOf(docCounter)*2+1,freq);
			}
			indexMap.put(text, postingList);
		}
	}
	else if(fieldname==FieldNames.CATEGORY){
		indexMap=catM;
		if(!indexMap.containsKey(text)){
			ArrayList<Integer> postingList=new ArrayList<Integer>();
			postingList.add(docCounter);
			postingList.add(1);
			indexMap.put(text, postingList);
			catCounter++;
		}
		else{
			ArrayList<Integer> postingList=indexMap.get(text);
			ArrayList<Integer> postingListTemp = new ArrayList<Integer>();
			for(int i=0;i<postingList.size();i++){
				if(i%2==0) postingListTemp.add(postingList.get(i));
			}
			if(!postingListTemp.contains(docCounter))
				{
				postingList.add(docCounter);
				postingList.add(1);
				}
			else{
				int freq=postingList.get((postingListTemp.indexOf(docCounter)*2+1))+1;
				postingList.add(postingListTemp.indexOf(docCounter)*2+1,freq);
			}
			indexMap.put(text, postingList);
		
		}
	}
	else if(fieldname==FieldNames.PLACE){
		indexMap=placeM;
		if(!indexMap.containsKey(text)){
			ArrayList<Integer> postingList=new ArrayList<Integer>();
			postingList.add(docCounter);
			postingList.add(1);
			indexMap.put(text, postingList);
			placeCounter++;
		}
		else{
			ArrayList<Integer> postingList=indexMap.get(text);
			ArrayList<Integer> postingListTemp = new ArrayList<Integer>();
			for(int i=0;i<postingList.size();i++){
				if(i%2==0) postingListTemp.add(postingList.get(i));
			}
			if(!postingListTemp.contains(docCounter))
				{
				postingList.add(docCounter);
				postingList.add(1);
				}
			else{
				int freq=postingList.get((postingListTemp.indexOf(docCounter)*2+1))+1;
				postingList.add(postingListTemp.indexOf(docCounter)*2+1,freq);
			}
			indexMap.put(text, postingList);
		
		}
	}


//			System.out.println(text);
		}
	}
	public void addLengthIndex(Document d){
		if(!doc_length.containsKey(d.getField(FieldNames.FILEID)[0])){
			doc_length.put(d.getField(FieldNames.FILEID)[0], d.length);
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
		
		HashMap[] listTerm={this.aa_an, this.ao_az,this.ca_cj,this.ck_cz,this.sa_si,this.sj_sz,this.b,this.d,this.e,this.f,this.g,this.h,this.i,this.j,this.k,this.l,this.m,this.n,this.o,this.p,this.q,this.r,this.t,this.u,this.vwxyz,this.symbol};
		HashMap[] listOther={this.placeM, this.authM,this.catM};
		HashMap[] listDoc={this.docMap,this.revDocMap};
		int[] listVar={this.authCounter,this.authdocCounter,this.catCounter,this.catdocCounter,this.docCounter,this.placeCounter,this.placedocCounter,this.termCounter,this.termdocCounter};
		File Term=new File (indexDir+File.separator+"Term.ser");
		File Other=new File (indexDir+File.separator+"Other.ser");
		File Var=new File (indexDir+File.separator+"Var.ser");
		File Doc=new File (indexDir+File.separator+"Doc.ser");
		File Length= new File(indexDir+File.separator+"Length.ser");
//		System.out.println(IndexWriter.docCounter);
		try{
		FileOutputStream f1 = new FileOutputStream(Term);  
		FileOutputStream f2 = new FileOutputStream(Other);
		FileOutputStream f3 = new FileOutputStream(Var);
		FileOutputStream f4 = new FileOutputStream(Doc);
		FileOutputStream f5 = new FileOutputStream(Length);
		ObjectOutputStream s1 = new ObjectOutputStream(f1);
		ObjectOutputStream s2 = new ObjectOutputStream(f2);
		ObjectOutputStream s3 = new ObjectOutputStream(f3);
		ObjectOutputStream s4 = new ObjectOutputStream(f4);
		ObjectOutputStream s5 = new ObjectOutputStream(f5);
		s1.writeObject(listTerm);
        s1.close();
        s2.writeObject(listOther);
        s2.close();
        s3.writeObject(listVar);
        s3.close();
        s4.writeObject(listDoc);
        s4.close();
        s5.writeObject(doc_length);
        s5.close();
		}
		catch(Exception e) {
			throw new IndexerException(e);
		}
		
	}
}
