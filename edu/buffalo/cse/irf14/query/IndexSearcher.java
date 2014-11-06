package edu.buffalo.cse.irf14.query;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexerException;

public class IndexSearcher {
	
	static int resCounter = 0;
	static HashMap<String, LinkedList<String>> TDocid = new HashMap<String, LinkedList<String>>();
	static HashMap<String, HashMap<String, Integer>> TDocid2 = new HashMap<String, HashMap<String, Integer>>();
	static Map<String, Integer> TermMap = new HashMap<String, Integer>();
	static TreeMap<String, LinkedList<String>> resNumMap = new TreeMap<String, LinkedList<String>>();
	static LinkedList<String> EmptyDocidslist = new LinkedList<String>();
	public IndexReader IRdrObj;
	
	public IndexSearcher(IndexReader i) {
		// TODO Auto-generated constructor stub
		IRdrObj = i;
		
	}
	public LinkedList<String> search(Query query){
		
		return ConvertToStack(query.getSplitQuery());
		
	}
	
	public LinkedList<String> ConvertToStack(ArrayList<String> a){
		
		String x ="";
		String y ="";
		String tstr ="";
		String bbb = "";
		int count = 0;
		
		Stack<String> MainStack = new Stack<String>(); 
	    MainStack.addAll(a);
	    Stack<String> TempStack = new Stack<String>(); 

	    while(!MainStack.isEmpty()){
	    	
	    	
	    	while(!x.equals("[")){
	    		if (count == 1 ){ TempStack.add(x); count++; }
	    		x = MainStack.pop();
	    		TempStack.add(x);
	    		if (x.equals("{")){break; }
	    	} 
	    	
	    	while(!y.equals("]")){
	        	y = TempStack.pop();
	        	tstr = tstr+" "+y;
	        	if (TempStack.isEmpty()){break; }
	        }
	    	tstr = tstr.substring(1);
	    	tstr = QueryProcess(tstr);
	    	MainStack.push(tstr);
	    	while(!TempStack.isEmpty()){
	    		bbb = TempStack.pop();
	        	MainStack.push(bbb);
	        }
	    	//System.out.println(MainStack);
	    	TempStack = new Stack<String>(); 
	    	
	    	x = MainStack.pop();
	    	 count = 1;
	    	 y = "";
	    	 tstr = "";

	    }
	    
//	    System.out.println(resNumMap.lastEntry().getValue());
	    return resNumMap.lastEntry().getValue();
	    
	}
	
	public String QueryProcess(String a){
//		IndexSearcher ISobj = new IndexSearcher(); 
		//System.out.println(TDocid.get("A"));
		//System.out.println("Query input here: "+a);
		int c=0;
		String[] splitQuerClone = a.split("\\s+");
		List<String> alternSplitQuer = new ArrayList<String>();
		
		
		for (int h=0; h<splitQuerClone.length;h++){
			if(splitQuerClone[h].contains("\"")){
				alternSplitQuer.add(splitQuerClone[h]+" "+splitQuerClone[h+1]);
				h++;
			}
			else {
				alternSplitQuer.add(splitQuerClone[h]);
			}
			c++;
		}
		String[] splitQuer = alternSplitQuer.toArray(new String[alternSplitQuer.size()]);
		String qresult = "";
		int SpqLen = splitQuer.length;
		
		ArrayList<String> QuerArrayList = new ArrayList<String>();
		for(int j=0; j<SpqLen; j++){
			if(j != 0 && j != SpqLen-1){QuerArrayList.add(splitQuer[j]);}
		}
		
		int QArrlLen = QuerArrayList.size();
		//System.out.println("query length: "+QArrlLen);
		if(QArrlLen == 1){
			String QprcsArr[] = new String[1];
			QprcsArr[0] = QuerArrayList.get(QArrlLen-1);
			QuerArrayList.remove(QArrlLen-1);
			qresult = SingleQueryProcessor(QprcsArr[0]);
		}
		
		else { 
			String QprcsArr[] = new String[3];
		
		
		while(!QuerArrayList.isEmpty()){
			QprcsArr[0] = QuerArrayList.get(QArrlLen-1);
			QuerArrayList.remove(QArrlLen-1);
			QprcsArr[1] = QuerArrayList.get(QArrlLen-2);
			QuerArrayList.remove(QArrlLen-2);
			QprcsArr[2] = QuerArrayList.get(QArrlLen-3);
			QuerArrayList.remove(QArrlLen-3);
			
			//System.out.println(QprcsArr[1]);
			if(QprcsArr[1].equals("AND")){
				
				//System.out.println("call AND function with "+ QprcsArr[0]+" and "+ QprcsArr[2]+" as operands");
				// process the operands
				// return res#
				qresult = AndProcessor(QprcsArr[0],QprcsArr[2]);
			}
			else {
				//System.out.println("call OR function with "+ QprcsArr[0]+" and "+ QprcsArr[2]+" as operands");
				// process the operands
				// return res#
				qresult = OrProcessor(QprcsArr[0],QprcsArr[2]);
				
			}
			
			QArrlLen = QuerArrayList.size();
			if(QArrlLen != 0){ 
				QuerArrayList.add(qresult);
				}
			QArrlLen = QuerArrayList.size();
				
		}
			
		//qprcCount++;
		//String qresult = "res"+qprcCount;
		}
		return qresult;
	}
	

