/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

/**
 * @author nikhillo
 * Wrapper exception class for any errors during Tokenization
 */
public class TokenizerException extends Exception {
	public TokenizerException() {super();}
	public TokenizerException(String s){super(s);}
	public TokenizerException(String s, Throwable c) {super(s,c);}
	public TokenizerException(Throwable c) {super(c);}
	/**
	 * 
	 */
	private static final long serialVersionUID = 215747832619773661L;

}
