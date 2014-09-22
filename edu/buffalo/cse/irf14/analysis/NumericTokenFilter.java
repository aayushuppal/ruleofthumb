package edu.buffalo.cse.irf14.analysis;
/*
 * Any number that is not a date should be removed.
 */
public class NumericTokenFilter extends TokenFilter{

	TokenStream localstream;
	public NumericTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		// TODO Auto-generated constructor stub
	}

	@Override
	public TokenStream filter() {
		Token token;
		String text;
		try {
			while(increment()){
				token=localstream.next();
				text=token.getTermText().replaceAll("[,.]", "");
				if(text.matches(".*[0-9]+.*")){
//					System.out.println("lol");
					if(text.length()==8 && Integer.parseInt(text.substring(4,6))<13 &&Integer.parseInt(text.substring(6,8))<32){
						//ignore, because this is our filtered date from prev filter
					}
					else
					{
						text=text.replaceAll("[0-9]+", "");
						
					}
				}
				if(text.equals(""))	localstream.remove();
				else token.setTermText(text);
			}
		} catch (TokenizerException e) {
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
