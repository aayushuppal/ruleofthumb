package edu.buffalo.cse.irf14.analysis;
/*
 * A stopword removal rule. It removes tokens that occur in a standard stop list.
 */
import java.util.ArrayList;


public class StopwordTokenFilter extends TokenFilter{
	TokenStream localstream;
	TokenFilter nextFilter;
	ArrayList<String> stopwordList;
	{
		stopwordList=new ArrayList<String>();
		stopwordList.add("a");    stopwordList.add("able");    stopwordList.add("about");    stopwordList.add("above");    stopwordList.add("according");    stopwordList.add("accordingly");    stopwordList.add("across");    stopwordList.add("actually");    stopwordList.add("after");    stopwordList.add("afterwards");    stopwordList.add("again");    stopwordList.add("against");    stopwordList.add("all");    stopwordList.add("allow");    stopwordList.add("allows");    stopwordList.add("almost");    stopwordList.add("alone");    stopwordList.add("along");    stopwordList.add("already");    stopwordList.add("also");    stopwordList.add("although");    stopwordList.add("always");    stopwordList.add("am");    stopwordList.add("among");    stopwordList.add("amongst");    stopwordList.add("an");    stopwordList.add("and");    stopwordList.add("another");    stopwordList.add("any");    stopwordList.add("anybody");    stopwordList.add("anyhow");    stopwordList.add("anyone");    stopwordList.add("anything");    stopwordList.add("anyway");    stopwordList.add("anyways");    stopwordList.add("anywhere");    stopwordList.add("apart");    stopwordList.add("appear");    stopwordList.add("appreciate");    stopwordList.add("appropriate");    stopwordList.add("are");    stopwordList.add("around");    stopwordList.add("as");    stopwordList.add("aside");    stopwordList.add("ask");    stopwordList.add("asking");    stopwordList.add("associated");    stopwordList.add("at");    stopwordList.add("available");    stopwordList.add("away");    stopwordList.add("awfully");    stopwordList.add("b");    stopwordList.add("be");    stopwordList.add("became");    stopwordList.add("because");    stopwordList.add("become");    stopwordList.add("becomes");    stopwordList.add("becoming");    stopwordList.add("been");    stopwordList.add("before");    stopwordList.add("beforehand");    stopwordList.add("behind");    stopwordList.add("being");    stopwordList.add("believe");    stopwordList.add("below");    stopwordList.add("beside");    stopwordList.add("besides");    stopwordList.add("best");    stopwordList.add("better");    stopwordList.add("between");    stopwordList.add("beyond");    stopwordList.add("both");    stopwordList.add("brief");    stopwordList.add("but");    stopwordList.add("by");    stopwordList.add("c");    stopwordList.add("came");    stopwordList.add("can");    stopwordList.add("cannot");    stopwordList.add("cant");    stopwordList.add("cause");    stopwordList.add("causes");    stopwordList.add("certain");    stopwordList.add("certainly");    stopwordList.add("changes");    stopwordList.add("clearly");    stopwordList.add("co");    stopwordList.add("com");    stopwordList.add("come");    stopwordList.add("comes");    stopwordList.add("concerning");    stopwordList.add("consequently");    stopwordList.add("consider");    stopwordList.add("considering");    stopwordList.add("contain");    stopwordList.add("containing");    stopwordList.add("contains");    stopwordList.add("corresponding");    stopwordList.add("could");    stopwordList.add("course");    stopwordList.add("currently");    stopwordList.add("d");    stopwordList.add("definitely");    stopwordList.add("described");    stopwordList.add("despite");    stopwordList.add("did");    stopwordList.add("different");    stopwordList.add("do");    stopwordList.add("does");    stopwordList.add("doing");    stopwordList.add("done");    stopwordList.add("down");    stopwordList.add("downwards");    stopwordList.add("during");    stopwordList.add("e");    stopwordList.add("each");    stopwordList.add("edu");    stopwordList.add("eg");    stopwordList.add("eight");    stopwordList.add("either");    stopwordList.add("else");    stopwordList.add("elsewhere");    stopwordList.add("enough");    stopwordList.add("entirely");    stopwordList.add("especially");    stopwordList.add("et");    stopwordList.add("etc");    stopwordList.add("even");    stopwordList.add("ever");    stopwordList.add("every");    stopwordList.add("everybody");    stopwordList.add("everyone");    stopwordList.add("everything");    stopwordList.add("everywhere");    stopwordList.add("ex");    stopwordList.add("exactly");    stopwordList.add("example");    stopwordList.add("except");    stopwordList.add("f");    stopwordList.add("far");    stopwordList.add("few");    stopwordList.add("fifth");    stopwordList.add("first");    stopwordList.add("five");    stopwordList.add("followed");    stopwordList.add("following");    stopwordList.add("follows");    stopwordList.add("for");    stopwordList.add("former");    stopwordList.add("formerly");    stopwordList.add("forth");    stopwordList.add("four");    stopwordList.add("from");    stopwordList.add("further");    stopwordList.add("furthermore");    stopwordList.add("g");    stopwordList.add("get");    stopwordList.add("gets");    stopwordList.add("getting");    stopwordList.add("given");    stopwordList.add("gives");    stopwordList.add("go");    stopwordList.add("goes");    stopwordList.add("going");    stopwordList.add("gone");    stopwordList.add("got");    stopwordList.add("gotten");    stopwordList.add("greetings");    stopwordList.add("h");    stopwordList.add("had");    stopwordList.add("happens");    stopwordList.add("hardly");    stopwordList.add("has");    stopwordList.add("have");    stopwordList.add("having");    stopwordList.add("he");    stopwordList.add("hello");    stopwordList.add("help");    stopwordList.add("hence");    stopwordList.add("her");    stopwordList.add("here");    stopwordList.add("hereafter");    stopwordList.add("hereby");    stopwordList.add("herein");    stopwordList.add("hereupon");    stopwordList.add("hers");    stopwordList.add("herself");    stopwordList.add("hi");    stopwordList.add("him");    stopwordList.add("himself");    stopwordList.add("his");    stopwordList.add("hither");    stopwordList.add("hopefully");    stopwordList.add("how");    stopwordList.add("howbeit");    stopwordList.add("however");    stopwordList.add("i");    stopwordList.add("ie");    stopwordList.add("if");    stopwordList.add("ignored");    stopwordList.add("immediate");    stopwordList.add("in");    stopwordList.add("inasmuch");    stopwordList.add("inc");    stopwordList.add("indeed");    stopwordList.add("indicate");    stopwordList.add("indicated");    stopwordList.add("indicates");    stopwordList.add("inner");    stopwordList.add("insofar");    stopwordList.add("instead");    stopwordList.add("into");    stopwordList.add("inward");    stopwordList.add("is");    stopwordList.add("it");    stopwordList.add("its");    stopwordList.add("itself");    stopwordList.add("j");    stopwordList.add("just");    stopwordList.add("k");    stopwordList.add("keep");    stopwordList.add("keeps");    stopwordList.add("kept");    stopwordList.add("know");    stopwordList.add("knows");    stopwordList.add("known");    stopwordList.add("l");    stopwordList.add("last");    stopwordList.add("lately");    stopwordList.add("later");    stopwordList.add("latter");    stopwordList.add("latterly");    stopwordList.add("least");    stopwordList.add("less");    stopwordList.add("lest");    stopwordList.add("let");    stopwordList.add("like");    stopwordList.add("liked");    stopwordList.add("likely");    stopwordList.add("little");    stopwordList.add("ll"); /*stopwordList.added to avoid words like you'll,I'll etc.*/    stopwordList.add("look");    stopwordList.add("looking");    stopwordList.add("looks");    stopwordList.add("ltd");    stopwordList.add("m");    stopwordList.add("mainly");    stopwordList.add("many");    stopwordList.add("may");    stopwordList.add("maybe");    stopwordList.add("me");    stopwordList.add("mean");    stopwordList.add("meanwhile");    stopwordList.add("merely");    stopwordList.add("might");    stopwordList.add("more");    stopwordList.add("moreover");    stopwordList.add("most");    stopwordList.add("mostly");    stopwordList.add("much");    stopwordList.add("must");    stopwordList.add("my");    stopwordList.add("myself");    stopwordList.add("n");    stopwordList.add("name");    stopwordList.add("namely");    stopwordList.add("nd");    stopwordList.add("near");    stopwordList.add("nearly");    stopwordList.add("necessary");    stopwordList.add("need");    stopwordList.add("needs");    stopwordList.add("neither");    stopwordList.add("never");    stopwordList.add("nevertheless");    stopwordList.add("new");    stopwordList.add("next");    stopwordList.add("nine");    stopwordList.add("no");    stopwordList.add("nobody");    stopwordList.add("non");    stopwordList.add("none");    stopwordList.add("noone");    stopwordList.add("nor");    stopwordList.add("normally");    stopwordList.add("not");    stopwordList.add("nothing");    stopwordList.add("novel");    stopwordList.add("now");    stopwordList.add("nowhere");    stopwordList.add("o");    stopwordList.add("obviously");    stopwordList.add("of");    stopwordList.add("off");    stopwordList.add("often");    stopwordList.add("oh");    stopwordList.add("ok");    stopwordList.add("okay");    stopwordList.add("old");    stopwordList.add("on");    stopwordList.add("once");    stopwordList.add("one");    stopwordList.add("ones");    stopwordList.add("only");    stopwordList.add("onto");    stopwordList.add("or");    stopwordList.add("other");    stopwordList.add("others");    stopwordList.add("otherwise");    stopwordList.add("ought");    stopwordList.add("our");    stopwordList.add("ours");    stopwordList.add("ourselves");    stopwordList.add("out");    stopwordList.add("outside");    stopwordList.add("over");    stopwordList.add("overall");    stopwordList.add("own");    stopwordList.add("p");    stopwordList.add("particular");    stopwordList.add("particularly");    stopwordList.add("per");    stopwordList.add("perhaps");    stopwordList.add("placed");    stopwordList.add("please");    stopwordList.add("plus");    stopwordList.add("possible");    stopwordList.add("presumably");    stopwordList.add("probably");    stopwordList.add("provides");    stopwordList.add("q");    stopwordList.add("que");    stopwordList.add("quite");    stopwordList.add("qv");    stopwordList.add("r");    stopwordList.add("rather");    stopwordList.add("rd");    stopwordList.add("re");    stopwordList.add("really");    stopwordList.add("reasonably");    stopwordList.add("regarding");    stopwordList.add("regardless");    stopwordList.add("regards");    stopwordList.add("relatively");    stopwordList.add("respectively");    stopwordList.add("right");    stopwordList.add("s");    stopwordList.add("said");    stopwordList.add("same");    stopwordList.add("saw");    stopwordList.add("say");    stopwordList.add("saying");    stopwordList.add("says");    stopwordList.add("second");    stopwordList.add("secondly");    stopwordList.add("see");    stopwordList.add("seeing");    stopwordList.add("seem");    stopwordList.add("seemed");    stopwordList.add("seeming");    stopwordList.add("seems");    stopwordList.add("seen");    stopwordList.add("self");    stopwordList.add("selves");    stopwordList.add("sensible");    stopwordList.add("sent");    stopwordList.add("serious");    stopwordList.add("seriously");    stopwordList.add("seven");    stopwordList.add("several");    stopwordList.add("shall");    stopwordList.add("she");    stopwordList.add("should");    stopwordList.add("since");    stopwordList.add("six");    stopwordList.add("so");    stopwordList.add("some");    stopwordList.add("somebody");    stopwordList.add("somehow");    stopwordList.add("someone");    stopwordList.add("something");    stopwordList.add("sometime");    stopwordList.add("sometimes");    stopwordList.add("somewhat");    stopwordList.add("somewhere");    stopwordList.add("soon");    stopwordList.add("sorry");    stopwordList.add("specified");    stopwordList.add("specify");    stopwordList.add("specifying");    stopwordList.add("still");    stopwordList.add("sub");    stopwordList.add("such");    stopwordList.add("sup");    stopwordList.add("sure");    stopwordList.add("t");    stopwordList.add("take");    stopwordList.add("taken");    stopwordList.add("tell");    stopwordList.add("tends");    stopwordList.add("th");    stopwordList.add("than");    stopwordList.add("thank");    stopwordList.add("thanks");    stopwordList.add("thanx");    stopwordList.add("that");    stopwordList.add("thats");    stopwordList.add("the");    stopwordList.add("their");    stopwordList.add("theirs");    stopwordList.add("them");    stopwordList.add("themselves");    stopwordList.add("then");    stopwordList.add("thence");    stopwordList.add("there");    stopwordList.add("thereafter");    stopwordList.add("thereby");    stopwordList.add("therefore");    stopwordList.add("therein");    stopwordList.add("theres");    stopwordList.add("thereupon");    stopwordList.add("these");    stopwordList.add("they");    stopwordList.add("think");    stopwordList.add("third");    stopwordList.add("this");    stopwordList.add("thorough");    stopwordList.add("thoroughly");    stopwordList.add("those");    stopwordList.add("though");    stopwordList.add("three");    stopwordList.add("through");    stopwordList.add("throughout");    stopwordList.add("thru");    stopwordList.add("thus");    stopwordList.add("to");    stopwordList.add("together");    stopwordList.add("too");    stopwordList.add("took");    stopwordList.add("toward");    stopwordList.add("towards");    stopwordList.add("tried");    stopwordList.add("tries");    stopwordList.add("truly");    stopwordList.add("try");    stopwordList.add("trying");    stopwordList.add("twice");    stopwordList.add("two");    stopwordList.add("u");    stopwordList.add("un");    stopwordList.add("under");    stopwordList.add("unfortunately");    stopwordList.add("unless");    stopwordList.add("unlikely");    stopwordList.add("until");    stopwordList.add("unto");    stopwordList.add("up");    stopwordList.add("upon");    stopwordList.add("us");    stopwordList.add("use");    stopwordList.add("used");    stopwordList.add("useful");    stopwordList.add("uses");    stopwordList.add("using");    stopwordList.add("usually");    stopwordList.add("uucp");    stopwordList.add("v");    stopwordList.add("value");    stopwordList.add("various");    stopwordList.add("ve");     stopwordList.add("very");    stopwordList.add("via");    stopwordList.add("viz");    stopwordList.add("vs");    stopwordList.add("w");    stopwordList.add("want");    stopwordList.add("wants");    stopwordList.add("was");    stopwordList.add("way");    stopwordList.add("we");    stopwordList.add("welcome");    stopwordList.add("well");    stopwordList.add("went");    stopwordList.add("were");    stopwordList.add("what");    stopwordList.add("whatever");    stopwordList.add("when");    stopwordList.add("whence");    stopwordList.add("whenever");    stopwordList.add("where");    stopwordList.add("whereafter");    stopwordList.add("whereas");    stopwordList.add("whereby");    stopwordList.add("wherein");    stopwordList.add("whereupon");    stopwordList.add("wherever");    stopwordList.add("whether");    stopwordList.add("which");    stopwordList.add("while");    stopwordList.add("whither");   stopwordList.add("who");    stopwordList.add("whoever");    stopwordList.add("whole");    stopwordList.add("whom");    stopwordList.add("whose");    stopwordList.add("why");    stopwordList.add("will");    stopwordList.add("willing");    stopwordList.add("wish");    stopwordList.add("with");    stopwordList.add("within");    stopwordList.add("without");    stopwordList.add("wonder");    stopwordList.add("would");    stopwordList.add("would");    stopwordList.add("x");    stopwordList.add("y");    stopwordList.add("yes");    stopwordList.add("yet");    stopwordList.add("you");    stopwordList.add("your");    stopwordList.add("yours");    stopwordList.add("yourself");    stopwordList.add("yourselves");    stopwordList.add("z");    stopwordList.add("zero");
	}
	
	public StopwordTokenFilter(TokenStream stream) {
		super(stream);
		localstream=stream;
		filter();
		// TODO Auto-generated constructor stub
	}
		


	
	public void filter(){
		localstream.reset();
		
		try {
			while(increment()==true){
				Token t=localstream.next();
				
				if(stopwordList.contains(t.getTermText())){
					localstream.remove();
				}
			}
		} catch (TokenizerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void setNextFilter(TokenFilter filter) {
		// TODO Auto-generated method stub
		this.nextFilter=filter;
	}
	
	@Override
	public TokenFilter getNextFilter() {
		// TODO Auto-generated method stub
		return this.nextFilter;
	}

}
