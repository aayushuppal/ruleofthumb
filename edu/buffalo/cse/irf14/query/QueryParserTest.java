package edu.buffalo.cse.irf14.query;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.SearchRunner;
import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.index.IndexReader;
/////////////////////////////////
// infinite loop query tester////
/////////////////////////////////
public class QueryParserTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SearchRunner test = new SearchRunner("C:/Users/Festy/Desktop/IR Slides/sample", "C:/Users/Festy/Desktop/IR Slides/training_flat", 'Q', new PrintStream(System.out));
		test.query("coconut", ScoringModel.TFIDF);
		
//		Scanner terminalInput = new Scanner(System.in);
////		while(true)
//		{ 
//		System.out.println("Enter your Query:");
//		String s = terminalInput.nextLine();
//		Query q =QueryParser.parse(s, "OR");
////		System.out.println(q.toString());
////		q.getSplitQuery();
//		
//		IndexReader indexer =IndexFactory.getIndexReader("C:\\Users\\Festy\\Desktop\\IR Slides\\sample");
//		System.out.println(indexer.getPostings("wheat").keySet());
//		System.out.println(indexer.revDocMap.get(5024));
//		System.out.println(indexer.revDocMap.get(441));
//		System.out.println(indexer.revDocMap.get(444));
//		System.out.println(indexer.revDocMap.get(447));
//		System.out.println(indexer.revDocMap.get(1463));
//		System.out.println(indexer.revDocMap.get(1880));
//		IndexSearcher s1 = new IndexSearcher("C:\\Users\\Festy\\Desktop\\IR Slides\\sample");
//		s1.search(q);
////		String s2=q.toString();
//		
////		System.out.println(s2);
////		
////		/*Assume that the query to be scored is (A AND B) OR (C AND D)
////		 * The result returned by an IndexSearcher method will be a HashMap with
////		 * "Term">"Doc, FreqofTerm" as a pair------------------------------------*/
////		TreeMap<String,String[]> result= new TreeMap<String,String[]> ();
////		result.put("pull", new String[]{"0000005","2","0000007","5","0000002","1"});
////		result.put("push", new String[]{"0000007","1","0000005","2","0000006","1"});
////		result.put("world", new String[]{"000000","1","401001","2","601001","6","101001","2","801001","3"});
////		result.put("crop", new String[]{"101001","3","201001","4"});
//////		//TFScorer scr = new TFScorer(result,q);
////		TFScorer scr = new TFScorer(result,q);
//		}
		
	}

}
