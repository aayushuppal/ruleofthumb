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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		Token token;
		String text;
		HashMap<Integer,String> punctMap = new HashMap<Integer, String>();
		int tokenCounter=0;
		while(localstream.hasNext()){
			tokenCounter++;
			token=localstream.next();
			String s=token.getTermText();
//			if(!Character.isLetterOrDigit(s.charAt(s.length()-1))){
//				punctMap.put(tokenCounter, Character.toString(s.charAt(s.length()-1)));
//				s=s.substring(0, s.length()-1);
//			}
//				System.out.println(token.getTermText());
			if(token.getTermText().endsWith(".")){
				punctMap.put(tokenCounter, ".");
			}
			if(token.getTermText().endsWith(",")){
				punctMap.put(tokenCounter, ",");
			}
			if(token.getTermText().endsWith("?")){
				punctMap.put(tokenCounter, "?");
			}
			if(token.getTermText().endsWith("!")){
				punctMap.put(tokenCounter, "!");
			}
//			System.out.println(s);
			text=s.replaceAll("[^0-9a-zA-Z/%$&]", "");
//			System.out.println(text);
			if(text.matches("[^A-Za-z]+")){
//				System.out.println(text);
//					System.out.println("lol");
				if(!text.contains("/")&&!text.matches("[^0-9]")&&text.length()==8 && Integer.parseInt(text.substring(4,6))<13 &&Integer.parseInt(text.substring(6,8))<32){
					//ignore, because this is our filtered date from prev filter
				}
				else
				{
					
					text=text.replaceAll("[0-9]+", "");
					
				}
			}
				token.setTermText(text);
			
		}
		localstream.reset();
		Token t1;
		for(int i=0;i<tokenCounter;i++){
			
			if(localstream.hasNext()){
			t1=localstream.next();
//			System.out.println(t1.getTermText());
			if(t1.getTermText()!=null&&!t1.getTermText().equals("")){
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
		}
		localstream.reset();
		// TODO Auto-generated method stub
		
	}

	
	
}
