/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

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
		
		String queryTerms[]=userQuery.split(" "); // created a LinkedList by splitting the query with space
		String preWord="",preWord2="";
		LinkedList<String> queryList = new LinkedList<String>(); 
		LinkedList<String> queryList2 = new LinkedList<String>();// just a copy of querylist
		LinkedList<String> newQueryList = new LinkedList<String>();//  another copy of querylist
		for(String s:queryTerms) queryList.add(s);
		String word="";

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
		
			
/*---------------------------------------------------------------------------------------------------------
* Add "Term:" where required. (Actually adding Ter##m: because we need these terms for cross index grouping)
* ---------------------------------------------------------------------------------------------------------*/

Pattern p1=Pattern.compile("");
				for(int i1=0; i1<queryList.size();i1++){

					String s1=queryList.get(i1);
					if(!(quoteFlag==true || s1.contains("Term:")|| s1.contains("Author:") || s1.contains("Place:")|| s1.contains("Category:") || s1.equals("OR")||s1.equals("AND") || s1.equals("NOT")||s1.matches("(\\()?\\(NOT"))){
						s1="Ter##m:"+s1;
						queryList.add(i1, s1);
						queryList.remove(i1+1);
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
				if((preWord.equals(("NOT")) || preWord.equals(("(NOT"))) && i>1){ //prev word= NOT
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
/*-----------------------------------------*
* Do the required refining due to Brackets *
* -----------------------------------------*/
				List<Integer> openBrackets= new ArrayList<Integer>();
				List<Integer> closeBrackets= new ArrayList<Integer>();
				Boolean openFlag=false;
				for(int i=0;i<newQueryList.size();i++){
					String s=newQueryList.get(i);
					if(s.contains("(")){
							openFlag=true;
							openBrackets.add(i);
							s="("+s.split("\\(")[0]+s.split("\\(")[1];
							newQueryList.remove(i);
							newQueryList.add(i, s);
					}
					if(s.contains(")") && (s.contains("Term:") || s.contains("Ter##m:"))){
						
						String g=newQueryList.get(openBrackets.get(openBrackets.size()-1));
						if(g.contains("(") && g.contains(":")){
							s=g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
							newQueryList.remove(i);
							newQueryList.add(i, s);
						}
						openFlag=false;
					}
					if(openFlag==true){
						if((s.contains("Term:") || s.contains("Ter##m:"))){
							
							String g=newQueryList.get(openBrackets.get(openBrackets.size()-1));
							if(g.contains("(") && g.contains(":")){
								if(s.contains("(((("))
									s="(((("+g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
								else if(s.contains("((("))
									s="((("+g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
								else if(s.contains("(("))
								s="(("+g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
								else if(s.contains("("))
									s="("+g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
								else
									s=g.split(":")[0].split("\\(")[1]+":"+s.split(":")[1];
								newQueryList.remove(i);
								newQueryList.add(i, s);
							}
						}
					}
					
				}
				/*--------------------------------------------------
				* Add brackets for cross index grouping 
				* i.e. Author:lala Term:Utsav Term:Parth Term:Nana
				* --> Author:lala (Term:Utsav Term:Parth Term:Nana)
				* -------------------------------------------------*/
						Boolean crossIndexFlag=false;
						int crossIndexCount=0;
						int startIndex = 0,endIndex;
						String lastIndexType = null;
						String nextIndexType = null;
						for(int i=0; i<newQueryList.size();i++){
							String s=newQueryList.get(i);

						
						if(i>0 && newQueryList.get(i-1).contains(":")) {
							lastIndexType=newQueryList.get(i-1).replaceAll("\\(", "").split(":")[0];
//							System.out.println(lastIndexType);

						}
						if(i!=newQueryList.size()-1 && newQueryList.get(i+1).contains(":")){
							nextIndexType=newQueryList.get(i+1).replaceAll("\\(", "").split(":")[0];

						}
						if(i>0 && !lastIndexType.equals("Ter##m") && s.contains("Ter##m")){
							crossIndexFlag=true;
							startIndex=i;
						}
						if(i>0 && crossIndexFlag==true&& s.contains("Ter##m")){
							crossIndexCount++;
						}
						if(crossIndexFlag==true && crossIndexCount>1 && (i==newQueryList.size()-1 || !nextIndexType.contains("Ter##m"))){
							String s1;
							s1=newQueryList.get(startIndex);
							if(!s1.contains("(")){
								if(newQueryList.get(startIndex-1).contains("(NOT")){
									
								}
								else if(newQueryList.get(startIndex-1).contains("NOT")){
									newQueryList.set(startIndex-1, "(NOT");
								}
								else
									s1="("+s1;
							}
							newQueryList.set(startIndex, s1);
							if(i==newQueryList.size()-1) {
								s1=newQueryList.get(i);
								if(!s1.endsWith(")")) s1=s1+")";
								newQueryList.set(i, s1);
							}
							else  {
								s1=newQueryList.get(i-1);
								if(!s1.endsWith(")")) s1=s1+")";
								newQueryList.set(i-1, s1);
							}
							
							crossIndexFlag=false;
						}
						}
						for(int i=0; i<newQueryList.size();i++){
							if(newQueryList.get(i).contains("Ter##m"))
								newQueryList.set(i, newQueryList.get(i).replace("Ter##m", "Term"));
						}
///* ------------------------------------------------------------------
//* Add starting and Ending brackets in query for easiness of next task
//* ------------------------------------------------------------------*/
//				String s=newQueryList.get(0);
//				System.out.println(s);
//				if (!s.startsWith("(")) {
//					newQueryList.remove(0);
//					newQueryList.add(0, "("+s);
//					s=newQueryList.getLast();
//					s=s+")";
//					newQueryList.removeLast();
//					newQueryList.addLast(s);
//					}
				
///* ----------------------------------------
// * Creating Operation and Operator Stacks 
// * ---------------------------------------*/
//				String opr="";
//				Stack<String> oprtrStack = new Stack<String>();
//				Stack<String> oprndStack = new Stack<String>();
//				for(int i=0; i<newQueryList.size();i++){
//					String str= newQueryList.get(i);
//					if(!str.endsWith("\\)")){
//						if(OPERATORS.contains(str)) oprtrStack.push(str);
//						else oprndStack.push(str);
//					}
//					else{
//						do{
//							opr=
//						}
//					}
//				}

//		for(String s1: newQueryList) System.out.print(s1+" ");
		Query q= new Query();
		q.setQueryString(newQueryList);
		return q;
	}
}