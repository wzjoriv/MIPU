import java.util.Scanner;

/*
 * Name: Josue N Rivera
 * Date: December 2, 2019
 * 
 * Return minimum Pumping Length of given Regular expression / DFA / NFA
 * */

public class MinPumpingLength {
	
	private NFA nfa;
	private RegExpToNFA regto;
	private LanguageStrGenerator lan;
	
	private final static int MAXString = 30; //number of strings that are considered
	private final static int MAXPump = 5;
	
	private String x = "", y = "", z = "";
	private String minString = "";
    
    public MinPumpingLength(String reg) { // For detection of regular expression
    	regto = new RegExpToNFA(); //converter regular expression to NFA
        nfa = regto.getNFA(reg); //get NFA of given regular expression
        lan = new LanguageStrGenerator(nfa); //get sequence of string for the language of given NFA    
    }
    
    public MinPumpingLength(NFA nfa) { // For detection of NFA/DFA
    	this.nfa = nfa;
        this.lan = new LanguageStrGenerator(nfa); //get sequence of string for the language of given NFA    
    }
    
    private boolean pump(String xx, String yy, String zz) {
    	String xxx = xx, zzz = zz, out = "";

    	if(xxx == RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon")) xxx = "";
    	if(zzz == RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon")) zzz = "";
    	
    	//pump down and up
    	for(int i = 0; i < MAXPump; i++) {
        	if(!nfa.compute(xxx + (out) + zzz)) return false;
        	out+=yy;
    	}
    	
    	return true;
    }
    
    public int minimunP() {
    	boolean pumped = false;
    	int minp = 0;
    	
    	String xy, nextstr = null, prevstr, xx, yy, zz;
    	
		lan.reset(); //set language to start from the beginning
		
		for(int strnum = 0; strnum < MAXString; strnum++) {
			prevstr = nextstr;
			if ((nextstr = lan.next()) == null) {//get next minimum string; if you get a language that is finite, get a pumping length bigger than maximum string
				this.x = this.y = this.z = "";
				
				minp = prevstr.length() + 1;
				minString = "";
				
				return minp; 
			}
    		
	    	for(int p_length = 1; p_length <= nextstr.length(); p_length++) {
    			xy = nextstr.substring(0, p_length);
    			
    			//set z to rest of the given string if empty set z to epsilon
    			if((zz = nextstr.substring(p_length)).equals("")) zz = RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon");
    			
    			for(int ysize = p_length - 1; ysize >= 0; ysize--) {
    				yy = xy.substring(ysize);
    	    				
    				//set x to remaining of the xy string if empty set x to epsilon
        			if((xx = xy.substring(0, ysize)).equals("")) xx = RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon");
        			
        			//if after partition string can be pump
        			if(pump(xx, yy, zz)) {
        				if(minString == null) { //if it can be pumped and their is not a smaller string than it, make this minimum string
        					minString = nextstr;
        					minp = minString.length();
        					
        					this.x = xx;
        					this.y = yy;
        					this.z = zz;
        					
        					lan.reset(minString.length()); //check rest of strings starting with strings of this size
        				}
        				
        				pumped = true;
        				break;
        			}
        			
        			if(pumped) break; //if the string can be pumped after partition, stop
        			
    			}
    			if(pumped) break; //if the string can be pumped after partition, stop
    			
    		}
	    	
	    	if(!pumped) {lan.block(minString); minString = null;} //if the current string couldn't be pumped than start from the next one and don't consider the current string
    		pumped = false;
    	}
    	
    	return minp;
    }
    
    public String getXYZ() {
    	return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
    
    public String getMinString() {
    	return minString;
    }
    
	public static void main(String args[]) {
		
		Scanner scan = new Scanner(System.in);
		
		String input;
		
		//run program so you can input the value in the command line (empty string cannot be inputed with Scanner but it works)
		System.out.print("Enter input string (stop with input \"--\"): ");
		while(!(input = scan.next()).equals("--")) {
			MinPumpingLength pumping = new MinPumpingLength(input);
			
			int minPump = pumping.minimunP();	
			System.out.println(
			  "\nInput string: " + input + "\n"
			+ "Minimun pumping length: " + minPump + "\n"
			+ "Minimun string: " + pumping.getMinString()+ "\n"
			+ pumping.getXYZ());
			
			System.out.print("\nEnter input string: ");
		}
		
		scan.close();
	}
}
