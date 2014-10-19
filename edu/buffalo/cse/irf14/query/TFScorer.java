package edu.buffalo.cse.irf14.query;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class TFScorer {
	TreeMap<String, Double> queryVector = new TreeMap<String, Double>();
	TreeMap<String, Double> idf = new TreeMap<String, Double>();
	TreeMap<String, Double> idf_query = new TreeMap<String, Double>();
	IndexReader index ;
	public static int MAX_QUERY_LENGTH=30;
	public TFScorer(HashMap<String, int[]> result, Query query) {
		index=new IndexReader("C:\\Users\\Festy\\Desktop\\IR Slides\\sample",IndexType.TERM);
		// TODO Auto-generated constructor stub
		ArrayList<String> termList = filterTerms(query);		// filters - operators, brackets, quotes
		TreeMap<Integer,int[]> docMap = docToTermMap(result);	// create doc --> {<term freq>} and stores terms in query vector to store positions of terms in the map
		TreeMap<Integer,double[]> docMap2= normalize(docMap); 	// Normalize the doc map with L2 norm of freq
		
		for(String s:termList){									// add frequency of terms in query Vector
			if(queryVector.containsKey(s)){
				queryVector.put(s, queryVector.get(s)+1);
			}
		}							
		normalizeQuery();										// Normalize query vector
		idf();													// calculate idf and stored it in a map with same name
		idf_query=queryVector;									// don't know why I did this. Let's just keep it that way.
		docMap2=tf_idf(docMap2);								// Calculated tf-idf
		docMap2=normalize(docMap2,0);
		normalizeQuery();
		LinkedHashMap< Integer, Double> rank=rank(docMap2);		// Ranked the documents
//		System.out.println(idf);
////-------Prints DocMap and Query Vector
//		System.out.println(queryVector);
//		for(int a:docMap2.keySet()){
//			System.out.print(a+"> ");
//			for (double b:docMap2.get(a)){
//				System.out.print(b+" ");
//			}
//			System.out.println();
//		}
		System.out.println(rank);
	}
	
	public ArrayList<String> filterTerms(Query query){
		LinkedList<String> queryString=query.queryString;
		ArrayList<String> termList=new ArrayList<String>();
		String s;
		for(int i=0;i<query.size();i++){
			s=queryString.get(i);
			s=s.replaceAll("[\"\\]\\[{}\\(\\)<>]", "");
			if(s.contains(":")){
				s=s.split(":")[1];
				termList.add(s);
			}
		}
		return termList;
		
	}
	public TreeMap<Integer, int[]> docToTermMap(HashMap<String, int[]> result){
	TreeMap<Integer, int[]> docMap = new TreeMap<Integer,int[]>();
	TreeMap<String, int[]> resultSorted = new TreeMap<String,int[]>(result);
		int index=0;
		for(String s:resultSorted.keySet()){
			int[] posting=result.get(s);
			for(int i=0;i<posting.length;i=i+2){
				int docID=posting[i];
				if(!docMap.containsKey(docID)) docMap.put(docID, new int[MAX_QUERY_LENGTH]);
				int[] temp= docMap.get(docID);
				temp[index]=posting[i+1];
				docMap.put(docID, temp);
			}
			
			if(!queryVector.containsKey(s)) queryVector.put(s, (double) 0);
			index++;
		}

		return docMap;
		
	}
	public TreeMap<Integer, double[]> normalize(TreeMap<Integer, int[]> docMap){
		Map<Integer, double[]> docMap2 = new TreeMap<Integer,double[]>();
		for(int i:docMap.keySet()){
			int[] temp=docMap.get(i);
			double[] temp2 = new double[100];
			double mod=0;
				for(int i1=0;i1<temp.length;i1++){
					mod=mod+temp[i1]*temp[i1];
				}
			mod=Math.sqrt(mod);
			for(int i1=0;i1<temp.length;i1++){
				temp2[i1]=temp[i1]/mod;
			}
			docMap2.put(i, temp2);
		}
		return (TreeMap<Integer, double[]>) docMap2;
	}
	public TreeMap<Integer, double[]> normalize(TreeMap<Integer, double[]> docMap, int j){ // j is just a dummy, ignore
		Map<Integer, double[]> docMap2 = new TreeMap<Integer,double[]>();
		for(int i:docMap.keySet()){
			double[] temp=docMap.get(i);
			double[] temp2 = new double[100];
			double mod=0;
				for(int i1=0;i1<temp.length;i1++){
					mod=mod+temp[i1]*temp[i1];
				}
			mod=Math.sqrt(mod);
			for(int i1=0;i1<temp.length;i1++){
				temp2[i1]=temp[i1]/mod;
			}
			docMap2.put(i, temp2);
		}
		return (TreeMap<Integer, double[]>) docMap2;
	}
	public void normalizeQuery(){
		double mod=0;
		for(String i:queryVector.keySet()){
			mod=mod+queryVector.get(i)*queryVector.get(i);
		}
		mod=Math.sqrt(mod);
		for(String i:queryVector.keySet()){
			queryVector.put(i, queryVector.get(i)/mod);
		}
	}
	public void idf(){
		double N=(double)index.getTotalValueTerms();
		for(String i:queryVector.keySet()){
			double Nt=(double)index.getPostings(i).size();
			idf.put(i, Math.log(N/Nt));
		}
	}
	public TreeMap<Integer, double[]> tf_idf(TreeMap<Integer, double[]> docMap2){
		 int count= 0;
		for(String s: queryVector.keySet()){
			double IDF =idf.get(s);
			for(int i:docMap2.keySet()){
				double[] temp=docMap2.get(i);
				temp[count]=temp[count]*IDF;
				docMap2.put(i, temp);
			}
			count++;
			double d=queryVector.get(s);
			d=d*IDF;
			queryVector.put(s, d);
		}
		return docMap2;
	}
	
	public LinkedHashMap<Integer, Double> rank(TreeMap<Integer, double[]> docMap2){
		HashMap<Integer, Double> rank=new HashMap<Integer,Double>();
			for(Entry<Integer,double[]> e: docMap2.entrySet()){
//				System.out.println(e.getKey());
				double[] arr=e.getValue();
				int index =0;
				double score=0;
				for(Entry<String, Double> term: queryVector.entrySet()){
					score=score+arr[index]*term.getValue();
					index++;
				}
				
//				System.out.println(score);
				rank.put(e.getKey(),score);
			}
//---------- Sorting the results-----//
			ArrayList<Double> temp = new ArrayList<Double>();
			temp.addAll(rank.values());
			Collections.sort(temp);
			Collections.reverse(temp);
			LinkedHashMap<Integer, Double> sortedRank = new LinkedHashMap<Integer, Double>();
			for(double d:temp){
				for(int i: rank.keySet()){
					if(rank.get(i)==d){
						sortedRank.put(i, d);
						rank.put(i,-1.0);
					}
				}
			}
		return sortedRank;
		
	}
}
