package edu.buffalo.cse.irf14.analysis;
/*
 * A stemmer that replaces words with their stemmed versions.
 */
public class StemmerTokenFilter extends TokenFilter{
	TokenStream localstream;	
	PorterStemmer psobj = new PorterStemmer();
	
	public StemmerTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		
	}

	@Override
	public void filter() {
		localstream.reset();
		Token token;
		
		try {
			while(increment()){
			token=localstream.next();
			
			char tokenchar[] = token.getTermBuffer();
			psobj.add(tokenchar, tokenchar.length);
			psobj.stem();
			String text = psobj.toString();
			token.setTermText(text);
			}
		}
		catch (TokenizerException e) {
			e.printStackTrace();
		}
		
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
