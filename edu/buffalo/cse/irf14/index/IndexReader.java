/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class that emulates reading data back from a written index
 */
public class IndexReader {
	TreeMap<String,Integer> sorted_map;
	String iDir;
	IndexType iType;
	HashMap<String,Integer> docMap;
	HashMap<Integer,String> revDocMap;
	int authCounter,authdocCounter,catCounter,catdocCounter,docCounter,placeCounter,placedocCounter,termCounter,termdocCounter;
	HashMap<String,ArrayList<Integer>> aa_an,ao_az,ca_cj,ck_cz,sa_si,sj_sz,b,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,t,u,vwxyz;
	HashMap<String,ArrayList<Integer>> symbol;
	/**
	 * Default constructor
	 * @param indexDir : The root directory from which the index is to be read.
	 * This will be exactly the same directory as passed on IndexWriter. In case 
	 * you make subdirectories etc., you will have to handle it accordingly.
	 * @param type The {@link IndexType} to read from
	 */
	public IndexReader(String indexDir, IndexType type) {
		this.iDir=indexDir;
		this.iType=type;
		try
	      {
	         FileInputStream fis = new FileInputStream(new File(iDir+File.separator+"Var.ser"));
//	         System.out.println(fis);
	         ObjectInputStream ois = new ObjectInputStream(fis);
//	         System.out.println(ois);
	         int[] list = (int[]) ois.readObject();
//	         System.out.println();
	         authCounter=list[0];
	         authdocCounter=list[1];
	         catCounter=list[2];
	         catdocCounter=list[3];
	         docCounter=list[4];
//	         System.out.println(list[4]);        
	         placeCounter=list[5];
	         placedocCounter=list[6];
	         termCounter=list[7];
//	         System.out.println(termCounter);
	         termdocCounter=list[8];
	         ois.close();
	         fis.close();
	         FileInputStream fis2 = new FileInputStream(new File(iDir+File.separator+"Doc.ser"));
	         ObjectInputStream ois2 = new ObjectInputStream(fis2);
	         HashMap[] list2 = (HashMap[]) ois2.readObject();
	         docMap=list2[0];
	         revDocMap=list2[1];
	         ois2.close();
	         fis2.close();
	         FileInputStream fis21 = new FileInputStream(new File(iDir+File.separator+"Term.ser"));
	         ObjectInputStream ois21 = new ObjectInputStream(fis21);
	         HashMap[] list21 = (HashMap[]) ois21.readObject();
	         aa_an=list21[0];
	         ao_az=list21[1];
	         ca_cj=list21[2];
	         ck_cz=list21[3];
	         sa_si=list21[4];
	         sj_sz=list21[5];
	         b=list21[6];
	         d=list21[7];
	         e=list21[8];
	         f=list21[9];
	         g=list21[10];
	         h=list21[11];
	         i=list21[12];
	         j=list21[13];
	         k=list21[14];
	         l=list21[15];
	         m=list21[16];
	         n=list21[17];
	         o=list21[18];
	         p=list21[19];
	        q= list21[20];
	         r=list21[21];
	         t=list21[22];
	         u=list21[23];
	         vwxyz=list21[24];
	         symbol=list21[25];
	         ois21.close();
	         fis21.close();
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
		
		//TODO
	}
	
	/**
	 * Get total number of terms from the "key" dictionary associated with this 
	 * index. A postings list is always created against the "key" dictionary
	 * @return The total number of terms
	 * @throws IndexerException 
	 */
	public HashMap returnMap(IndexType it) throws IndexerException{
		HashMap[] list1;
		HashMap<String,ArrayList<Integer>> hashM;
		try{
		switch(it){
		case AUTHOR:
			 FileInputStream fis2 = new FileInputStream(new File(iDir+File.separator+"Other.ser"));
	         ObjectInputStream ois2 = new ObjectInputStream(fis2);
	         HashMap[] list2 = (HashMap[]) ois2.readObject();
	         ois2.close();
	         fis2.close();
	         return list2[1];
		case CATEGORY:
			 FileInputStream fis21 = new FileInputStream(new File(iDir+File.separator+"Other.ser"));
	         ObjectInputStream ois21 = new ObjectInputStream(fis21);
	         HashMap[] list21 = (HashMap[]) ois21.readObject();
	         ois21.close();
	         fis21.close();
	         return list21[2];
		case PLACE:
			 FileInputStream fis211 = new FileInputStream(new File(iDir+File.separator+"Other.ser"));
	         ObjectInputStream ois211 = new ObjectInputStream(fis211);
	         HashMap[] list211 = (HashMap[]) ois211.readObject();
	         ois211.close();
	         fis211.close();
	         return list211[0];
		case TERM:
			// Already in Memory
			break;
		default:
			break;
		
		}
		}
		catch(Exception e){
			throw new IndexerException();
		}
		return null; 
	}
	public int getTotalKeyTerms() {
		//TODO : YOU MUST IMPLEMENT THIS
		if(iType==IndexType.AUTHOR)
		return authCounter;
		if(iType==IndexType.CATEGORY)
		return catCounter;
		if(iType==IndexType.PLACE)
		return placeCounter;
		if(iType==IndexType.TERM)
		return termCounter;
		else return 0;
	}
	
	/**
	 * Get total number of terms from the "value" dictionary associated with this 
	 * index. A postings list is always created with the "value" dictionary
	 * @return The total number of terms
	 */
	public int getTotalValueTerms() {
		//TODO: YOU MUST IMPLEMENT THIS
		if(iType==IndexType.AUTHOR)
			return authdocCounter;
			if(iType==IndexType.CATEGORY)
			return catdocCounter;
			if(iType==IndexType.PLACE)
			return placedocCounter;
			if(iType==IndexType.TERM)
			return docCounter;
			else return 0;
	}
	
	/**
	 * Method to get the postings for a given term. You can assume that
	 * the raw string that is used to query would be passed through the same
	 * Analyzer as the original field would have been.
	 * @param term : The "analyzed" term to get postings for
	 * @return A Map containing the corresponding fileid as the key and the 
	 * number of occurrences as values if the given term was found, null otherwise.
	 */
	public Map<String, Integer> getPostings(String term) {
		//TODO:YOU MUST IMPLEMENT THIS
		String textLowerCase=term.toLowerCase();
		HashMap<String,ArrayList<Integer>> map = null;
		if(iType==IndexType.TERM){
		if(textLowerCase.equals("a")) map=aa_an;
		else if(textLowerCase.charAt(0)=='a'){
			if((textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='n'))
			{
				map=aa_an;
			}
			else{
				map=ao_az;
			}
			
		}
	
		if(textLowerCase.equals("c")) map=ca_cj;
		else if(textLowerCase.charAt(0)=='c'){
			if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='j'){
				map=ca_cj;
			}
			else{
				map=ck_cz;
			}
		}
		if(textLowerCase.equals("s")) map=sa_si;
		else if(textLowerCase.charAt(0)=='s'){
			if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='i'){
				map=sa_si;
			}
			else{
				map=sj_sz;
			}
		}
		
		else if(textLowerCase.charAt(0)=='b'){
			map=b;
		}
		else if(textLowerCase.charAt(0)=='d'){
			map=d;
		}
		else if(textLowerCase.charAt(0)=='e'){
			map=e;
		}
		else if(textLowerCase.charAt(0)=='f'){
			map=f;
		}
		else if(textLowerCase.charAt(0)=='g'){
			map=g;
		}
		else if(textLowerCase.charAt(0)=='h'){
			map=h;
		}
		else if(textLowerCase.charAt(0)=='i'){
			map=i;
		}
		else if(textLowerCase.charAt(0)=='j'){
			map=j;
		}
		else if(textLowerCase.charAt(0)=='k'){
			map=k;
		}
		else if(textLowerCase.charAt(0)=='l'){
			map=l;
		}
		else if(textLowerCase.charAt(0)=='m'){
			map=m;
		}
		else if(textLowerCase.charAt(0)=='n'){
			map=n;
		}
		else if(textLowerCase.charAt(0)=='o'){
			map=o;
		}
		else if(textLowerCase.charAt(0)=='p'){
			map=p;
			
		}
		else if(textLowerCase.charAt(0)=='q'){
			map=q;
		}
		else if(textLowerCase.charAt(0)=='r'){
			map=r;
		}
		else if(textLowerCase.charAt(0)=='t'){
			map=t;
		}
		else if(textLowerCase.charAt(0)=='u'){
			map=u;
		}
		else if(textLowerCase.charAt(0)=='v' ||textLowerCase.charAt(0)=='w' ||textLowerCase.charAt(0)=='x' ||textLowerCase.charAt(0)=='y' ||textLowerCase.charAt(0)=='z'){
			map=vwxyz;
		}
		else{
			map=symbol;
		}
		}
		
