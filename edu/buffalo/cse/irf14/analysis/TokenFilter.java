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
	
	TokenStream tokenstream;
	TokenFilter nextFilter;
	public TokenFilter(TokenStream stream) {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		tokenstream=stream;
	}
	
	public abstract void filter();
	
	public void setNextFilter(TokenFilter filter){
		nextFilter=filter;
	}
	
	public TokenFilter getNextFilter(){
		return nextFilter;
	}
	
	public TokenStream getStream() {
		return tokenstream;
	}
	
	public boolean increment() throws TokenizerException {
		filter();
		if(this.nextFilter!=null){
			
			return this.nextFilter.increment();
		}
		else 
			return false;
	}
	
}
