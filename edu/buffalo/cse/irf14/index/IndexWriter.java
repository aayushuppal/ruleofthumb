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
		try {
			stream = tokenizer.consume(d.getField(FieldNames.CONTENT)[0]);
			filter = (TokenFilter)aFactory.getAnalyzerForField(FieldNames.CONTENT, stream);
//			filter.filter();
			stream=filter.getStream();
			while(stream.hasNext()){
				token=stream.next();
				System.out.println(token.getTermText());
			}
//			IndexerFactory.getInstance().getClassForIndex(IndexType.TERM).write(myStream, d.getField(FieldNames.FILEID)[0]);//hardcoded 0
		} catch (TokenizerException e) {
			throw new IndexerException();
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
