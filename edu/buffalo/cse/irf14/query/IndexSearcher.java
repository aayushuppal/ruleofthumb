package edu.buffalo.cse.irf14.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import rot_beta.hmap;

public class IndexSearcher {
	
	static int resCounter = 0;
	static HashMap<String, LinkedList<String>> TDocid = new HashMap<String, LinkedList<String>>();
	static TreeMap<String, LinkedList<String>> resNumMap = new TreeMap<String, LinkedList<String>>();

	public IndexSearcher() {
		// TODO Auto-generated constructor stub
		
		
	}
	public static String search(Query query){
		ConvertToStack(query.getSplitQuery());
		return null;
		
	}
	
	public static void ConvertToStack(ArrayList<String> a){
		
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
	    
	    System.out.println(resNumMap.lastEntry().getValue());
	    
	    
	}
	
	public static String QueryProcess(String a){

		//System.out.println(TDocid.get("A"));
		
		//System.out.println("Query input here: "+a);
		String[] splitQuer = a.split("\\s+");
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
	

	public static String SingleQueryProcessor(String a){
		//Pass a through filters and show new "a"
		//Pass b through filters and show new "b"
		if (a.contains("Term:")){
			a = a.substring(5);
			TDocid
		}
		
		resCounter++;
		hmap hobj1 = new hmap();
		FullDocidslist.addAll(hobj1.GenerateFullDocidList()); 
		
		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> SingQuerList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist);
		
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
				Oprnd1List = TDocid.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
				
			}
			else { Oprnd1List = TDocid.get(a); 
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
			//System.out.println(AndList);
			
		String singquer = "res#"+resCounter;
		resNumMap.put(singquer, SingQuerList);
		return singquer;
	}

	public static String AndProcessor(String a, String b){
		//Pass a through filters and show new "a"
		//Pass b through filters and show new "b"
		resCounter++;
		hmap hobj1 = new hmap();
		FullDocidslist.addAll(hobj1.GenerateFullDocidList()); 
		
		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> Oprnd2List = new LinkedList<String>();
		LinkedList<String> AndList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist);
		
		if (a.contains("res#")){
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = resNumMap.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { Oprnd1List = resNumMap.get(a); 
					if (Oprnd1List == null){
						Oprnd1List = new LinkedList<String>();
						Oprnd1List.addAll(EmptyDocidslist);
					}
			}
		}
		else {
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = TDocid.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd1List = TDocid.get(a);	
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
		}
		
		
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
				Oprnd2List = TDocid.get(b1);
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
			else { 
				Oprnd2List = TDocid.get(b);	
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
		}
		
		if (a.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd1List);
			Oprnd1List.addAll(FullDocidslistClone);
		}
		
		if (b.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd2List);
			Oprnd2List.addAll(FullDocidslistClone);
		}
		
		Collections.sort(Oprnd1List);
		Collections.sort(Oprnd2List);
		
		
			for(String j:Oprnd1List){
				if(Oprnd2List.contains(j)){
					AndList.add(j);
				}
		}

			Collections.sort(AndList);
			//System.out.println(AndList);
			
		String andquer = "res#"+resCounter;
		resNumMap.put(andquer, AndList);
		return andquer;
	}

	public static String OrProcessor(String a, String b){
		//Pass a through filters and show new "a"
		//Pass b through filters and show new "b"
		resCounter++;
		hmap hobj1 = new hmap();
		FullDocidslist.addAll(hobj1.GenerateFullDocidList());
		
		LinkedList<String> Oprnd1List = new LinkedList<String>();
		LinkedList<String> Oprnd2List = new LinkedList<String>();
		LinkedList<String> Oprnd1ListClone = new LinkedList<String>();
		LinkedList<String> Oprnd2ListClone = new LinkedList<String>();
		LinkedList<String> OrList = new LinkedList<String>();
		LinkedList<String> FullDocidslistClone = new LinkedList<String>();
		FullDocidslistClone.addAll(FullDocidslist);
		
		if (a.contains("res#")){
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = resNumMap.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { Oprnd1List = resNumMap.get(a); 
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
		}
		else {
			if (a.startsWith("<")){
				String a1 = a.substring(1, a.length()-1);
				Oprnd1List = TDocid.get(a1);
				if (Oprnd1List == null){
					Oprnd1List = new LinkedList<String>();
					Oprnd1List.addAll(EmptyDocidslist);
				}
			}
			else { Oprnd1List = TDocid.get(a);
			if (Oprnd1List == null){
				Oprnd1List = new LinkedList<String>();
				Oprnd1List.addAll(EmptyDocidslist);
			}
			}	
		}
		
		
		if (b.contains("res#")){
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = resNumMap.get(b1);
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
			else { Oprnd2List = resNumMap.get(b); 
					if (Oprnd2List == null){
						Oprnd2List = new LinkedList<String>();
						Oprnd2List.addAll(EmptyDocidslist);
					}
			}
		}
		else {
			if (b.startsWith("<")){
				String b1 = b.substring(1, b.length()-1);
				Oprnd2List = TDocid.get(b1);
				if (Oprnd2List == null){
					Oprnd2List = new LinkedList<String>();
					Oprnd2List.addAll(EmptyDocidslist);
				}
			}
			else { Oprnd2List = TDocid.get(b); 
					if (Oprnd2List == null){
						Oprnd2List = new LinkedList<String>();
						Oprnd2List.addAll(EmptyDocidslist);
					}
			}	
		}
		
		if (a.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd1List);
			Oprnd1List.addAll(FullDocidslistClone);
		}
		
		if (b.startsWith("<")){
			FullDocidslistClone.removeAll(Oprnd2List);
			Oprnd2List.addAll(FullDocidslistClone);
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


	
	
}
