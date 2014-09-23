/**
 * 
 */
package edu.buffalo.cse.irf14.document;

/**
 * @author nikhillo
 * Generic wrapper exception class for parsing exceptions
 */
public class ParserException extends Exception {
	public ParserException() {super();}
	public ParserException(String s){super(s);}
	public ParserException(String s, Throwable c) {super(s,c);}
	public ParserException(Throwable c) {super(c);}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4691717901217832517L;

}
