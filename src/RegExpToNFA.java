/*
 * Name: Josue N Rivera
 * Date: November 24, 2019
 * 
 * Regular expression to NFA 
 * */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegExpToNFA {
	
	private NFA nfa;
	private Set<String> states;
	private Set<String> alphabet;
	private HashMap<String, ArrayList<String>> transition;
	private String startq;
	private Set<String> endqs;
	
	int currentq;
	
	protected static final Map<String, String> SPECIAL_SYMBOLS;
	
	static {
		 HashMap<String, String> ops = new HashMap<String, String>();
		 ops.put("star", "*");
		 ops.put("concat", ".");
		 ops.put("union", "U");
		 ops.put("empty set", "\\");
		 ops.put("epsilon", "e");
		 
		 SPECIAL_SYMBOLS = Collections.unmodifiableMap(ops);
	}
	
	public static Map<String, String> getSpecialSymbols() {return SPECIAL_SYMBOLS;}
	
	public static boolean isSpecialSymbol(String input) {return SPECIAL_SYMBOLS.containsValue(input);}
	
	public RegExpToNFA() {}
	
	public NFA getNFA(String input) {
		transition = new HashMap<String, ArrayList<String>>();
		alphabet = new HashSet<String>();
		states = new HashSet<String>();
		currentq = 0;
		
		int[] temp = getTransitions2(input);
		this.startq = "q" + temp[0];
		if(input.equals(SPECIAL_SYMBOLS.get("empty set"))) this.endqs = new HashSet<String>(); //no end state if empty set
		else this.endqs = new HashSet<String>(Arrays.asList("q" + temp[1])); //get transition here, return end state
		
		nfa = new NFA(states, alphabet, transition, startq, endqs);
		
		return nfa;
	}
	
	private int[] getTransitions2(String input) { //return start and end state of constructed NFA
		ArrayList<String> seg = new ArrayList<String>();
		int[] startqs, endqs, involvefrom, involveto, temp;
		
		if(input.equals(SPECIAL_SYMBOLS.get("star")) || input.equals(SPECIAL_SYMBOLS.get("concat")) || input.equals(SPECIAL_SYMBOLS.get("union"))) {//if just a operand for input string, return error
			System.err.println("An operand by itself is not allowed");
			return new int[] {-1, -1};
		}else if(input.equals(SPECIAL_SYMBOLS.get("empty set"))) { //Empty set doesn't have any start or end state
			return new int[] {-1, -1};
		}
		else if(input.length() <= 0) { //if empty string, no transitions needed so return just start state
			return new int[] {0, 0};
		}else if(input.length() == 1) { //when it is a single element, return NFA with single transition
			addTransition("q"+(currentq++)+"+" + input, "q"+(currentq++));
			states.add("q"+(currentq-1));
			states.add("q"+(currentq-2));
			
			if(!input.equals(SPECIAL_SYMBOLS.get("epsilon"))) alphabet.add(input); //if not epsilon, add to alphabet
			
			return new int[] {currentq - 2, currentq - 1};
		}
		
		//parse string
		seg = parseString(input); //Separates into segments
		
		//construct NFA for individual segments
		startqs = new int[seg.size()]; //track what state (q) starts a segment
		endqs = new int[seg.size()]; //track what state (q) ends a segment
		involvefrom = new int[seg.size()]; //track which segments merged (used to update each segment to reflect same start, end and segments merged)
		involveto = new int[seg.size()];
		
		//non-operand
		for(int i = 0; i < seg.size(); i++) {
			if(!seg.get(i).equals(SPECIAL_SYMBOLS.get("star")) && !seg.get(i).equals(".") && !seg.get(i).equals("U")) { //if it is not a operand, construct NFA
				temp = getTransitions2(seg.get(i)); //constructs NFA
				
				startqs[i] = temp[0];
				endqs[i] = temp[1];
				involvefrom[i] = i;
				involveto[i] = i;
			}
		}
		
		//star
		for(int i = 0; i < seg.size(); i++) {
			if(seg.get(i).equals(SPECIAL_SYMBOLS.get("star"))) {
				startqs[i] = currentq;
				addTransition("q"+endqs[i-1]+"+e", "q" + startqs[i-1]);
				addTransition("q"+(currentq)+"+e", "q" + startqs[i-1]);
				states.add("q"+(currentq));
				addTransition("q"+(currentq)+"+e", "q"+(++currentq));
				states.add("q"+(currentq));
				addTransition("q"+endqs[i-1]+"+e", "q" + (currentq++));
				endqs[i] = currentq-1;
				involvefrom[i] = involvefrom[i-1];
				involveto[i] = i;
				
				for(int k = involvefrom[i]; k <= involveto[i]; k++) { //update value of the merged segments
					startqs[k] = startqs[i];
					endqs[k] = endqs[i];
					involvefrom[k] = involvefrom[i];
					involveto[k] = involveto[i];
				}
			}
		}

		//concatenation
		for(int i = 0; i < seg.size(); i++) {
			if(seg.get(i).equals(".")) {
				addTransition("q"+(endqs[i-1])+"+e", "q"+(startqs[i+1]));
				
				startqs[i] = startqs[i-1];
				endqs[i] = endqs[i+1];
				involvefrom[i] = involvefrom[i-1];
				involveto[i] = involveto[i+1];
				
				for(int k = involvefrom[i]; k <= involveto[i]; k++) {  //update value of the merge segments
					startqs[k] = startqs[i];
					endqs[k] = endqs[i];
					involvefrom[k] = involvefrom[i];
					involveto[k] = involveto[i];
				}
			}
		}

		//union
		if(seg.contains("U")) {
			currentq += 2;
			states.add("q"+(currentq-1));
			states.add("q"+(currentq-2));
			for(int i = 0; i < seg.size(); i++) { //add transitions for all element left to the union
				if(seg.get(i).equals("U")) {
					addTransition("q"+(currentq-2)+"+e", "q"+(startqs[i-1]));
					addTransition("q"+(endqs[i-1])+"+e", "q"+(currentq-1));
				}
			}
			//add transitions for the single element right to the union
			addTransition("q"+(currentq-2)+"+e", "q"+(startqs[seg.size()-1]));
			addTransition("q"+(endqs[seg.size()-1])+"+e", "q"+(currentq-1));
			
			int i = 0;
			startqs[i] = currentq - 2;
			endqs[i] = currentq - 1;
			involvefrom[i] = 0;
			involveto[i] = seg.size() - 1;
			
			for(int k = involvefrom[i]; k <= involveto[i]; k++) {  //update value of the merge segments
				startqs[k] = startqs[i];
				endqs[k] = endqs[i];
				involvefrom[k] = involvefrom[i];
				involveto[k] = involveto[i];
			}
		}
		
		
		return new int[] {startqs[seg.size() - 1], endqs[seg.size() - 1]};
	}
	
	protected ArrayList<String> parseString(String input) { //divides expression into segments and operands 
		int count = 0;
		ArrayList<String> seg = new ArrayList<String>();
		char inputArray[] = input.toCharArray();
		String temp = "";
		
		//parse
		for(int i = 0; i < input.length(); i++) {
			if(inputArray[i] == '(') { //group outer parenthesis together
				count++;
				if(count == 1) continue; //don't include outer parenthesis
			} else if(inputArray[i] == ')') {
				count--;
				if(count == 0) {
					seg.add(temp);
					temp = "";
					//delete != "*"
					if(i < input.length() - 1 && (inputArray[i+1] != SPECIAL_SYMBOLS.get("union").charAt(0) && inputArray[i+1] != SPECIAL_SYMBOLS.get("star").charAt(0))) seg.add(".");
					continue; //don't include outer parenthesis
				}
			}
			
			if(count == 0 && (inputArray[i] == SPECIAL_SYMBOLS.get("union").charAt(0) || inputArray[i] == SPECIAL_SYMBOLS.get("star").charAt(0) || inputArray[i] == SPECIAL_SYMBOLS.get("concat").charAt(0))) { //add operands
				seg.add(""+inputArray[i]);
				//delete
				if(inputArray[i] == SPECIAL_SYMBOLS.get("star").charAt(0) && (i + 1 < input.length() && inputArray[i+1] != SPECIAL_SYMBOLS.get("union").charAt(0))) seg.add("."); //add concatenation for non-union command (aka *)
				continue;
			}
			
			temp+= inputArray[i]; //read current strings
			
			if(count == 0) { //string left are concatenation: add input and concatenation command
				seg.add(temp);
				temp = "";
				if(i < input.length() - 1 && (inputArray[i+1] != SPECIAL_SYMBOLS.get("union").charAt(0) && inputArray[i+1] != SPECIAL_SYMBOLS.get("star").charAt(0) && inputArray[i+1] != SPECIAL_SYMBOLS.get("concat").charAt(0))) seg.add(".");
			}
		}
		
		return seg;
	}
	
	private void addTransition(String key, String value) {
		
		ArrayList<String> temp;

		if((temp = transition.get(key)) == null) temp = new ArrayList<String>();
		
		temp.add(value);
		transition.put(key, temp);
	}
	
	public static void main(String[] args) {
        
		RegExpToNFA reg = new RegExpToNFA();
		String input = "(1U0)*101(1U0)*";
		
        NFA nfa = reg.getNFA(input); //contains the substring 101
        System.out.println(reg.parseString(input));
        System.out.println(nfa);
        System.out.println(nfa.compute("1"));
    }
}
