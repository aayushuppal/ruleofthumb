package edu.buffalo.cse.irf14.analysis;

import java.util.HashMap;

/*
 * Any number that is not a date should be removed.
 */
public class NumericTokenFilter extends TokenFilter{

	TokenStream localstream;
	public NumericTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		Token token;
		String text;
		HashMap<Integer,String> punctMap = new HashMap<Integer, String>();
		int tokenCounter=0;
		try {
			while(increment()){
				tokenCounter++;
				token=localstream.next();
//				System.out.println(token.getTermText());
				if(token.getTermText().endsWith(".")){
					punctMap.put(tokenCounter, ".");
				}
				if(token.getTermText().endsWith(",")){
					punctMap.put(tokenCounter, ",");
				}
				text=token.getTermText().replaceAll("[,.]", "");
				if(text.matches("^\\d+$")){
//					System.out.println("lol");
					if(text.length()==8 && Integer.parseInt(text.substring(4,6))<13 &&Integer.parseInt(text.substring(6,8))<32){
						//ignore, because this is our filtered date from prev filter
					}
					else
					{
						text=text.replaceAll("[0-9]+", "");
						
					}
				}
					token.setTermText(text);
				
			}
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localstream.reset();
		Token t1;
		for(int i=0;i<tokenCounter;i++){
			t1=localstream.next();
			if(!t1.getTermText().equals("")){
				if(punctMap.containsKey(i+1)){
					if(!t1.getTermText().endsWith(punctMap.get(i+1))){
						t1.setTermText(t1.getTermText()+punctMap.get(i+1));
//						System.out.println(t1.getTermText());
					}
				}
				
			}
			else{
				localstream.remove();
				
			}
		}
		localstream.reset();
		// TODO Auto-generated method stub
		super.tokenstream=localstream;
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
