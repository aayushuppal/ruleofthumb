package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;

/*
 * 	Any character that is not a alphabet or a number and does not fit the above rules should be removed.
 */
public class SpecialCharsTokenFilter extends TokenFilter{
	TokenStream localstream;
	public SpecialCharsTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		Token token;
		String text;
		TokenStream ts;
		ArrayList<String> list = new ArrayList();
		ArrayList<Token> list2=new ArrayList();
		try {
			while(increment()){
				token=localstream.next();
				text=token.getTermText();
				text=text.replaceAll("[^A-Za-z0-9 .-]","");
//				text=text.replaceAll("\\P{Alnum}","");
//				text=text.replaceAll(":", "");
//				text=text.replaceAll(";", "");
//				text=text.replaceAll("/", "");
				if(text.contains("-")) {
					
					if(text.matches("[0-9]+-[0-9]+")) {}
//					else if(text.matches("^[^A-Za-z0-9]+[A-Za-z]+")) {}
					else if(text.startsWith("-")){}
					else {text=text.replaceAll("[^A-Za-z0-9]+", "");}
					
				}
					list.add(text);
			}
				for(String s:list){
					Token t=new Token();
					if(!s.trim().equals(""))
						{
						t.setTermText(s);
						list2.add(t);
						}
					
				}
				localstream= new TokenStream(list2);
				super.tokenstream=localstream;
		
		}
		catch (TokenizerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
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
