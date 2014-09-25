package edu.buffalo.cse.irf14.analysis;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolTokenFilter extends TokenFilter{
	TokenStream localstream;
	List<String> contractionList= Arrays.asList("'m","'ll","'d","'re","'s","n't","'ve");
	public SymbolTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		Token token;
		try {
			while(increment()){
				token=localstream.next();
				String text=token.getTermText();
				
				//Remove sentence ending period "."
				while((text.endsWith(".") ||text.endsWith("!")||text.endsWith("?")) && text.length()>1){
					text=text.substring(0, text.length()-1);
				}
				
				// Convert Standard Contractions
				for(int i=0;i<contractionList.size();i++){
					if(text.endsWith(contractionList.get(i))){
						if(i==0) text=text.substring(0,text.length()-2)+" "+"am";
						if(i==1) text=text.substring(0,text.length()-3)+" "+"will";
						if(i==2) text=text.substring(0,text.length()-2)+" "+"would"; 
						if(i==3) text=text.substring(0,text.length()-3)+" "+"are"; 	
						if(i==5) {
							text=text.substring(0,text.length()-3);
							if (text.equalsIgnoreCase("wo")) text="will not";
							else if (text.equalsIgnoreCase("sha")) text="shall not";
							else if (text.equalsIgnoreCase("ai")) text="am not";
							else text=text+" "+"not";
							}
						if(i==6) text=text.substring(0,text.length()-3)+" "+"have";
						if(i==4) {
							if(text.split("'s")[0].equalsIgnoreCase("it")| text.split("'s")[0].equalsIgnoreCase("He")| text.split("'s")[0].equalsIgnoreCase("she")|text.split("'s")[0].equalsIgnoreCase("Where")|text.split("'s")[0].equalsIgnoreCase("when")|text.split("'s")[0].equalsIgnoreCase("where")|text.split("'s")[0].equalsIgnoreCase("who")|text.split("'s")[0].equalsIgnoreCase("why")|text.split("'s")[0].equalsIgnoreCase("how"))
								text=text.substring(0,text.length()-2)+" "+"is";
						}
					}
				}
				
				//Removes other "'"
				if(text.contains("'s")) text=text.replace("'s", "");
				
//				if(text.endsWith("s'")) text=text.replace("'s", "");
				if(text.equals("'em")) text="them";
				if(text.contains("'")) text=text.replace("'", "");
				
				
				Pattern p = Pattern.compile(".*\\d.*");
				Matcher m=p.matcher(text);
				
				// Hyphen related rules
				if(text.contains("-")&&!text.contains("+")&&!text.contains("@")&&!text.contains("=")){
					{
					if(m.matches()){
						//do nothing
					}
					else if(text.matches("(\\s-)|(-\\s)")){
						text=text.replaceAll("-", "");
					}
					else{
						text=text.replaceAll("-", " ");
					}
				}
				}
				if  (text.trim().equals("")) localstream.remove();
				else token.setTermText(text.trim());
				
			}
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localstream.reset();
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
/* 	It should act on the following symbols with actions as 
	described:
*  	Any punctuation marks that possibly mark the end of a sentence (. ! ?) should be removed.
	Obviously if the symbol appears within a token it should be retained (a.out for example).
*  	Any possessive apostrophes should be removed (�s s� or just � at the end of a word). 
	Common contractions should be replaced with expanded forms but treated as one token. (e.g. 
	should�ve => should have). All other apostrophes should be removed.
*  	If a hyphen occurs within a alphanumeric token it should be retained (B-52, at least one 
	of the two constituents must have a number). If both are alphabetic, it should be replaced with 
	a whitespace and retained as a single token (week-day => week day). Any other hyphens 
	padded by spaces on either or both sides should be removed.
*/
	
}
