package edu.buffalo.cse.irf14.analysis;

public class EmptyFilter extends TokenFilter{

	public EmptyFilter(TokenStream m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void filter() {
		// TODO Auto-generated method stub
		
	}

	public boolean increment() throws TokenizerException {
		return false;
	}

}
