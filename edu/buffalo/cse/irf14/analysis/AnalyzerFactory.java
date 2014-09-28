/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import edu.buffalo.cse.irf14.document.FieldNames;

/**
 * @author nikhillo
 * This factory class is responsible for instantiating "chained" {@link Analyzer} instances
 */
public class AnalyzerFactory {
	private static AnalyzerFactory myClass;
	private AnalyzerFactory() {}
	/**
	 * Static method to return an instance of the factory class.
	 * Usually factory classes are defined as singletons, i.e. 
	 * only one instance of the class exists at any instance.
	 * This is usually achieved by defining a private static instance
	 * that is initialized by the "private" constructor.
	 * On the method being called, you return the static instance.
	 * This allows you to reuse expensive objects that you may create
	 * during instantiation
	 * @return An instance of the factory
	 */
	public static AnalyzerFactory getInstance() {
		//TODO: YOU NEED TO IMPLEMENT THIS METHOD
		if(myClass==null) {
			myClass = new AnalyzerFactory();
		}
		return myClass;
	}
	
	/**
	 * Returns a fully constructed and chained {@link Analyzer} instance
	 * for a given {@link FieldNames} field
	 * Note again that the singleton factory instance allows you to reuse
	 * {@link TokenFilter} instances if need be
	 * @param name: The {@link FieldNames} for which the {@link Analyzer}
	 * is requested
	 * @param TokenStream : Stream for which the Analyzer is requested
	 * @return The built {@link Analyzer} instance for an indexable {@link FieldNames}
	 * null otherwise
	 */
	public Analyzer getAnalyzerForField(FieldNames name, TokenStream stream) {
		//TODO : YOU NEED TO IMPLEMENT THIS METHOD
		TokenFilter filter = null;
		TokenFilterFactory factory = TokenFilterFactory.getInstance();
		switch(name) {
		case TITLE:{
			
			filter=factory.getFilterByType(TokenFilterType.DATE, stream);
			filter.nextFilter=factory.getFilterByType(TokenFilterType.ACCENT, stream);
			filter.nextFilter.nextFilter = factory.getFilterByType(TokenFilterType.STOPWORD, stream);
			filter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
			filter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.DATE, stream);
			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.CAPITALIZATION, stream);
			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.NUMERIC, stream);
			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.STEMMER, stream);
			break;
		}
			
		case AUTHOR:{
//			filter=factory.getFilterByType(TokenFilterType.SYMBOL, stream);
			filter=factory.getFilterByType(TokenFilterType.ACCENT, stream);
//			filter.nextFilter.nextFilter = factory.getFilterByType(TokenFilterType.STOPWORD, stream);
			filter.nextFilter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.DATE, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.CAPITALIZATION, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.NUMERIC, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.STEMMER, stream);
		}
			break;
		case AUTHORORG:{
			filter=factory.getFilterByType(TokenFilterType.ACCENT, stream);
			filter.nextFilter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
		}
			break;
		case CATEGORY:{
			
		}
			break;
		case CONTENT:{
			//System.out.println("lol1");
//			filter=factory.getFilterByType(TokenFilterType.STEMMER, stream);
//			filter.nextFilter=factory.getFilterByType(TokenFilterType.NUMERIC, stream);
//			filter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.DATE, stream);
//			filter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter = factory.getFilterByType(TokenFilterType.STOPWORD, stream);
//			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.ACCENT, stream);
			filter=factory.getFilterByType(TokenFilterType.SYMBOL, stream); //System.out.println("lol2");
			filter.nextFilter=factory.getFilterByType(TokenFilterType.ACCENT, stream);//System.out.println("lol3");
			filter.nextFilter.nextFilter = factory.getFilterByType(TokenFilterType.STOPWORD, stream);//System.out.println("lol4");
			filter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);//System.out.println("lol6");
			filter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.DATE, stream);//System.out.println("lol7");
////		filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.CAPITALIZATION, stream);System.out.println("lo8");
			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.NUMERIC, stream);//System.out.println("lol9");
			filter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter.nextFilter=factory.getFilterByType(TokenFilterType.STEMMER, stream);//System.out.println("lol10");
			break;
		}
		case FILEID:
			break;
		case NEWSDATE:
		{
			filter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
			filter.nextFilter = factory.getFilterByType(TokenFilterType.DATE, stream);
			break;
		}
		case PLACE:{
			filter=factory.getFilterByType(TokenFilterType.SPECIALCHARS, stream);
		}
			break;
		default:
			break;
		
		}
		
		return filter;
	}
}