		if(iType==IndexType.AUTHOR){
			try {
				map=returnMap(IndexType.AUTHOR);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(iType==IndexType.CATEGORY){
			try {
				map=returnMap(IndexType.CATEGORY);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(iType==IndexType.PLACE){
			try {
				map=returnMap(IndexType.PLACE);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Integer> postList = new ArrayList<Integer>();
		HashMap<String, Integer> map2=new HashMap<String,Integer>();
		
		if(map.containsKey(term)){
			postList=map.get(term);
		for(int i=0;i+2<=postList.size();i=i+2){
			String docID= revDocMap.get(postList.get(i));
			int freq1 = postList.get(i+1);
			map2.put(docID, freq1);
		}
		}
		else{
			map2=null;
		}
		return map2;
	}
	
	/**
	 * Method to get the top k terms from the index in terms of the total number
	 * of occurrences.
	 * @param k : The number of terms to fetch
	 * @return : An ordered list of results. Must be <=k fr valid k values
	 * null for invalid k values
	 */
	public List<String> getTopK(int k) {
		if(k>0)
		{
		//TODO YOU MUST IMPLEMENT THIS
		Map<String,Integer> sortMap = new HashMap<String,Integer>();
		ValueComparator bvc =  new ValueComparator(sortMap);
		sorted_map = new TreeMap<String,Integer>(bvc);
		HashMap[] list = {};
		if(iType==IndexType.AUTHOR){
			try {
				list[0]=returnMap(IndexType.AUTHOR);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(iType==IndexType.CATEGORY){
			try {
				list[0]=returnMap(IndexType.CATEGORY);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(iType==IndexType.PLACE){
			try {
				list[0]=returnMap(IndexType.PLACE);
			} catch (IndexerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			list=new HashMap[] {aa_an, ao_az,ca_cj,ck_cz,sa_si,sj_sz,b,d,e,f,g,h,i,j,this.k,l,m,n,o,p,q,r,t,u,vwxyz,symbol};
		}
		for(HashMap<String,ArrayList<Integer>> map:list){
			for(String ls:map.keySet()){
				int sum=0;
				for(int i=0;i+1<map.get(ls).size();i=i+2){
					sum=sum+map.get(ls).get(i+1);
				}
				sortMap.put(ls, sum);
			}
		}
		sorted_map.putAll(sortMap);
		int i=0;
		List<String> list1=new ArrayList<String>();
		for(String k2:sorted_map.keySet()){
			i++;
			if(i>k) break;
			list1.add(k2);
			
		}
		return list1;
	}
		else return null;
	}
	/**
	 * Method to implement a simple boolean AND query on the given index
	 * @param terms The ordered set of terms to AND, similar to getPostings()
	 * the terms would be passed through the necessary Analyzer.
	 * @return A Map (if all terms are found) containing FileId as the key 
	 * and number of occurrences as the value, the number of occurrences 
	 * would be the sum of occurrences for each participating term. return null
	 * if the given term list returns no results
	 * BONUS ONLY
	 */
	public Map<String, Integer> query(String...terms) {
		//TODO : BONUS ONLY
		
		return null;
	}
}
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}