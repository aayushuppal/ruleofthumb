package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.query.IndexSearcher;
import edu.buffalo.cse.irf14.query.OKScorer;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;
import edu.buffalo.cse.irf14.query.ResultFormat;
import edu.buffalo.cse.irf14.query.TFScorer;

/**
 * Main class to run the searcher.
 * As before implement all TODO methods unless marked for bonus
 * @author nikhillo
 *
 */
public class SearchRunner {
	public enum ScoringModel {TFIDF, OKAPI};
//	String indexdir="C:/Users/Festy/Desktop/IR Slides/sample"; 

	String corpusDir,queryString;
	char queryMode;
	
	IndexReader indexReader;
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	TFScorer scorer;
	Query query;
	PrintStream printstream;
	ResultFormat resultFormat;
	
	
	
	
	/**
	 * Default (and only public) constuctor
	 * @param indexDir : The directory where the index resides
	 * @param corpusDir : Directory where the (flattened) corpus resides
	 * @param mode : Mode, one of Q or E
	 * @param stream: Stream to write output to
	 */
	public SearchRunner(String indexDir, String corpusDir, 
			char mode, PrintStream stream) {
		//TODO: IMPLEMENT THIS METHOD
		this.corpusDir=corpusDir;
		queryMode=mode;
		printstream=stream;
		indexReader=new IndexReader(indexDir,IndexType.TERM);
		indexSearcher= new IndexSearcher(indexReader);
	}
	
	
	
	
	/**
	 * Method to execute given query in the Q mode
	 * @param userQuery : Query to be parsed and executed
	 * @param model : Scoring Model to use for ranking results
	 */
	@SuppressWarnings("static-access")
	public void query(String userQuery, ScoringModel model) {
		//TODO: IMPLEMENT THIS METHOD
		queryString=userQuery;
		query=queryParser.parse(queryString, "OR");
		query.toString();
		LinkedList<String> result=indexSearcher.search(query);
		TreeMap<String,String[]> resultMap =new ResultFormat(result,query,indexReader).Result();
		TFScorer scorer = new TFScorer(resultMap, query);
		printstream.print(scorer.result());
	}
	
	
	
	
	/**
	 * Method to execute queries in E mode
	 * @param queryFile : The file from which queries are to be read and executed
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public void query(File queryFile) throws IOException {
		//TODO: IMPLEMENT THIS METHOD
		String result="";
		String[] resultArray = new String[100];
		String firstLine="";
		int queryCount = 0, resultCount=0;
		BufferedReader reader = new BufferedReader(new FileReader(queryFile));
		try{
			firstLine=reader.readLine();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		if(firstLine!=null && firstLine.contains("numQueries=")){
			queryCount=Integer.parseInt(firstLine.split("numQueries=")[1].trim());
		}
		if(queryCount>0){
			for(int i=0;i<queryCount;i--){
				queryString=reader.readLine();
				query=queryParser.parse(queryString, "OR");
				query.toString();
				LinkedList<String> result1=indexSearcher.search(query);
				TreeMap<String,String[]> resultMap =new ResultFormat(result1,query,indexReader).Result();
				TFScorer scorer = new TFScorer(resultMap, query);
				printstream.print(scorer.result());
			}
			
		}
		
	}
	
	
	
	
	/**
	 * General cleanup method
	 */
	public void close() {
		//TODO : IMPLEMENT THIS METHOD
	}
	
	
	
	
	/**
	 * Method to indicate if wildcard queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}
	
	
	
	
	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * @return A Map containing the original query term as key and list of
	 * possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		//TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;
		
	}
	
	
	
	
	/**
	 * Method to indicate if speel correct queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}
	
	
	
	
	/**
	 * Method to get ordered "full query" substitutions for a given misspelt query
	 * @return : Ordered list of full corrections (null if none present) for the given query
	 */
	public List<String> getCorrections() {
		//TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}
}