/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author nikhillo
 * Static parser that converts raw text to Query objects
 */
public class QueryParser {
	
	/**
	 * MEthod to parse the given user query into a Query object
	 * @param userQuery : The query to parse
	 * @param defaultOperator : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) {
		
		final ArrayList<String> OPERATORS =new ArrayList<String>();
		OPERATORS.add("OR"); 
		OPERATORS.add("AND");
		OPERATORS.add("NOT");
		
		String queryTerms[]=userQuery.split(" ");
		String preWord="",preWord2="";
		LinkedList<String> queryList = new LinkedList<String>();
		LinkedList<String> queryList2 = new LinkedList<String>();
		LinkedList<String> newQueryList = new LinkedList<String>();
		for(String s:queryTerms) queryList.add(s);
		String word="";
/*-------------------
 * Strip All Brackets
 * -----------------*/
		List<Integer> openBrackets= new ArrayList<Integer>();
		List<Integer> closeBrackets= new ArrayList<Integer>();
//		for(int i=0;i<queryList.size();i++){
//			String s= queryList.get(i);
//			while(s.startsWith("(")){
//				break;
//			}
//		}
/*-----------------------
 * Combining Quoted Terms
 * ---------------------*/		
		Boolean quoteFlag=false;
		for(int i=0; i<queryList.size();i++){
			String s=queryList.get(i);
		if((s.startsWith("\"") || s.startsWith("Author:\"")|| s.startsWith("Category:\"") || s.startsWith("Term:\"") ||s.startsWith("Place:\"")  ) && quoteFlag==false){
			queryList2.add(s);
			quoteFlag=true;
		}
		else if(s.endsWith("\"") && quoteFlag==true){
			s=queryList2.getLast()+" "+s;
			queryList2.removeLast();
			queryList2.add(s);
			quoteFlag=false;
		}
		else if(quoteFlag==true) {
			s=queryList2.getLast()+" "+s;
			queryList2.removeLast();
			queryList2.add(s);
		}
		else queryList2.add(s);
		}
		queryList=queryList2;
/*--------------------------
* Add "Term:" where required. 
* -------------------------*/

				for(int i=0; i<queryList.size();i++){
					String s=queryList.get(i);
					if(!(quoteFlag==true || s.startsWith("Terms:")|| s.startsWith("Author:") || s.startsWith("Place:")|| s.startsWith("Category:") || OPERATORS.contains(s))){
						s="Term:"+s;
						queryList.add(i, s);
						queryList.remove(i+1);
					}
				}
/*------------------------------------
 * Add Default Operators where required. 
 * ----------------------------------*/
		
		for(int i=0; i<queryList.size();i++){
			if(i==0){ newQueryList.add(queryList.get(0)); continue;}
			word=queryList.get(i);
			//System.out.println(word);
			if(!OPERATORS.contains(word)){  //  word = TERM
				preWord=queryList.get(i-1);
				if(preWord.equals(("NOT")) && i>1){ //prev word= NOT
					preWord2=queryList.get(i-2);
					if(OPERATORS.contains(preWord2)){ // prev of prev word is not any operator
//						newQueryList.add(newQueryList.size()-1,defaultOperator);
						newQueryList.add(word);
						continue;// add default operator before NOT
					}
					else{
						newQueryList.add(newQueryList.size()-1,"AND");
						newQueryList.add(word);
					}
				}
				else if(OPERATORS.contains(preWord)){  // preWord is an operator , ignore
					newQueryList.add(word);
					continue;
				}
				else if(i>0){
					newQueryList.add(defaultOperator);
					newQueryList.add(word);
					continue;
				}
			}
			else newQueryList.add(word);

		}

		for(String s: newQueryList) System.out.print(s+" ");
		System.out.println();
		return null;
	}
}