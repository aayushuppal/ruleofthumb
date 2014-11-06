package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class OKScorer {
	// All tuning parameters for the formula of OKAPI
	public static double _k1 = 1.2 , _b = 0.75 , _k3 = 1.2;
	public static int MAX_QUERY_LENGTH = 30;
	TreeMap<String, Double> queryTerms = new TreeMap<String, Double>();
	TreeMap<String, double[]> docVector = new TreeMap<String, double[]>();
	IndexReader indexer ;
	double L_ave; // Average length of documents in corpus
	LinkedHashMap< String, Double> result2;
	
	public OKScorer(TreeMap<String, String[]> result, Query query, IndexReader ir) {
		// TODO Auto-generated constructor stub
		indexer = ir;
		L_ave=indexer.averageLength();
		createQueryTerms(query);
		createDocMap(result);
		result2 =okapi();
//		System.out.println(okapi_doc("0000005"));
//		test();
		
		
	}

	public LinkedHashMap< String, Double> result(){
		return result2;
	}
	
	public LinkedHashMap<String, Double> okapi() {
		HashMap<String, Double> temp = new HashMap<String, Double>();
		for(String docID: docVector.keySet()){
			temp.put(docID, okapi_doc(docID));
		}
		//---------- Sorting the results-----//
		ArrayList<Double> temp2 = new ArrayList<Double>();
		temp2.addAll(temp.values());
		Collections.sort(temp2);
		Collections.reverse(temp2);
		LinkedHashMap<String, Double> sortedRank = new LinkedHashMap<String, Double>();
		for(double d:temp2){
			for(String i: temp.keySet()){
				if(temp.get(i)==d){
					sortedRank.put(i, d);
					temp.put(i,-1.0);
				}
			}
		}
		double max=0;
		for(String s:sortedRank.keySet()){
			 max = sortedRank.get(s);
			 break;
		}
		for(String s:sortedRank.keySet()){
			sortedRank.put(s, (double) Math.round(100000*(sortedRank.get(s)/max))/100000) ;
		}
		return sortedRank;
	}



	public void createQueryTerms(Query q){
		LinkedList<String> queryString=q.queryString;
		String s;
		for(int i=0;i<q.size();i++){
			s=queryString.get(i);
			s=s.replaceAll("[\"\\]\\[{}\\(\\)<>]", "");
			if(s.contains(":")){
				
				Token t = new Token();
				TokenStream stream;
				TokenFilter filter;
				String term=s.split(":")[1];
				String type=s.split(":")[0];
				t.setTermText(term);
				ArrayList<Token> arr = new ArrayList<Token>();
				arr.add(t);
				stream = new TokenStream(arr);
				if(type.equals("Term"))
					{
					filter = (TokenFilter) AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
					try {
						while(filter.increment()){
							// do nothing
						}
					} catch (TokenizerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					term=filter.getStream().next().getTermText();
//					System.out.println(term);
					}

				if(!queryTerms.containsKey(term))
				queryTerms.put(term,1.0);
				else {
					double d =queryTerms.get(term)+1;
					queryTerms.put(term,d);
				}
			}
		}
		
	}
	
	public void createDocMap(TreeMap<String, String[]> result){
		int index=0;
		for(Entry<String, String[]> entry: result.entrySet()){
			String term= entry.getKey();
			String[] docArray = entry.getValue();
			for(int i=0;i<docArray.length;i=i+2){
				String docID =docArray[i];
				int termFrq = Integer.parseInt(docArray[i+1]);
				if(!docVector.containsKey(docID)){
					double[] temp = new double[MAX_QUERY_LENGTH];
					temp[index] = (double) termFrq;
					docVector.put(docID, temp);
				}
				else{
					double[] temp=docVector.get(docID);
					temp[index]=(double) termFrq;
					docVector.put(docID, temp);
				}
			}
			index++;
		}
	}
	public double okapi_doc(String docID){

		double rsvd = 0;
		TreeMap<String, Double> temp= new TreeMap<String, Double>(queryTerms);
		if(docVector.containsKey(docID)){
			int index=0;
			double N =  indexer.getTotalValueTerms();
			for(double d:docVector.get(docID)){
				
				String term =temp.pollFirstEntry().getKey();
//				System.out.println(term);
				double DF_t = 0;
				if(indexer.getPostings(term)!=null)
					DF_t = indexer.getPostings(term).size();
				double TF_td = docVector.get(docID)[index];
				double TF_tq = queryTerms.get(term);
				double Ld = indexer.getLength(docID);
				double L_ave = indexer.averageLength();
				double x1;
				if(DF_t!=0)
				x1 = Math.log(N/DF_t);
				else 
				x1=0;
				double x2 = (_k1+1) * TF_td;
				double x3 = _k1*((1-_b) + _b * (Ld/L_ave)) + TF_td;
				double x4 = ((_k3+1) * TF_tq)/(_k3 + TF_tq);
				rsvd = rsvd + x1 * (x2/x3) * x4;
				index++;
				if(index==queryTerms.size()) break;
			}
		}
		return rsvd;
	}
	public void test(){
		System.out.println(queryTerms);
		for(String a:docVector.keySet()){
			System.out.print(a+"> ");
			for (double b:docVector.get(a)){
				System.out.print(b+" ");
			}
			System.out.println();
		}
	}
}