	public String SingleQueryProcessor(String a){
		if(a.contains("\"")){
		String x = TermQuoteRem(a);
		a = x;
		}
		
		int flag = 0;
		// define a linked list of full doc ids as a single linked list of strings. call it "FullDocidslist"
//		String[] arr1 = (String[]) IRdrObj.revDocMap.keySet().toArray();
		String[] arr1 = new String[IRdrObj.docMap.keySet().size()];
		int index =0;
		for(String s:IRdrObj.docMap.keySet()){
			arr1[index]=s;
			index++;
		}
		LinkedList<String> FullDocidslist = new LinkedList<String>(Arrays.asList(arr1));
		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> SingQuerList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist); // create a clone of the fulldocids list
		resCounter++;
		
		
		
		if (a.contains("Term:")){
			a = a.substring(5);	
			//Pass a through filters and show new "a"
			if (a.startsWith("<")){a = a.substring(1, a.length()-1); flag = 1;}
			
			ArrayList<Token> arr11= new ArrayList<Token>();
			Token t= new Token();
			t.setTermText(a);
			arr11.add(t);
			TokenStream stream = new TokenStream(arr11);
			
			TokenFilter filter = (TokenFilter)AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream=filter.getStream();
			a=stream.next().toString();
			if (flag == 1){a = "<"+a+">"; }
			
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				TermMap = IRdrObj.getPostings(a1);
				if (TermMap == null){
					Oprnd1List.addAll(FullDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					FullDocidslistClone.removeAll(Oprnd1List);
					Oprnd1List.addAll(FullDocidslistClone);
				}
				SingQuerList.addAll(Oprnd1List);
				//retrieve a link list of string for all doc ids corresponding to a1 ----- (1)
					// if "a1" is not present in dictionary return an empty linklist<string> 
					// else output the difference of full doc ids list and list retrieved in (1)
				// output goes in singquerlist of type "linklist<string>"
			}
			else {
				TermMap = IRdrObj.getPostings(a);
				
				if (TermMap == null){
					Oprnd1List.addAll(EmptyDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					
				}
				
				SingQuerList.addAll(Oprnd1List);
				// TermMap = IRdrObj.getPostings(a);
				// convert this to string array for the key values
				// convert this string array to linkedlist<string> of docids
				// o/p this linkedlist<string> in singquerlist
				// if termmap is empty pass an empty linklist<string> through singquerlist
			}
			Collections.sort(SingQuerList);
		}
		
		else { 
			if (a.contains("Author:")||a.contains("author:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of author */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.AUTHOR);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(7);
			}
			else if(a.contains("Place:") || a.contains("place:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of place */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.PLACE);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(6);
			}
			else if(a.contains("Category:") || a.contains("category:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of category */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.CATEGORY);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(9);
			}
			else {}
		
		if (a.contains("res#")){
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = resNumMap.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd1List = resNumMap.get(a); 
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = TDocid.get(a1.toLowerCase());
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
				
			}
			else { TDocid.get(a); 
				Oprnd1List = TDocid.get(a.toLowerCase()); 
					if (Oprnd1List == null){
						Oprnd1List = new LinkedList<String>();
						Oprnd1List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		
		if (a.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd1List);
			Oprnd1List.addAll(FullDocidslistClone);
		}
		
		
		Collections.sort(Oprnd1List);

		SingQuerList.addAll(Oprnd1List);
		Collections.sort(SingQuerList);
		}	//System.out.println(AndList);
			
		String singquer = "res#"+resCounter;
		resNumMap.put(singquer, SingQuerList);
		return singquer;
	}

	
	public String AndProcessor(String a, String b){
		if(a.contains("\"")){
			String x = TermQuoteRem(a);
			a = x;
			}
		if(b.contains("\"")){
			String x = TermQuoteRem(b);
			b = x;
			}
		int flag = 0;
		resCounter++;
		// define the FullDocidslist
		String[] arr1 = new String[IRdrObj.docMap.keySet().size()];
		int index =0;
		for(String s:IRdrObj.docMap.keySet()){
			arr1[index]=s;
			index++;
		}
		LinkedList<String> FullDocidslist = new LinkedList<String>(Arrays.asList(arr1));
		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> Oprnd2List = new LinkedList<String>();
		LinkedList<String> AndList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist); // create a clone of FullDocidslist
		
		if (a.contains("Term:")){
			a = a.substring(5);
			if(a.contains(">")){
				 a= a.substring(1, a.length());
				 a = "<"+a;
			}
			//a = a.substring(0, a.length()-1);
			//Pass a through filters and show new "a"
			if (a.startsWith("<")){a = a.substring(1, a.length()-1); flag = 1;}
			
			ArrayList<Token> arr11= new ArrayList<Token>();
			Token t= new Token();
			t.setTermText(a);
			arr11.add(t);
			TokenStream stream = new TokenStream(arr11);
			
			TokenFilter filter = (TokenFilter)AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream=filter.getStream();
			a=stream.next().toString();
			if (flag == 1){a = "<"+a+">"; }
			
			
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				
				TermMap = IRdrObj.getPostings(a1);
				if (TermMap == null){
					Oprnd1List.addAll(FullDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					FullDocidslistClone.removeAll(Oprnd1List);
					Oprnd1List.addAll(FullDocidslistClone);
				}
				//retrieve a link list of string for all doc ids corresponding to a1 ----- (1)
					// if "a1" is not present in dictionary return an empty linklist<string> 
					// else output the difference of full doc ids list and list retrieved in (1)
				// output goes in Oprnd1List of type "linklist<string>"
			}
			else { 

				TermMap = IRdrObj.getPostings(a);
				if (TermMap == null){
					Oprnd1List.addAll(EmptyDocidslist);
				}

				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					
				}
				// TermMap = IRdrObj.getPostings(a);
				// convert this to string array for the key values
				// convert this string array to linkedlist<string> of docids
				// o/p this linkedlist<string> in Oprnd1List
				// if termmap is empty pass an empty linklist<string> through Oprnd1List
			}
			//collesction.sort(Oprnd1List)
		}
		
		else { 
			if (a.contains("Author:") || a.contains("author:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of author */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.AUTHOR);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(7);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else if(a.contains("Place:") || a.contains("place:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of place */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.PLACE);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(6);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else if(a.contains("Category:") || a.contains("category:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of category */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.CATEGORY);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(9);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else {}
		
		if (a.contains("res#")){
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = resNumMap.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd1List = resNumMap.get(a); 
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = TDocid.get(a1.toLowerCase());
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
				
			}
			else { Oprnd1List = TDocid.get(a.toLowerCase()); 
					if (Oprnd1List == null){
						Oprnd1List = new LinkedList<String>();
						Oprnd1List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		
		if (a.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd1List);
			Oprnd1List.addAll(FullDocidslistClone);
		}
		Collections.sort(Oprnd1List);
		}
		
		flag = 0;
		
		if (b.contains("Term:")){
			b = b.substring(5);	
			if(b.contains(">")){
				 b= b.substring(1, b.length());
				 b = "<"+b;
			}
			//Pass b through filters and show new "b"
			if (b.startsWith("<")){b = a.substring(1, b.length()-1); flag = 1;}
			ArrayList<Token> arr11= new ArrayList<Token>();
			Token t= new Token();
			t.setTermText(b);
			arr11.add(t);
			TokenStream stream = new TokenStream(arr11);
			
			TokenFilter filter = (TokenFilter)AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream=filter.getStream();
			b=stream.next().toString();
			if (flag == 1){b = "<"+b+">"; }
			
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				TermMap = IRdrObj.getPostings(b1);
				if (TermMap == null){
					Oprnd2List.addAll(FullDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd2List = new LinkedList<String>(Arrays.asList(arrtm));
					FullDocidslistClone.removeAll(Oprnd1List);
					Oprnd2List.addAll(FullDocidslistClone);
				}
				//retrieve a link list of string for all doc ids corresponding to b1 ----- (1)
					// if "b1" is not present in dictionary return an empty linklist<string> 
					// else output the difference of full doc ids list and list retrieved in (1)
				// output goes in Oprnd2List of type "linklist<string>"
			}
			else {
				TermMap = IRdrObj.getPostings(b);
				if (TermMap == null){
					Oprnd2List.addAll(EmptyDocidslist);
				}

				else {	
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd2List = new LinkedList<String>(Arrays.asList(arrtm));
					
				}
				// TermMap = IRdrObj.getPostings(b);
				// convert this to string array for the key values
				// convert this string array to linkedlist<string> of docids
				// o/p this linkedlist<string> in Oprnd2List
				// if termmap is empty pass an empty linklist<string> through Oprnd2List
			}
			//collesction.sort(Oprnd2List)
		}
		
		else { 
			if (b.contains("Author:") || b.contains("author:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of author */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.AUTHOR);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(7);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else if(b.contains("Place:") || b.contains("place:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of place */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.PLACE);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(6);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else if(b.contains("Category:") || b.contains("category:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of category */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.CATEGORY);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(9);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else {}
		
		if (b.contains("res#")){
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = resNumMap.get(b1);
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd2List = resNumMap.get(b); 
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = TDocid.get(b1.toLowerCase());
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
				
			}
			else { Oprnd2List = TDocid.get(b.toLowerCase()); 
					if (Oprnd2List == null){
						Oprnd2List = new LinkedList<String>();
						Oprnd2List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		
		if (b.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd2List);
			Oprnd2List.addAll(FullDocidslistClone);
		}
		Collections.sort(Oprnd2List);
		}
		
			for(String j:Oprnd1List){
//				System.out.println("1 "+j);
				if(Oprnd2List.contains(j)){
//					System.out.println("2 "+j);
					AndList.add(j);
				}
		}

			Collections.sort(AndList);
			//System.out.println(AndList);
			
		String andquer = "res#"+resCounter;
		resNumMap.put(andquer, AndList);
		return andquer;
	}

	public String OrProcessor(String a, String b){
		if(a.contains("\"")){
			String x = TermQuoteRem(a);
			a = x;
			}

		if(b.contains("\"")){
			String x = TermQuoteRem(b);
			b = x;
			}
		resCounter++;
		int flag = 0;
		// define a linked list of full doc ids as a single linked list of strings. call it "FullDocidslist"
		String[] arr1 = new String[IRdrObj.docMap.keySet().size()];
		int index =0;
		for(String s:IRdrObj.docMap.keySet()){
			arr1[index]=s;
			index++;
		}
		
		LinkedList<String> FullDocidslist = new LinkedList<String>(Arrays.asList(arr1));

		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> Oprnd2List = new LinkedList<String>();
		LinkedList<String> Oprnd1ListClone = new LinkedList<String>();
		LinkedList<String> Oprnd2ListClone = new LinkedList<String>();
		LinkedList<String> OrList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist); // make a clone of FullDocidslist
		
		
		if (a.contains("Term:")){
			a = a.substring(5);	
			//Pass a through filters and show new "a"
			//if (a.startsWith("<")){a = a.substring(1, a.length()-1); flag = 1;}
			if(a.contains(">")){
				 a= a.substring(1, a.length());
				 a = "<"+a;
				
			}
			ArrayList<Token> arr11= new ArrayList<Token>();
			Token t= new Token();
			t.setTermText(a);
			arr11.add(t);
			TokenStream stream = new TokenStream(arr11);
			
			TokenFilter filter = (TokenFilter)AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream=filter.getStream();
			a=stream.next().toString();
			if (flag == 1){a = "<"+a+">"; }
			
			
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				TermMap = IRdrObj.getPostings(a1);
				if (TermMap == null){
					Oprnd1List.addAll(FullDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					FullDocidslistClone.removeAll(Oprnd1List);
					Oprnd1List.addAll(FullDocidslistClone);
				}
				//retrieve a link list of string for all doc ids corresponding to a1 ----- (1)
					// if "a1" is not present in dictionary return an empty linklist<string> 
					// else output the difference of full doc ids list and list retrieved in (1)
				// output goes in Oprnd1List of type "linklist<string>"
			}
			else {
				TermMap = IRdrObj.getPostings(a);
				if (TermMap == null){
					Oprnd1List.addAll(EmptyDocidslist);
				}

				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd1List = new LinkedList<String>(Arrays.asList(arrtm));
					
				}
				// TermMap = IRdrObj.getPostings(a);
				// convert this to string array for the key values
				// convert this string array to linkedlist<string> of docids
				// o/p this linkedlist<string> in Oprnd1List
				// if termmap is empty pass an empty linklist<string> through Oprnd1List
			}
			//collesction.sort(Oprnd1List)
		}
		
		else { 
			if (a.contains("Author:") || a.contains("author:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of author */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.AUTHOR);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(7);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else if(a.contains("Place:") || a.contains("place:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of place */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.PLACE);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(6);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else if(a.contains("Category:") || a.contains("category:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of category */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.CATEGORY);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				a = a.substring(9);
				if(a.contains(">")){
					 a= a.substring(1, a.length());
					 a = "<"+a;
				}
			}
			else {}

		if (a.contains("res#")){
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = resNumMap.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd1List = resNumMap.get(a); 
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = TDocid.get(a1.toLowerCase());
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
				
			}
			else { Oprnd1List = TDocid.get(a.toLowerCase()); 
					if (Oprnd1List == null){
						Oprnd1List = new LinkedList<String>();
						Oprnd1List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		
		if (a.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd1List);
			Oprnd1List.addAll(FullDocidslistClone);
		}
		Collections.sort(Oprnd1List);
		}
		
		if (b.contains("Term:")){
			b = b.substring(5);	
			//Pass b through filters and show new "b"
			//if (b.startsWith("<")){b = a.substring(1, b.length()-1); flag = 1;}
			if(b.contains(">")){
				 b= b.substring(1, b.length());
				 b = "<"+b;
				 
			}
			ArrayList<Token> arr11= new ArrayList<Token>();
			Token t= new Token();
			t.setTermText(b);
			arr11.add(t);
			TokenStream stream = new TokenStream(arr11);
			
			TokenFilter filter = (TokenFilter)AnalyzerFactory.getInstance().getAnalyzerForField(FieldNames.CONTENT, stream);
			try {
				while(filter.increment()){
				}
			} catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream=filter.getStream();
			a=stream.next().toString();
			if (flag == 1){b = "<"+b+">"; }
			
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				TermMap = IRdrObj.getPostings(b1);
				if (TermMap == null){
					Oprnd2List.addAll(FullDocidslist);
				}
				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd2List = new LinkedList<String>(Arrays.asList(arrtm));
					FullDocidslistClone.removeAll(Oprnd1List);
					Oprnd2List.addAll(FullDocidslistClone);
				}
				//retrieve a link list of string for all doc ids corresponding to b1 ----- (1)
					// if "b1" is not present in dictionary return an empty linklist<string> 
					// else output the difference of full doc ids list and list retrieved in (1)
				// output goes in Oprnd2List of type "linklist<string>"
			}
			else {
				TermMap = IRdrObj.getPostings(b);
				if (TermMap == null){
					Oprnd2List.addAll(EmptyDocidslist);
				}

				else {	
//					String[] arrtm = (String[]) TermMap.keySet().toArray();
					String[] arrtm = Arrays.copyOf(TermMap.keySet().toArray(), TermMap.keySet().toArray().length, String[].class);
//					String[] arrtm = new String[TermMap.keySet().size()];
//					int index1 =0;
//					for(int s:IRdrObj.revDocMap.keySet()){
//						arrtm[index1]=Integer.toString(s);
//						index1++;
//					}
					Oprnd2List = new LinkedList<String>(Arrays.asList(arrtm));
					
				}
				// TermMap = IRdrObj.getPostings(b);
				// convert this to string array for the key values
				// convert this string array to linkedlist<string> of docids
				// o/p this linkedlist<string> in Oprnd2List
				// if termmap is empty pass an empty linklist<string> through Oprnd2List
			}
			//collesction.sort(Oprnd2List)
		}
		
		else { 
			if (b.contains("Author:") || b.contains("author:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of author */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.AUTHOR);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(7);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else if(b.contains("Place:") || b.contains("place:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of place */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.PLACE);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(6);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else if(b.contains("Category:") || b.contains("category:")){/*define the hashmap Tdocid<String, linklist<string> as a hashmap of category */
				try {
					String termLower = "";
					TDocid2 = IRdrObj.returnMap(IndexType.CATEGORY);
					for(String term:TDocid2.keySet()){
						termLower = term.toLowerCase();
						LinkedList<String> list = new LinkedList<String>();
						for(String doc:TDocid2.get(term).keySet()){
							list.add(doc);
						}
						TDocid.put(termLower, list);
					}
				} catch (IndexerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				b = b.substring(9);
				if(b.contains(">")){
					 b= b.substring(1, b.length());
					 b = "<"+b;
				}
			}
			else {}
		
		if (b.contains("res#")){
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = resNumMap.get(b1);
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd2List = resNumMap.get(b); 
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = TDocid.get(b1.toLowerCase());
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
				
			}
			else { Oprnd2List = TDocid.get(b.toLowerCase()); 
					if (Oprnd2List == null){
						Oprnd2List = new LinkedList<String>();
						Oprnd2List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		
		if (b.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd2List);
			Oprnd2List.addAll(FullDocidslistClone);
		}
		Collections.sort(Oprnd2List);
		}
		
		
		Oprnd1ListClone.addAll(Oprnd1List);
		Oprnd2ListClone.addAll(Oprnd2List);
		

		Collections.sort(Oprnd1List);
		Collections.sort(Oprnd2List);

		Collections.sort(Oprnd1ListClone);
		Collections.sort(Oprnd2ListClone);
		
			for(String j:Oprnd1List){
				if(Oprnd2List.contains(j)){
					OrList.add(j);
					Oprnd1ListClone.remove(j);
					Oprnd2ListClone.remove(j);
				}
			}
			for (String t:Oprnd1ListClone){
				OrList.add(t);
			}
			for (String t:Oprnd2ListClone){
				OrList.add(t);
			}
			Collections.sort(OrList);
			//System.out.println(OrList);
			
			String orquer = "res#"+resCounter;
			resNumMap.put(orquer, OrList);
			return orquer;
		
	}
	
	public String TermQuoteRem(String x){
		x = x.replace("\"", "");
		return x;
	}


	
	
}
