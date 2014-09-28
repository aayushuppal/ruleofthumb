/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexWriter;
import edu.buffalo.cse.irf14.index.IndexerException;

/**
 * @author nikhillo
 *
 */
public class Runner {

	/**
	 * 
	 */
	public Runner() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ipDir = "C:/Users/Festy/Desktop/IR Slides/training";								// My Added Line
		String indexDir = "C:/Users/Festy/Desktop/IR Slides/sample";							// My Added Line
		long startTime = System.currentTimeMillis();											// My Added Line
		int count=0;																			// My Added Line
		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		
		String[] files;
		File dir;
		
		Document d = null;
		IndexWriter writer = new IndexWriter(indexDir);
		
		try {
			for (String cat : catDirectories) {
				dir = new File(ipDir+ File.separator+ cat);
				files = dir.list();
				
				if (files == null)
					continue;
				
				for (String f : files) {
					try {
						d = Parser.parse(dir.getAbsolutePath() + File.separator +f);
						if(count%500==0) System.out.println(count+" files scanned..");			// My Added Line
						count++;																// My Added Line
//						System.out.println("File:"+f);
						writer.addDocument(d);
					} catch (ParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}
				
			}
			
			writer.close();
			System.out.println("Total Terms: "+IndexWriter.termCounter);						// My Added Line
			System.out.println("Files scanned: "+count);										// My Added Line
			long stopTime = System.currentTimeMillis();											// My Added Line
			long elapsedTime = stopTime - startTime;											// My Added Line
			System.out.println("Execution time:"+(double)elapsedTime/1000+"s");					// My Added Line
			Runtime runtime = Runtime.getRuntime();												// My Added Line
		    // Run the garbage collector														// My Added Line
		    runtime.gc();																		// My Added Line
		    // Calculate the used memory														// My Added Line
		    long memory = runtime.totalMemory() - runtime.freeMemory();							// My Added Line
		    System.out.println("Used memory is bytes: " + memory);								// My Added Line
		    System.out.println("Used memory is megabytes: "+ (long)memory/(long)1024L * 1024L);	// My Added Line
		    System.out.println(IndexWriter.catM);
		    System.out.println(IndexWriter.catCounter);
		    System.out.println(IndexWriter.catdocCounter);
		    System.out.println(IndexWriter.authCounter);
		    System.out.println(IndexWriter.authdocCounter);
		    System.out.println(IndexWriter.placeCounter);
		    System.out.println(IndexWriter.placedocCounter);
		    System.out.println(IndexWriter.termCounter);
		    System.out.println(IndexWriter.docCounter);
		    
		    } catch (IndexerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}