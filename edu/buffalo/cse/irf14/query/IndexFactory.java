package edu.buffalo.cse.irf14.query;

import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class IndexFactory {
	public static IndexReader indexer;
	private IndexFactory() {

	}
	public static IndexReader getIndexReader(String iDir){
		if ( indexer== null) {
			indexer = new IndexReader(iDir, IndexType.TERM);
			return indexer;
		}
		else return indexer;

	}

}
