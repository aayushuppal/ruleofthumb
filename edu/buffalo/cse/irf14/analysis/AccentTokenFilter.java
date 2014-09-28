package edu.buffalo.cse.irf14.analysis;

import java.text.Normalizer;

/*
 * 	All accents and diacritics must be removed by folding into the corresponding English characters.
 */
public class AccentTokenFilter extends TokenFilter{
	TokenStream localstream;
	public AccentTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		localstream.reset();
		Token token;
		while(localstream.hasNext()){
			token=localstream.next();
			String text=token.getTermText();
			text = Normalizer.normalize(text, Normalizer.Form.NFD); 
			text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			token.setTermText(text);
		}
		
	}





}
