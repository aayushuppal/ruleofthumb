package edu.buffalo.cse.irf14.query;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class that represents a parsed query
 * @author nikhillo
 *
 */
public class Query {
	LinkedList<String> queryString;
	/**
	 * Method to convert given parsed query into string
	 */
	public void setQueryString(LinkedList<String> s){
		queryString=s;
	}
	public String toString() {
		//TODO: YOU MUST IMPLEMENT THIS
		if(queryString==null || queryString.equals(""))
		return null;
		else{
			for(int i=0;i<queryString.size();i++){
				String str= (String) queryString.get(i);
				if(i==0 && !str.startsWith("{")){
					str="{"+str;
					queryString.set(0, str);
					String str2=queryString.getLast();
					str2=str2+"}";
					queryString.removeLast();
					queryString.addLast(str2);
				}
				if(str.contains("(")){
					str=str.replace("(", "[");
					queryString.remove(i);
					queryString.add(i,str);
				}
				if(str.contains(")")){
					str=str.replace(")", "]");
					queryString.remove(i);
					queryString.add(i,str);
					
				}
				Pattern p1=Pattern.compile("NOT");
				Matcher m1= p1.matcher(str.trim());
				if(str.contains("NOT")){
					String str2=queryString.get(i+1);
					String temp=str2;
					str2=str2.replace("[", "");
					Pattern p= Pattern.compile("[^a-zA-Z0-9:]+");
					Matcher m= p.matcher(str2);
					if(m.find()){
//						System.out.println(str2);
						str2="<"+str2.substring(0, m.start())+">"+str2.substring(m.start(),str2.length());
					}
					else str2="<"+str2+">";
					if(!str.equals("NOT"))
					queryString.set(i+1, str.split("NOT")[0]+str2);
					else if(temp.contains("[")){
						queryString.set(i+1, "["+str2);
					}
					else queryString.set(i+1,str2);
					queryString.set(i, "");
					
				}
			}
			
			return queryString.toString();
		}
	}
}