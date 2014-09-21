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
	
	TokenStream localstream;
	TokenStream ts;
	
	public DateTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		// TODO Auto-generated constructor stub
	}


	public TokenStream filter(){
		Token token;
		String text,day="",year="";
		int month=0;
		
		boolean flag=false;
		Pattern pMonth= Pattern.compile("^(January|Jan|February|feb|March|mar|April|Apr|May|June|July|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec)$",Pattern.CASE_INSENSITIVE);
		Pattern pDay= Pattern.compile("^\\d{1,2}$");
		Pattern pYear= Pattern.compile("^\\d{4}$");
		Pattern pTime= Pattern.compile("\\d{1,2}:\\d{1,2}[ap]m",Pattern.CASE_INSENSITIVE);
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
		try {
			while(increment()){
				token=localstream.next();
				text=token.getTermText().replaceAll("[,.-/]", "").trim();
				mDay=pDay.matcher(text);
				mMonth=pMonth.matcher(text);
//				mADBC=pADBC.matcher(text);
				mYear=pYear.matcher(text);
				mtime=pTime.matcher(text);
				tokenCounter++;
				
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
						h=adbc.split(":")[0];
						h=Integer.toString(Integer.parseInt(h)+12);
						m=adbc.split(":")[1].toLowerCase().split("pm")[0];
					}
					else{
						h=adbc.split(":")[0];
						h=Integer.parseInt(h)/10==0?"0"+h:h;
						m=adbc.split(":")[1].toLowerCase().split("am")[0];
					}
					timeMap.put(timeTokenNumber, h+":"+m+":"+"00");
				}
				else if(mYear.matches()){
					yearTokenNumber=tokenCounter;
					year=mYear.group();
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
				
				for(int j:yearMap.keySet()){
					
					for(int k:dayMap.keySet()){
						
					if((j==i+1 && k==i+2) || (i==j+1&&k==j+2) || (i==k+1)&&(j==k+2) || (k==i+1&&j==i+2)||(k==j+1&&i==j+2)||(j==k+1&&i==k+2)){
//						System.out.println(k);
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
			
/* This for loop below, finds double day.month,year combo*/

			for (int i: monthMapCopy.keySet() ){
//				System.out.println(i);
				for(int j:yearMapCopy.keySet()){
//					System.out.println(j);
					for(int k:dayMapCopy.keySet()){
//						System.out.println(k);
						if(i==j+1 || j==i+1) { allMapCopy.put(Math.min(i, j), yearMapCopy.get(j)+monthMapCopy.get(i)+"01"); monthMapCopy1.remove(i); yearMapCopy1.remove(j);}
						if(j==k+1 || k==j+1) { allMapCopy.put(Math.min(j, k), yearMapCopy.get(j)+"01"+dayMapCopy.get(k)); dayMapCopy1.remove(k); yearMapCopy1.remove(j);  }
						if(i==k+1 || k==i+1) { allMapCopy.put(Math.min(i, k), "1900"+monthMapCopy.get(i)+dayMapCopy.get(k)); monthMapCopy1.remove(i); dayMapCopy1.remove(k);}
						
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
			Map<Integer, String> sortedMap = new TreeMap<Integer, String>(finalMap);
			localstream.reset();
			int count=1;
			
			Token tok;
			ArrayList<Token> a=new ArrayList();
			for(int i:sortedMap.keySet())
			{
				tok=new Token();
				tok.setTermText(sortedMap.get(i));
				a.add(tok);
							
			}
			ts=new TokenStream(a);

		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ts;
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
