/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import edu.buffalo.cse.irf14.analysis.TokenizerException;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.index.IndexerException;

/**
 * @author nikhillo
 *
 */
public class Runner1 {
	  private static final long MEGABYTE = 1024L * 1024L;
	/**
	 * 
	 */
	public Runner1() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IndexerException 
	 * @throws TokenizerException 
	 * @throws ParserException 
	 */
	public static void main(String[] args) throws IOException, IndexerException, TokenizerException, ParserException {
//		String ipDir = args[0];
//		String indexDir = args[1];
		
		String indexDir="C:/Users/Festy/Desktop/IR Slides/sample";
		//more? idk!
		long startTime = System.currentTimeMillis();
		String ipDir="C:/Users/Festy/Desktop/IR Slides/training";
//		String ipDir="C:/Users/Festy/Desktop/IR Slides/sample";
//		PrintWriter writer = new  PrintWriter("C:/Users/Festy/Desktop/IR Slides/result2.txt");
		int count=0;
		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		
		String[] files;
		File dir;
		
		Document d = null;
		IndexWriter writer = new IndexWriter(indexDir);
		
		for (String cat : catDirectories) {
			dir = new File(ipDir+ File.separator+ cat);
			files = dir.list();
			
			if (files == null)
				continue;

			for (String f : files) {
				d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
				if(count%500==0) System.out.println(count+" files scanend..");
//				writer.print(d.getField(FieldNames.FILEID)[0]);
//				writer.print(Arrays.toString(d.getField(FieldNames.CATEGORY)));
//				writer.print(Arrays.toString(d.getField(FieldNames.TITLE)));
//				writer.print(Arrays.toString(d.getField(FieldNames.PLACE)));
//				writer.print(Arrays.toString(d.getField(FieldNames.NEWSDATE)));
//				writer.print(Arrays.toString(d.getField(FieldNames.AUTHOR)));
//				writer.print(Arrays.toString(d.getField(FieldNames.AUTHORORG)));
//				writer.print(Arrays.toString(d.getField(FieldNames.CONTENT)));
//				writer.println();
				count++;
						writer.addDocument(d); 
				
			}
			
			
		}
//		writer.printIndex();
		
		writer.close();
		System.out.println("Total Terms: "+IndexWriter.termCounter);
		System.out.println("Files scanned: "+count);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Execution time:"+(double)elapsedTime/1000+"s");
		Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is bytes: " + memory);
	    System.out.println("Used memory is megabytes: "+ bytesToMegabytes(memory));
	}
	 public static long bytesToMegabytes(long bytes) {
		    return bytes / MEGABYTE;
		  }
}
