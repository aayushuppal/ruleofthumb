package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
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
	private int size;
	/**
	 * Method to convert given parsed query into string
	 */
	public void setQueryString(LinkedList<String> s){
		queryString=s;
	}
	public String toString() {
		//TODO: YOU MUST IMPLEMENT THIS
		if(queryString==null || queryString.equals("")){
			this.size=0;
			return null;
		}
		
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
			this.size=queryString.size();
			String s2 = "";
			for(String s1: queryString) 
				if(!s1.trim().equals("")) s2=s2+" "+s1;
//			System.out.println(s2);
//			System.out.println(queryString.toString());
			return s2.trim();
		}
	}
	public ArrayList<String> getSplitQuery(){
		toString();
		ArrayList<String> result = new ArrayList<String>();
		for(String s:queryString){
//			System.out.println(s);
			while(s.startsWith("{")){
				result.add("{");
				s=s.substring(1);
			}
			while(s.startsWith("[")){
				result.add("[");
				s=s.substring(1);
			}
			String t=s.replaceAll("[\\]\\}]", "");
			
			result.add(t);
			s=s.replace(t, "");
//			System.out.println(s);
			while(s.startsWith("]")){
				result.add("]");
				s=s.substring(1);
			}
			
			while(s.startsWith("}")){
				result.add("}");
				s=s.substring(0,s.length()-1);
//				System.out.println(s);
			}
			
		}
//		System.out.println(result);
		return result;
	}
	
	public int size(){
		return size;
	}
}