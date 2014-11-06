package edu.buffalo.cse.irf14.query;

import java.io.File;
import java.io.IOException;
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SearchRunner test = new SearchRunner("/Users/utsavpatel/Desktop/sample", "/Users/utsavpatel/Desktop/sample/training_flat", 'E', new PrintStream(System.out));
		test.query("current financial quarter", ScoringModel.TFIDF);
//		test.query(new File("/Users/utsavpatel/Desktop/sample/123.txt"));
	}

}
