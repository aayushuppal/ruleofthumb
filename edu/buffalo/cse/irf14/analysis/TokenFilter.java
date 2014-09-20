/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

/**
 * The abstract class that you must extend when implementing your 
 * TokenFilter rule implementations.
 * Apart from the inherited Analyzer methods, we would use the 
 * inherited constructor (as defined here) to test your code.
 * @author nikhillo
 *
 */
public abstract class TokenFilter implements Analyzer {
	/**
	 * Default constructor, creates an instance over the given
	 * TokenStream
	 * @param stream : The given TokenStream instance
	 */
	
	TokenStream tokenStream;
	public TokenFilter(TokenStream stream) {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		tokenStream=stream;
	}
	
	public abstract void filter();
	
	public abstract void setNextFilter(TokenFilter filter);
	
	public abstract TokenFilter getNextFilter();
	
	public TokenStream getStream() {
		return tokenStream;
	}
	
	public boolean increment() throws TokenizerException {
		if(tokenStream!=null && tokenStream.hasNext()) {
			return true;
		}
			return false;
	}
	
}
