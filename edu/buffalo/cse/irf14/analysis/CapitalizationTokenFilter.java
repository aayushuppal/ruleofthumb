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
		filter();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void filter() {
		// TODO Auto-generated method stub
		localstream.reset();
		Token token;
		
		int counter = 0;
		try{
			while(increment()){
				token = localstream.next();
				String tokeninput = token.toString();
				String token1 = tokeninput;
				if (counter == 0){
					if (tokeninput.toUpperCase() == tokeninput){
						// do nothing to token and return as all caps	
						counter++;
					}
					else {
						token = localstream.next();
						String token2 = token.toString();
						char nexttokenchar[] = token.getTermBuffer();
						char a = Character.toUpperCase(nexttokenchar[0]);
						
						if (a == nexttokenchar[0]){
							tokeninput = token1 + token2;
							counter++;
							counter++;
						}
						else tokeninput = token1.toLowerCase();					
						token = localstream.previous();
						counter++;
					}
				}
				
				else {
					token = localstream.previous();
					char prevtokenchar[] = token.getTermBuffer();
					int length = prevtokenchar.length;
					
					token = localstream.next();
					tokeninput = token.toString();
					token1 = tokeninput;
					
					if(prevtokenchar[length-1] == '.'){  //case where word is a sentence starter
						if(tokeninput.toUpperCase() == tokeninput){
						//do nothing to token and return as all caps
							counter++;
						}
						else { //implement camel case scenario for first word of sentence case
							token = localstream.next();
							String token2 = token.toString();
							char nexttokenchar[] = token.getTermBuffer();
							char a = Character.toUpperCase(nexttokenchar[0]);
							
							if (a == nexttokenchar[0]){
								tokeninput = token1 + token2;
								counter++;
								counter++;
							}
							else tokeninput = token1.toLowerCase();					
							token = localstream.previous();
							counter++;
						}
					}
					
					else { //not a sentence starter
						char token1char[] = token.getTermBuffer();
						char b = Character.toUpperCase(token1char[0]);
						
							if(token1.toUpperCase() == token1){
								//do nothing to token and return as all caps
								counter++;
							}
							else if (b == token1char[0]) {
								token = localstream.next();
								String token2 = token.toString();
								char nexttokenchar[] = token.getTermBuffer();
								char a = Character.toUpperCase(nexttokenchar[0]);
								
								if (a == nexttokenchar[0]){
									tokeninput = token1 + token2;
									counter++;
									counter++;
								}
								else tokeninput = token1.toLowerCase();					
								token = localstream.previous();
								counter++;
							}
							
							else tokeninput = token1.toLowerCase();					
							token = localstream.previous();
							counter++;
							
					}
					
				}
				token.setTermText(tokeninput);
			}
		}

		catch (TokenizerException e) {
			e.printStackTrace();
		}
		localstream.reset();
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
