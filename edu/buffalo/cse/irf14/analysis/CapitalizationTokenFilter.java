package edu.buffalo.cse.irf14.analysis;
/*
 * 	All tokens should be lowercased unless:
 *	The whole word is in caps (AIDS etc.) and the whole sentence is not in caps
 *	The word is camel cased and is not the first word in a sentence. 
 *  If adjacent tokens satisfy the above rule, they should be combined into a single token (San Francisco, Brad Pitt, etc.)
 *  */
public class CapitalizationTokenFilter extends TokenFilter{
	TokenStream localstream;
	public CapitalizationTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void filter() {
		// TODO Auto-generated method stub
		localstream.reset();
		Token token;
		int counter = 0;
		
			while(localstream.hasNext()){
				token = localstream.next();
				String text = null;
				String tokenAsString = token.getTermText();
				String tokenStringUpperCase = tokenAsString.toUpperCase();
				char tokenAsCharArr[] = token.getTermBuffer();
				char lowercasefirstchar = Character.toLowerCase(tokenAsCharArr[0]);
				char uppercasefirstchar = Character.toUpperCase(tokenAsCharArr[0]);
				
				
				if(localstream.hasNext()){
					token = localstream.next();
					int index1 =localstream.arrListToken.indexOf(token);
				String nexttokenAsString = token.getTermText();
				String nexttokenStringUpperCase = nexttokenAsString.toUpperCase();
				char nexttokenAsCharArr[] = token.getTermBuffer();
				char nextTlowercasefirstchar = Character.toLowerCase(tokenAsCharArr[0]);
				char nextTuppercasefirstchar = Character.toUpperCase(tokenAsCharArr[0]);
				
				localstream.reset();
				for (int k=0; k<index1; k++){
					token = localstream.next();
				}
				

				if (counter == 0){ 
					if(tokenAsString == tokenStringUpperCase){
						text = tokenAsString;
						token.setTermText(text);
					}
					else {	
						if(checkCamelCase(nexttokenAsString)){
							text = tokenAsString + " "+ nexttokenAsString;
							token.setTermText(text);
							token = localstream.next();
							localstream.remove();
							counter++;
						}
						else { text = tokenAsString.toLowerCase(); 
						token.setTermText(text);
						 }
					} 
				}
				
				else { 
					if(tokenAsString == tokenStringUpperCase){
						text = tokenAsString;
						token.setTermText(text);
					}
					else if(checkCamelCase(tokenAsString)){ 
						if(checkCamelCase(nexttokenAsString)){ 
							text = tokenAsString + " " +nexttokenAsString;
							token.setTermText(text);
							token = localstream.next();
							localstream.remove();
							counter++;
						}
						else { text = tokenAsString;
						token.setTermText(text); }
					}

					else {  text = tokenAsString.toLowerCase();
					token.setTermText(text);}
					
				}
			}
				else{
					if(tokenAsString == tokenStringUpperCase){
						text = tokenAsString;
						token.setTermText(text);
					}
					else if(checkCamelCase(tokenAsString)){
						text = tokenAsString; 
						token.setTermText(text);
					}

					else {  text = tokenAsString.toLowerCase();
					token.setTermText(text);}

					
				}

				counter++;
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

	public boolean checkCamelCase(String abc){
		char[] strToArray = abc.toCharArray();

		for(int i = 0; i<strToArray.length; i++){
		char u = Character.toUpperCase(strToArray[i]);
		
		if(strToArray[i] == u){return true;}
			
		}
		return false;
	}
	
}
