/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

/**
 * @author nikhillo
 * Class that converts a given string into a {@link TokenStream} instance
 */
public class Tokenizer {
	/**
	 * Default constructor. Assumes tokens are whitespace delimited
	 */
	String delim;
	public Tokenizer() {
		this.delim=" ";
		//TODO : YOU MUST IMPLEMENT THIS METHOD
	}
	
	/**
	 * Overloaded constructor. Creates the tokenizer with the given delimiter
	 * @param delim : The delimiter to be used
	 */
	public Tokenizer(String delim) {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		this.delim=delim;
	}
	
	/**
	 * Method to convert the given string into a TokenStream instance.
	 * This must only break it into tokens and initialize the stream.
	 * No other processing must be performed. Also the number of tokens
	 * would be determined by the string and the delimiter.
	 * So if the string were "hello world" with a whitespace delimited
	 * tokenizer, you would get two tokens in the stream. But for the same
	 * text used with lets say "~" as a delimiter would return just one
	 * token in the stream.
	 * @param str : The string to be consumed
	 * @return : The converted TokenStream as defined above
	 * @throws TokenizerException : In case any exception occurs during
	 * tokenization
	 */
	public TokenStream consume(String str) throws TokenizerException {
		//TODO : YOU MUST IMPLEMENT THIS METHOD
		Token tok ;
		TokenStream ts;
		ArrayList<Token> arrListTok =new ArrayList<Token>();
		if(str==null) throw new TokenizerException();
		if(str.trim().equals("")) throw new TokenizerException();
		if(str.trim().equals(delim)) throw new TokenizerException();
		String tokArr[]=str.split(delim);
		for(String s:tokArr){
			tok=new Token();
			tok.setTermText(s);	
			arrListTok.add(tok);
			}
		ts=new TokenStream(arrListTok);
		return ts;
	}
}
