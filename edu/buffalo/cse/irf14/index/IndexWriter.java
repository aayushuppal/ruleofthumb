/**
 * 
 */
package edu.buffalo.cse.irf14.index;

import java.lang.reflect.Array;
import java.util.Arrays;

import edu.buffalo.cse.irf14.analysis.*;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * Class responsible for writing indexes to disk
 */
public class IndexWriter {
	
	String indexDir;
	/**
	 * Default constructor
	 * @param indexDir : The root directory to be sued for indexing
	 */
	public IndexWriter(String indexDir) {
		indexDir=this.indexDir;
		//TODO : YOU MUST IMPLEMENT THIS
	}
	
	/**
	 * Method to add the given Document to the index
	 * This method should take care of reading the filed values, passing
	 * them through corresponding analyzers and then indexing the results
	 * for each indexable field within the document. 
	 * @param d : The Document to be added
	 * @throws IndexerException : In case any error occurs
	 * @throws TokenizerException 
	 */
	public void addDocument(Document d) throws IndexerException, TokenizerException {
		String s;
		Token token;
		Tokenizer tokenizer = new Tokenizer(" ");
		TokenFilter filter;
		TokenStream stream;
		AnalyzerFactory aFactory=AnalyzerFactory.getInstance();
		s=d.getField(FieldNames.TITLE)[0];
		stream=tokenizer.consume(s);
//		System.out.println(stream.next().getTermText());
		DateTokenFilter stf = new DateTokenFilter(stream);
		stream=stf.filter();
		stream.reset();
		while(stream.hasNext()){
			System.out.println(stream.next().getTermText());
		}
		//TODO : YOU MUST IMPLEMENT THIS
	}
	
	/**
	 * Method that indicates that all open resources must be closed
	 * and cleaned and that the entire indexing operation has been completed.
	 * @throws IndexerException : In case any error occurs
	 */
	public void close() throws IndexerException {
		//TODO
	}
}
