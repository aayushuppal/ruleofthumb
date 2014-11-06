package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
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
		System.out.println("Loading the data..");
		System.out.println("----------------------------");
		System.out.println();
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
//		System.out.println("Query Mode started..");
//		System.out.println("Enter your Query:");
		long startTime = System.nanoTime();
		queryString=userQuery;
		query=queryParser.parse(queryString, "OR");
		String asdf=query.toString();
		LinkedList<String> result=indexSearcher.search(query);
		TreeMap<String,String[]> resultMap =new ResultFormat(result,query,indexReader).Result();
		TFScorer scorer1;
		OKScorer scorer2;
		if(model == ScoringModel.TFIDF){
			TFScorer scorer = new TFScorer(resultMap, query,indexReader);
			int index = 0;
			System.out.println("Query: "+asdf);
			for(Entry<String, Double> entry : scorer.result().entrySet()){
//				System.out.println(Integer.parseInt(entry.getKey()));
				printstream.println("Rank:"+(index+1)+"\nDoc ID:"+entry.getKey()+"\nRelevance Score: "+entry.getValue()+"\n");
				File f = new File(corpusDir+File.separator+entry.getKey());
				if(f.exists()){
					try {
						BufferedReader fstream= new BufferedReader(new FileReader(f));
						String s11 = fstream.readLine();
						while(s11.equals(null) || s11.trim().equals("")){
							s11 = fstream.readLine();
						}
						System.out.println(s11);
						s11 = fstream.readLine();
						while(s11.equals(null) || s11.trim().equals("")){
							s11 = fstream.readLine();
						}
						System.out.println(s11);
						s11 = fstream.readLine();
						System.out.println(s11);
						s11 = fstream.readLine();
						System.out.println(s11);
						System.out.println("---------------------------------------------------------------");
						System.out.println();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				index++;
				if(index==10) break;
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.printf("\n\nDuration of Execution: %fms",(double)duration/1000000);
		}
		else{
			OKScorer scorer = new OKScorer(resultMap, query,indexReader);
			int index = 0;
			System.out.println("Query: "+asdf);
			for(Entry<String, Double> entry : scorer.result().entrySet()){
//				System.out.println(Integer.parseInt(entry.getKey()));
				printstream.println("Rank:"+(index+1)+"\n Doc ID:"+entry.getKey()+"\nRelevance:"+entry.getValue()+"\n");
				File f = new File(corpusDir+File.separator+entry.getKey());
				if(f.exists()){
					try {
						BufferedReader fstream= new BufferedReader(new FileReader(f));
						String s11 = fstream.readLine();
						while(s11.equals(null) || s11.trim().equals("")){
							s11 = fstream.readLine();
						}
						System.out.println(s11);
						s11 = fstream.readLine();
						while(s11.equals(null) || s11.trim().equals("")){
							s11 = fstream.readLine();
						}
						System.out.println(s11);
						s11 = fstream.readLine();
						System.out.println(s11);
						s11 = fstream.readLine();
						System.out.println(s11);
						System.out.println("------------------------------------------------");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				index++;
				if(index==10) break;
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.printf("\nDuration of Execution: %fms\n",(double)duration/1000000);
		}
		
//		printstream.print(scorer.result());
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
		String resultString="numResults="+queryCount;
		ArrayList<String> resultList = new ArrayList<String>();
		if(queryCount>0){
			for(int i=0;i<queryCount;i++){
				queryString=reader.readLine();
				String qid = queryString.substring(0, 8);
				queryString=queryString.substring(8);
				queryString=queryString.replace("{","");
				queryString=queryString.replace("}", "");
				query=queryParser.parse(queryString, "OR");
				String s =query.toString();
//				System.out.println(s);
				LinkedList<String> result1=indexSearcher.search(query);
				TreeMap<String,String[]> resultMap =new ResultFormat(result1,query,indexReader).Result();
//				String s1[] = resultMap.firstEntry().getValue();
				TFScorer scorer = new TFScorer(resultMap, query,indexReader);
//				OKScorer scorer = new OKScorer(resultMap, query,indexReader);
				
				if(!scorer.result().isEmpty()){
					LinkedHashMap<String,Double> temp = new LinkedHashMap<String,Double>();
					int i1 = 0;
					for(Entry<String, Double> entry :scorer.result().entrySet()){
						if(i1>9) break;
						temp.put(entry.getKey(), entry.getValue());
					}
					resultList.add(qid+temp.toString().replace("=", "#"));
				}
			}
			printstream.println("numResults="+resultList.size());
			for(String s: resultList){
				printstream.println(s);
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