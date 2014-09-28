/**
 * 
 */
package edu.buffalo.cse.irf14.index;

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
		//TODO
	}
	
	/**
	 * Get total number of terms from the "key" dictionary associated with this 
	 * index. A postings list is always created against the "key" dictionary
	 * @return The total number of terms
	 */
	public int getTotalKeyTerms() {
		//TODO : YOU MUST IMPLEMENT THIS
		if(iType==IndexType.AUTHOR)
		return IndexWriter.authCounter;
		if(iType==IndexType.CATEGORY)
		return IndexWriter.catCounter;
		if(iType==IndexType.PLACE)
		return IndexWriter.placeCounter;
		if(iType==IndexType.TERM)
		return IndexWriter.termCounter;
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
			return IndexWriter.authdocCounter;
			if(iType==IndexType.CATEGORY)
			return IndexWriter.catdocCounter;
			if(iType==IndexType.PLACE)
			return IndexWriter.placedocCounter;
			if(iType==IndexType.TERM)
			return IndexWriter.docCounter;
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
		textLowerCase=term.toLowerCase();
		if(iType==IndexType.TERM){
		if(textLowerCase.equals("a")) map=IndexWriter.aa_an;
		else if(textLowerCase.charAt(0)=='a'){
			if((textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='n'))
			{
				map=IndexWriter.aa_an;
			}
			else{
				map=IndexWriter.ao_az;
			}
			
		}
	
		if(textLowerCase.equals("c")) map=IndexWriter.ca_cj;
		else if(textLowerCase.charAt(0)=='c'){
			if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='j'){
				map=IndexWriter.ca_cj;
			}
			else{
				map=IndexWriter.ck_cz;
			}
		}
		if(textLowerCase.equals("s")) map=IndexWriter.sa_si;
		else if(textLowerCase.charAt(0)=='s'){
			if(textLowerCase.charAt(1)>='a' && textLowerCase.charAt(1)<='i'){
				map=IndexWriter.sa_si;
			}
			else{
				map=IndexWriter.sj_sz;
			}
		}
		
		else if(textLowerCase.charAt(0)=='b'){
			map=IndexWriter.b;
		}
		else if(textLowerCase.charAt(0)=='d'){
			map=IndexWriter.d;
		}
		else if(textLowerCase.charAt(0)=='e'){
			map=IndexWriter.e;
		}
		else if(textLowerCase.charAt(0)=='f'){
			map=IndexWriter.f;
		}
		else if(textLowerCase.charAt(0)=='g'){
			map=IndexWriter.g;
		}
		else if(textLowerCase.charAt(0)=='h'){
			map=IndexWriter.h;
		}
		else if(textLowerCase.charAt(0)=='i'){
			map=IndexWriter.i;
		}
		else if(textLowerCase.charAt(0)=='j'){
			map=IndexWriter.j;
		}
		else if(textLowerCase.charAt(0)=='k'){
			map=IndexWriter.k;
		}
		else if(textLowerCase.charAt(0)=='l'){
			map=IndexWriter.l;
		}
		else if(textLowerCase.charAt(0)=='m'){
			map=IndexWriter.m;
		}
		else if(textLowerCase.charAt(0)=='n'){
			map=IndexWriter.n;
		}
		else if(textLowerCase.charAt(0)=='o'){
			map=IndexWriter.o;
		}
		else if(textLowerCase.charAt(0)=='p'){
			map=IndexWriter.p;
		}
		else if(textLowerCase.charAt(0)=='q'){
			map=IndexWriter.q;
		}
		else if(textLowerCase.charAt(0)=='r'){
			map=IndexWriter.r;
		}
		else if(textLowerCase.charAt(0)=='t'){
			map=IndexWriter.t;
		}
		else if(textLowerCase.charAt(0)=='u'){
			map=IndexWriter.u;
		}
		else if(textLowerCase.charAt(0)=='v' ||textLowerCase.charAt(0)=='w' ||textLowerCase.charAt(0)=='x' ||textLowerCase.charAt(0)=='y' ||textLowerCase.charAt(0)=='z'){
			map=IndexWriter.vwxyz;
		}
		else{
			map=IndexWriter.symbol;
		}
		}
		
		if(iType==IndexType.AUTHOR){
			map=IndexWriter.authM;
		}
		if(iType==IndexType.CATEGORY){
			map=IndexWriter.catM;
		}
		if(iType==IndexType.PLACE){
			map=IndexWriter.placeM;
		}
		
		ArrayList<Integer> postList = new ArrayList<Integer>();
		HashMap<String, Integer> map2=new HashMap<String,Integer>();
		
		if(map.containsKey(term)){
			postList=map.get(term);
		for(int i=0;i+2<=postList.size();i=i+2){
			String docID= IndexWriter.revDocMap.get(postList.get(i));
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
			list[0]=IndexWriter.authM;
		}
		else if(iType==IndexType.CATEGORY){
			list[0]=IndexWriter.catM;
		}
		else if(iType==IndexType.PLACE){
			list[0]=IndexWriter.placeM;
		}
		else{
			list=new HashMap[] {IndexWriter.aa_an, IndexWriter.ao_az,IndexWriter.ca_cj,IndexWriter.ck_cz,IndexWriter.sa_si,IndexWriter.sj_sz,IndexWriter.b,IndexWriter.d,IndexWriter.e,IndexWriter.f,IndexWriter.g,IndexWriter.h,IndexWriter.i,IndexWriter.j,IndexWriter.k,IndexWriter.l,IndexWriter.m,IndexWriter.n,IndexWriter.o,IndexWriter.p,IndexWriter.q,IndexWriter.r,IndexWriter.t,IndexWriter.u,IndexWriter.vwxyz,IndexWriter.symbol};
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