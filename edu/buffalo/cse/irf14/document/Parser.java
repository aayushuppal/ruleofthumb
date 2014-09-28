package edu.buffalo.cse.irf14.document;

import java.io.BufferedReader;
import java.io.File;
/**
 * 
 */
//package edu.buffalo.cse.irf14.document;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	static String 	fName=null,
					fileID=null,
					category=null,
					title=null,
					author=null,
					org=null,
					month=null,
					date=null,
					place=null,
					content=null,
					contentString=null;
	static int		titleEndIndex,dateStartIndex,titleLineIndex=0,dateEndIndex=0,lineCount=0;
	static File 	f;
	static BufferedReader reader;
	static boolean authorLine=false;
	public static Document parse(String filename) throws ParserException  {
		Document d = new Document();
		cleaner();
		try {
			fName=filename;
			f=new File(fName);
			setFileID();
			setFileCategory();
			setFileTitle();
			setAuthorOrg();
			setDateAndMonthAndPlace();
			setContent();
			reader.close();
		if(!(title==null))		d.setField(FieldNames.TITLE, title);
		if(!(category==null))	d.setField(FieldNames.CATEGORY, category);
		if(!(fileID==null))		d.setField(FieldNames.FILEID,fileID);
		if(!(author==null))		d.setField(FieldNames.AUTHOR,author);
		if(!(org==null))		d.setField(FieldNames.AUTHORORG,org);
		if(!(month==null))		d.setField(FieldNames.NEWSDATE,month+" "+date);
		if(!(place==null))		d.setField(FieldNames.PLACE,place);
		if(!(content==null))	d.setField(FieldNames.CONTENT,content);
		}
		catch(Exception e){
			throw new ParserException();
		}
		return d;
		
	}
	
	
	public static void setDateAndMonthAndPlace() throws IOException{
		String sample,tempPlace,content;
		if(authorLine==true)
		{
			sample=reader.readLine().trim();
			while((sample.equals("\n") || sample.equals("\r") ||sample.equals("") ||sample.equals(null)))
				{
		    	sample=reader.readLine().trim();
		    	}
		}
		else{
			sample=contentString;
		}
		
		sample=sample.replaceAll("[()]","").split("-")[0];
		Pattern p1 =Pattern.compile(".*(jan.*|feb.*|mar.*|apr.*|may.*|jun.*|jul.*|aug.*|sep.*|oct.*|nov.*|dec.*)",Pattern.CASE_INSENSITIVE);//		    Pattern p1 =Pattern.compile("\b(?:Jan(?:uary)?|Feb(?:ruary)?|...|Dec(?:ember)?)",Pattern.CASE_INSENSITIVE);
		Matcher m1 =p1.matcher(sample);
			
		if(m1.matches())
			{	
				month=m1.group(1).split(" ")[0].trim();
				Pattern p2= Pattern.compile((".*"+month+"\\s(\\d{1,2})(\\D*)(.*)"),Pattern.CASE_INSENSITIVE);
				Matcher m2=p2.matcher(sample);
				if(m2.matches()){
					date=m2.group(1);
					dateStartIndex=m1.start(1);
					dateEndIndex=m1.end(1);
					
					if(dateStartIndex>0){
					tempPlace=sample.substring(0, dateStartIndex).trim();
					if(tempPlace.substring(tempPlace.length()-1).equals(",")){
						tempPlace=tempPlace.substring(0,tempPlace.length()-1);
					}
					place=tempPlace.trim();
					}
					
					}
				}
			
		else if(sample.contains(",")){				
				place=sample.split(",")[0].trim();
				if(place.contains("-")){
						place=place.split("-")[0].trim();
				}
						
			}
			reader.close();
//			System.out.println(month+" "+date+" "+place);
		}
		
		

	private static void setFileID() throws ParserException{
		fileID=f.getName();
	}

	private static void setFileCategory() throws ParserException{
		category=f.getParentFile().getName().trim();
	}

	public static void setFileTitle() throws ParserException, IOException{
		String s;
		reader = new BufferedReader(new FileReader(f));
		s=reader.readLine();
		lineCount++;
			 {
				while(s.equals("\n") || s.equals("\r") ||s.equals(""))
			 		{
				 		s=reader.readLine();
				 		titleLineIndex++;
				 		lineCount++;
			 		}
			 		title=s.trim();
			 }
		}
		


	public static void setAuthorOrg() throws IOException{
			Pattern p =Pattern.compile("(<AUTHOR>)(.*)(</AUTHOR>)",Pattern.CASE_INSENSITIVE); // TODO implement for <author>
			String  s =reader.readLine();
			lineCount++;
			while(s.equals("\n") || s.equals("\r") ||s.equals(""))
			 	{
					s=reader.readLine();
					lineCount++;
			 	}
				contentString=s;
			 	s=s.trim();
			Matcher m=p.matcher(s);
			if(m.find()){
				authorLine=true;
				lineCount++;
				String s2[];
				String s3[];
				s2=m.group(2).split(",");
				s3=s2[0].split("(?i)by");
				author=s3[s3.length-1].trim();
				
// * Multiple Authors * //
				
//				if(author.toLowerCase().contains(" and "))
//				{
//					author=author.split("(?i)and")[0]+"*"+author.split("(?i)and")[1];
//					System.out.println(author);
//				}
				if(s2.length>=2)
					org=s2[s2.length-1].trim();
				}
				else{
					org=null;
					author=null;
				}
				}
	
	public static void setContent() throws IOException{
		int count=lineCount;
		String str="",line="";
		String str2[]={""};
		reader = new BufferedReader(new FileReader(f));
		while(count!=1){
			str=reader.readLine();
			count--;
		}
		str=reader.readLine().trim();
		str=str.split("-")[str.split("-").length-1].trim();
		while((line=reader.readLine())!=null){
			
			str=str.trim()+" "+line;
		}
		content=str;
//		System.out.println(str.trim().split(" ")[0]);
		if(content.trim().equals("")) content=null;
		reader.close();
	}
	
	public static void cleaner(){
				fName=null;
				fileID=null;
				category=null;
				title=null;
				author=null;
				org=null;
				month=null;
				date=null;
				place=null;
				content=null;
				authorLine=false;
				titleEndIndex=0;
				dateStartIndex=0;
				titleLineIndex=0;
				dateEndIndex=0;
				lineCount=0;
				contentString=null;
				f=null;
	}
}
