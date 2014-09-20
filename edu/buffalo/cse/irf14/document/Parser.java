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
					content=null;
	static int		titleEndIndex,dateStartIndex,titleLineIndex=0,dateEndIndex=0,lineCount=0;
	static File 	f;

	public static Document parse(String filename)  {
		Document d = new Document();
		cleaner();
		fName=filename;
		
		f=new File(fName);
		setFileID();
		setFileCategory();
		try {
			setFileTitle();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAuthorOrg();
		try {
			setDateAndMonthAndPlace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			setContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d.setField(FieldNames.TITLE, title);
		d.setField(FieldNames.CATEGORY, category);
		d.setField(FieldNames.FILEID,fileID);
		d.setField(FieldNames.AUTHOR,author);
		d.setField(FieldNames.AUTHORORG,org);
		d.setField(FieldNames.NEWSDATE,month+" "+date);
		d.setField(FieldNames.PLACE,place);
		d.setField(FieldNames.CONTENT,content);
		return d;
	}
	public static void setDateAndMonthAndPlace() throws FileNotFoundException{
		String sample,tempPlace,content;

		BufferedReader reader;
		reader = new BufferedReader(new FileReader(f));
		try {
		    sample=reader.readLine().trim();
		    lineCount++;
		    while((sample.equals("\n") || sample.equals("\r") ||sample.equals("") ||sample.equals(null))){
		    	sample=reader.readLine().trim();
		    	lineCount++;
		    }
		    sample=reader.readLine().trim();
		    lineCount++;
		    while((sample.equals("\n") || sample.equals("\r") ||sample.equals("") ||sample.equals(null))){
		    	sample=reader.readLine().trim();
		    	lineCount++;
		    }
		    if(sample.toLowerCase().contains("author"))	{ sample=reader.readLine().trim(); lineCount++; }// Skips a line if it has an author tag 
		    																							// Assuming <author> is not in the same line as date line
		    
		    sample=sample.replaceAll("[()]","");
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
//			else if(sample.split(" ")[0].matches("[A-Z]")){
//				place=sample.split(" ")[0];
//				System.out.println(fileID);
//			}
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		
//		System.out.println(date+" "+month);
		
	}
	private static void setFileID(){
		fileID=f.getName();
	}

	private static void setFileCategory(){
		category=f.getParentFile().getName().trim();
	}

	public static void setFileTitle() throws FileNotFoundException{
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(f));
		try {
			String s;
			s=reader.readLine();
		    while(s.equals("\n") || s.equals("\r") ||s.equals("") ||s.equals(null))
		    {
		    	s=reader.readLine();
		    	titleLineIndex++;
		    }
		    title=s.trim();
		    reader.close();

		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}


	public static void setAuthorOrg(){
		Pattern p =Pattern.compile("(<AUTHOR>)(.*)(</AUTHOR>)",Pattern.CASE_INSENSITIVE); // TODO implement for <author>
		try {
			FileInputStream fi= new FileInputStream(f);
			byte[] data = new byte[(int)(f).length()];
		    try {
				fi.read(data);
				String s = new String(data, "UTF-8");
				Matcher m=p.matcher(s);
				if(m.find()){
					String s2[];
					String s3[];
					s2=m.group(2).split(",");
					s3=s2[0].split("(?i)by");
					author=s3[s3.length-1].trim();
					if(s2.length>=2)
						org=s2[s2.length-1].trim();
					
				}
				fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void setContent() throws IOException{
		int count=lineCount;
		String str="",line="";
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(f));
		while(count!=0){
			str=reader.readLine();
			count--;
		}
		Pattern p3=  Pattern.compile(".*-(.*)");
		Matcher m3= p3.matcher(str);
		if(m3.matches()) str=m3.group(1)+" ";
				
		while((line=reader.readLine())!=null){
			
			str=str+line;
		}
		content=str;
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
				titleEndIndex=0;
				dateStartIndex=0;
				titleLineIndex=0;
				dateEndIndex=0;
				lineCount=0;
				f=null;
	}
}
