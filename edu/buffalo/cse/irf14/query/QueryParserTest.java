package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
/////////////////////////////////
// infinite loop query tester////
/////////////////////////////////
public class QueryParserTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner terminalInput = new Scanner(System.in);
//		while(true)
		{ 
		System.out.println("Enter your Query:");
		String s = terminalInput.nextLine();
		Query q =QueryParser.parse(s, "OR");
		String s2=q.toString();
		
		System.out.println(s2);
		
		/*Assume that the query to be scored is (A AND B) OR (C AND D)
		 * The result returned by an IndexSearcher method will be a HashMap with
		 * "Term">"Doc, FreqofTerm" as a pair------------------------------------*/
		TreeMap<String,String[]> result= new TreeMap<String,String[]> ();
		result.put("pull", new String[]{"0000005","2","0000007","5","0000002","1"});
		result.put("push", new String[]{"0000007","1","0000005","2","0000006","1"});
		result.put("world", new String[]{"000000","1","401001","2","601001","6","101001","2","801001","3"});
		result.put("crop", new String[]{"101001","3","201001","4"});
//		//TFScorer scr = new TFScorer(result,q);
		OKScorer scr = new OKScorer(result,q);
		}
		
	}

}
