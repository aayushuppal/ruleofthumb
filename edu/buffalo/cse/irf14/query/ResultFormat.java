package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexReader;

public class ResultFormat {

	static IndexReader reader;
	TokenStream stream;
	Token t;
	TreeMap<String,String[]> resultMap;
	public ResultFormat(LinkedList<String> result, Query q ,IndexReader ir){
	reader=ir;
	ArrayList<Token> list;
	TokenStream stream;
	TokenFilter filter;
	HashMap<String, Integer> posting;
	resultMap = new TreeMap<String,String[]>();
	for(String s: q.getSplitQuery())
	{
		Token token = new Token();
		s=s.replaceAll("[\\(\\)\"\\{\\}]", "");
		
		if(s.contains("Term:"))
		{
			s=s.substring(5);
			token.setTermText(s);
			list = new ArrayList<Token>();
			list.add(token);
			stream = new TokenStream(list);
			filter = (TokenFilter) AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
					
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s =filter.getStream().next().getTermText();
		}
		
		if(s.contains("Category:"))
		{
			s=s.substring(9);
			token.setTermText(s);
			list = new ArrayList<Token>();
			list.add(token);
			stream = new TokenStream(list);
			filter = (TokenFilter) AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CATEGORY, stream);
			try {
				while(filter.increment()){
					
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s =filter.getStream().next().getTermText();
		}
		if(s.contains("Author:"))
		{
			s=s.substring(7);
			token.setTermText(s);
			list = new ArrayList<Token>();
			list.add(token);
			stream = new TokenStream(list);
			filter = (TokenFilter) AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.AUTHOR, stream);
			try {
				while(filter.increment()){
					
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s =filter.getStream().next().getTermText();
		}
		if(s.contains("Place:"))
		{
			s=s.substring(6);
			token.setTermText(s);
			list = new ArrayList<Token>();
			list.add(token);
			stream = new TokenStream(list);
			filter = (TokenFilter) AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
					
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s =filter.getStream().next().getTermText();
		}
		/////////////////////////////
		/* Now it reads from reader*/
		/////////////////////////////
		if(!s.equals("") && !s.equals(null))
		{
			System.out.println(s);
			posting=(HashMap<String, Integer>) reader.getPostings(s);
			if(posting!=null){
				for(String docID: result){
					if(posting.containsKey(docID)){
						if(!resultMap.containsKey(docID))
							resultMap.put(s, new String[]{"-1"});
						ArrayList<String> arr;
						if(resultMap.get(s)[0].equals("-1")){
						arr = new ArrayList<String>();
						}
						else{
						arr = new ArrayList<String>();
							for(String s1: resultMap.get(s)){
								arr.add(s1);
							}
						}
						 arr.add(docID);
						 arr.add(arr.indexOf(docID)+1,Integer.toString(posting.get(docID)));
//						resultMap.put(s,(String[]) arr.toArray() );
						 String[] t = new String[arr.size()];
						 for(int i=0;i<arr.size();i++){
							 t[i]=arr.get(i);
						 }
						 resultMap.put(s,t );
					}
				}
			}
			else{
				System.out.println("no results for "+s);
			}
		}
		else{
			System.out.println("s is filterd");
		}
		
	}
	
	//TODO: Method to format given result for Q mode Query()
	
	
}

public TreeMap<String,String[]> Result(){
	//TODO: Method to format given resultArray[] for E mode Query()
	return resultMap;
	
}
}
