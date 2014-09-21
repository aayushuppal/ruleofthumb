package edu.buffalo.cse.irf14.analysis;

import java.text.Normalizer;
import java.util.regex.Pattern;

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

/*** Template for all filter methods ***/	
//	public TokenStream filter() {
//		Token token;
//		try {
//			while(increment()){
//				token=localstream.next();
//				String text=token.getTermText();
//				
//			}
//		}
//		catch (TokenizerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//		// TODO Auto-generated method stub
//		return localstream;
//	}
	@Override
	public TokenStream filter() {
		Token token;
		String nfdNormalizedString;
		try {
			while(increment()){
				token=localstream.next();
				String text=token.getTermText();
				text = Normalizer.normalize(text, Normalizer.Form.NFD); 
				text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				token.setTermText(text);
			}
		}
		catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		// TODO Auto-generated method stub
		return localstream;
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
