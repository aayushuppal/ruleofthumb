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
		
	}

	@Override
	public void filter() {
		
		localstream.reset();
		Token token;
		
		while(localstream.hasNext()){
		token=localstream.next();
		
		char tokenchar[] = token.getTermBuffer();
		if(Character.isLetter(tokenchar[0])){
		psobj.add(tokenchar, tokenchar.length);
		psobj.stem();
		String text = psobj.toString();
		token.setTermText(text);
		}
		}
		localstream.reset();
	}

}
