package edu.buffalo.cse.irf14.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 	Any date occurrence should be converted to yyyymmdd format for dates and HH:mm:ss for time stamps (yyyymmdd HH:mm:ss for both combined). 
 *	Time zones can be ignored. The following defaults should be used if any field is absent:
	*	Year should be set as 1900.
	*	Month should be January
	*	Date should be 1st.
	*	Hour, minute or second should be 00.
*/
public class DateTokenFilter extends TokenFilter{
	/**Some bug, wrongly prints the middle symbol of two merging token, at the end of merged token*/
	TokenStream localstream;
	TokenStream ts;
	
	public DateTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		// TODO Auto-generated constructor stub
	}


	public void filter(){
		localstream.reset();
		Token token;
		String text,day="",year="";
		int month=0;
		String lastPunctuation="";
		boolean flag=false;
		Pattern pMonth= Pattern.compile("^(January|Jan|February|feb|March|mar|April|Apr|May|June|July|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)$",Pattern.CASE_INSENSITIVE);
		Pattern pDay= Pattern.compile("^\\d{1,2}$");
		Pattern pYear= Pattern.compile("^(\\d{4})$");
		Pattern pTime= Pattern.compile("\\d{1,2}(:\\d{1,2})?[ap]m.*",Pattern.CASE_INSENSITIVE);
		Matcher mDay,mMonth,mYear,mtime;
		int tokenCounter=0,monthTokenNumber=0,dayTokenNumber=0,yearTokenNumber=0,timeTokenNumber=0;
		String[] months={"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
		HashMap<Integer, String> monthMap= new HashMap();
		HashMap<Integer, String> dayMap= new HashMap();
		HashMap<Integer, String> yearMap=new HashMap();
		HashMap<Integer, String> timeMap=new HashMap();
		HashMap<Integer, String> allMap = new HashMap();
		HashMap<Integer, String> monthMapCopy = new HashMap();
		HashMap<Integer, String> dayMapCopy = new HashMap();
		HashMap<Integer, String> yearMapCopy = new HashMap();
		HashMap<Integer, String> allMapCopy = new HashMap();
		HashMap<Integer, String> monthMapCopy1 = new HashMap();
		HashMap<Integer, String> dayMapCopy1 = new HashMap();
		HashMap<Integer, String> yearMapCopy1 = new HashMap();
		HashMap<Integer, String> allMapCopy1 = new HashMap();
		HashMap<Integer, String> finalMap = new HashMap();
		HashMap<Integer, String> remainingMap=new HashMap();
		HashMap<Integer, String> symbolMap=new HashMap();
		try {
			while(increment()){
				token=localstream.next();
				text=token.getTermText();
				tokenCounter++;
				if(text.endsWith(",")) { 
					symbolMap.put(tokenCounter, ","); 
					text=text.replace(",", "");
					if(localstream.hasNext()==false){
						lastPunctuation=",";
					}
						
				}
				if(text.endsWith(".")) { 
					symbolMap.put(tokenCounter, "."); 
					text=text.replace(".", "");
					if(localstream.hasNext()==false){
						lastPunctuation=".";
					}
				}
//				System.out.println(text);
//				if(text.matches("\\d{4}-\\d{4}") || text.matches("\\d{4}-\\d{2}")) System.out.println("lol");
				mDay=pDay.matcher(text);
				mMonth=pMonth.matcher(text);
				mYear=pYear.matcher(text);
				mtime=pTime.matcher(text);
				
				
				if(mDay.matches()){
					dayTokenNumber=tokenCounter;
					day=mDay.group();
					dayMap.put(dayTokenNumber, (Integer.parseInt(day)/10)==0?"0"+day:day);
					}
				
				else if (mMonth.matches()){
					monthTokenNumber=tokenCounter;
					month=Arrays.asList(months).indexOf(mMonth.group().substring(0, 3).toLowerCase())+1;
					monthMap.put(monthTokenNumber,month/10==0?"0"+Integer.toString(month):Integer.toString(month));
				}
				else if(mtime.matches()){
					timeTokenNumber=tokenCounter;
					String adbc = mtime.group(0);
					String h,m;
					if(adbc.toLowerCase().contains("pm")){
						h=adbc.split(":")[0].toLowerCase().split("pm")[0];
//						System.out.println(h);
						h=Integer.toString(Integer.parseInt(h)+12);
						if(adbc.split(":").length==2)
							m=adbc.split(":")[1].toLowerCase().split("pm")[0];
						else{
							m="00";
						}
					}
					else{
						h=adbc.split(":")[0].toLowerCase().split("am")[0];
						h=Integer.parseInt(h)/10==0?"0"+h:h;
						if(adbc.split(":").length==2)
							m=adbc.split(":")[1].toLowerCase().split("am")[0];
						else{
							m="00";
//							System.out.println("lol");

						}
					}
					timeMap.put(timeTokenNumber, h+":"+m+":"+"00");
				}
				else if(mYear.matches()){
					yearTokenNumber=tokenCounter;
					year=mYear.group();
//					System.out.println(year);
					yearMap.put(yearTokenNumber, year);
					
					}
				else{
					remainingMap.put(tokenCounter, text);
				}
				}
			monthMapCopy.putAll(monthMap);
			yearMapCopy.putAll(yearMap);
			dayMapCopy.putAll(dayMap);
				
/*
 * This for loop below, finds triple day.month,year combo*/
			
			for (int i: monthMap.keySet() ){
//				System.out.println(i);
				for(int j:yearMap.keySet()){
//					System.out.println(j);
					for(int k:dayMap.keySet()){
//						System.out.println(k);
					if((j==i+1 && k==i+2) || (i==j+1&&k==j+2) || (i==k+1)&&(j==k+2) || (k==i+1&&j==i+2)||(k==j+1&&i==j+2)||(j==k+1&&i==k+2)){
//						System.out.println(k);
						if(symbolMap.containsKey(Math.max(Math.max(i, j), k)))
						allMap.put(Math.min(Math.min(i, j), k), yearMap.get(j)+monthMap.get(i)+dayMap.get(k)+symbolMap.get(Math.max(Math.max(i, j), k)));
						else
						allMap.put(Math.min(Math.min(i, j), k), yearMap.get(j)+monthMap.get(i)+dayMap.get(k));
						monthMapCopy.remove(i);
						dayMapCopy.remove(k);
						yearMapCopy.remove(j);
					}
					}
				}
			}
monthMapCopy1.putAll(monthMapCopy);
dayMapCopy1.putAll(dayMapCopy);
yearMapCopy1.putAll(yearMapCopy);

//for(int i:dayMapCopy1.keySet())System.out.println(i);	
/* This for loop below, finds double day.month,year combo*/

			for (int i: monthMapCopy.keySet()){
				for(int j:yearMapCopy.keySet()){
						
						if(i==j+1 || j==i+1) { 
							
							if(symbolMap.get(Math.max(i, j))!=null)
								allMapCopy.put(Math.min(i, j), yearMapCopy.get(j)+monthMapCopy.get(i)+"01"+symbolMap.get(Math.max(i, j))); 
							else
								allMapCopy.put(Math.min(i, j), yearMapCopy.get(j)+monthMapCopy.get(i)+"01");
							monthMapCopy1.remove(i); 
							yearMapCopy1.remove(j);
						
						}
				}
			}
			
			for(int k:dayMapCopy.keySet()){
				for(int j:yearMapCopy.keySet()){
					if(j==k+1 || k==j+1) 
						{
						
							if(symbolMap.get(Math.max(k, j))!=null)
								allMapCopy.put(Math.min(j, k), yearMapCopy.get(j)+"01"+dayMapCopy.get(k)+symbolMap.get(Math.max(k, j)));
							else
								allMapCopy.put(Math.min(j, k), yearMapCopy.get(j)+"01"+dayMapCopy.get(k));
							dayMapCopy1.remove(k); 
							yearMapCopy1.remove(j);  
						}
				}
			}
						
			
			for(int i: monthMapCopy.keySet()){
				for(int k:dayMapCopy.keySet()){
					
					if(i==k+1 || k==i+1) 
					{
						
						if(symbolMap.get(Math.max(i, k))!=null)
							allMapCopy.put(Math.min(i, k), "1900"+monthMapCopy.get(i)+dayMapCopy.get(k)+symbolMap.get(Math.max(i, k))); 
						else
							allMapCopy.put(Math.min(i, k), "1900"+monthMapCopy.get(i)+dayMapCopy.get(k)); 
						monthMapCopy1.remove(i); 
						dayMapCopy1.remove(k);
					}
					
				}
				
			}
			/* This loop below, finds single day.month,year combo*/
			
			for (int i: monthMapCopy1.keySet() ){
				allMapCopy1.put(i, "1900"+monthMapCopy1.get(i)+"01");
			}
			
			for (int i: dayMapCopy1.keySet() ){
				allMapCopy1.put(i, "1900"+"01"+dayMapCopy1.get(i));
			}
			for (int i: yearMapCopy1.keySet() ){
				allMapCopy1.put(i, yearMapCopy1.get(i)+"01"+"01");
			}
			
			finalMap.putAll(allMapCopy1);
			finalMap.putAll(allMap);
			finalMap.putAll(allMapCopy);
			finalMap.putAll(timeMap);
			finalMap.putAll(remainingMap);
			int count1=0;
			int[] remove1=new int[50];
			for(int i:finalMap.keySet()){
				if(finalMap.get(i).equalsIgnoreCase("AD") || finalMap.get(i).equalsIgnoreCase("BC")){
					if(finalMap.get(i-1).matches("\\d+")){
						if(finalMap.get(i-1).length()==8){
							if(!finalMap.get(i-1).substring(0, 4).equals("1900")){
								if(finalMap.get(i).equalsIgnoreCase("BC"))
									finalMap.put(i-1, "-"+finalMap.get(i-1).substring(0, 4)+"0101");
								if(finalMap.get(i).equalsIgnoreCase("AD"))
									finalMap.put(i-1, finalMap.get(i-1).substring(0, 4)+"0101");
//								finalMap.remove(i);
								remove1[i]=i;
							}
							else{
								if(finalMap.get(i).equalsIgnoreCase("BC"))
									finalMap.put(i-1, "-"+"00"+finalMap.get(i-1).substring(6, 8)+"0101");
								if(finalMap.get(i).equalsIgnoreCase("AD"))
									finalMap.put(i-1, "00"+finalMap.get(i-1).substring(6, 8)+"0101");
//								finalMap.remove(i);
								remove1[i]=i;
							}
						}
						else{
							
							if(finalMap.get(i).equalsIgnoreCase("BC"))
								finalMap.put(i-1, "-"+"0"+finalMap.get(i-1)+"0101");
							if(finalMap.get(i).equalsIgnoreCase("AD"))
								finalMap.put(i-1, "0"+finalMap.get(i-1)+"0101");
//							finalMap.remove(i);
							remove1[i]=i;
						}
					}
				}
					
			}
			for(int j:remove1){
				if(j!=0) finalMap.remove(j);
			}
			for(int i:finalMap.keySet()){
				if(finalMap.get(i).matches("(\\d+)AD.*")||finalMap.get(i).matches("(\\d+)ad.*")){
					String s=finalMap.get(i).toLowerCase().split("ad")[0];
					while(4-s.length()!=0){
						s="0"+s;
					}
					s=s+"0101";
					finalMap.put(i, s);
				}
				else if(finalMap.get(i).matches("(\\d+)BC.*")||finalMap.get(i).matches("(\\d+)bc.*")){
					String s=finalMap.get(i).toLowerCase().split("bc")[0];
					while(4-s.length()!=0){
						s="0"+s;
					}
					s="-"+s+"0101";
					finalMap.put(i, s);
				}
			}
			int[] remove = new int[50];
			for(int i:finalMap.keySet()){
				if((finalMap.get(i).toLowerCase().contains("am")) && finalMap.get(i-1).matches("\\d{1,8}(:\\d{0,2})?")){
				if(finalMap.get(i-1).contains(":")){
					String adbc=finalMap.get(i-1);
					String h=adbc.split(":")[0];
					String m;
					h=Integer.parseInt(h)/10==0?"0"+h:h;
					if(adbc.split(":").length==2)
						m=adbc.split(":")[1].toLowerCase().split("am")[0];
					else
						m="00";
					finalMap.put(i-1, h+":"+m+":"+"00");
//					finalMap.remove(i);
					remove[i]=i;
				}
				else if(finalMap.get(i-1).length()==8){
					String s=finalMap.get(i-1).substring(6,8);
					finalMap.put(i-1, s+":00:00");
//					finalMap.remove(i);
					remove[i]=i;
				}
			}
				else if((finalMap.get(i).toLowerCase().contains("pm")) && finalMap.get(i-1).matches("\\d{1,8}(:\\d{0,2})?")){
				if(finalMap.get(i-1).contains(":")){
					String adbc=finalMap.get(i-1);
					String h=adbc.split(":")[0];
					String m;
					h=Integer.toString((Integer.parseInt(h)+12));
					if(adbc.split(":").length==2)
						m=adbc.split(":")[1].toLowerCase().split("pm")[0];
					else
						m="00";
					finalMap.put(i-1, h+":"+m+":"+"00");
//					finalMap.remove(i);
					remove[i]=i;
				}
				else if(finalMap.get(i-1).length()==8){
					String s=finalMap.get(i-1).substring(6,8);
					finalMap.put(i-1, Integer.toString((Integer.parseInt(s)+12))+s+":00:00");
//					finalMap.remove(i);
					remove[i]=i;
				}
			}
			}
			for(int j:remove){
//				System.out.println(j);
				if(j!=0)finalMap.remove(j);
			}
			for(int i1:finalMap.keySet()){
				if(finalMap.get(i1).matches("^(\\d{4})-(\\d{2,4})$")){
					String t1=finalMap.get(i1).split("-")[0];
					String t2=finalMap.get(i1).split("-")[1];
					if(t2.length()==2){
						t2=t1.substring(0,2)+t2;
					}
					t1=t1+"0101";
					t2=t2+"0101";
					finalMap.put(i1, t1+"-"+t2);
				}
			}
			for(int i1:finalMap.keySet()){
					if(symbolMap.containsKey(i1)) finalMap.put(i1, finalMap.get(i1)+symbolMap.get(i1));
			}

			
			
			TreeMap<Integer, String> sortedMap = new TreeMap<Integer, String>(finalMap);
			localstream.reset();
			int count=1;
			String s=sortedMap.get(sortedMap.lastKey());
			if(!s.endsWith(lastPunctuation)) sortedMap.put(sortedMap.lastKey(), s+lastPunctuation);
			Token tok;
			ArrayList<Token> a=new ArrayList();
			for(int i:sortedMap.keySet())
			{
				tok=new Token();
				if(!sortedMap.get(i).trim().equals("") && !sortedMap.get(i).trim().equals(null))
				{
				tok.setTermText(sortedMap.get(i));
				a.add(tok);
				}
							
			}
			ts=new TokenStream(a);
			ts.reset();
			super.tokenstream=ts;


		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
