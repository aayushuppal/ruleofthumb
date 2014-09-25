/**
 * 
 */
package edu.buffalo.cse.irf14.index;

/**
 * @author nikhillo
 * Generic wrapper exception class for indexing exceptions
 */
public class IndexerException extends Exception {

	public IndexerException() {super();}
	public IndexerException(String s){super(s);}
	public IndexerException(String s, Throwable c) {super(s,c);}
	public IndexerException(Throwable c) {super(c);}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3012675871474097239L;

}
