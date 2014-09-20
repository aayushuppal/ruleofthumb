package edu.buffalo.cse.irf14.analysis;
/*
 * 	All tokens should be lowercased unless:
 *	The whole word is in caps (AIDS etc.) and the whole sentence is not in caps
 *	The word is camel cased and is not the first word in a sentence. 
 *  If adjacent tokens satisfy the above rule, they should be combined into a single token (San Francisco, Brad Pitt, etc.)
 *  */
public class CapitalizationTokenFilter extends TokenFilter{
	public CapitalizationTokenFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void filter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNextFilter(TokenFilter filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TokenFilter getNextFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
