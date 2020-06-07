/*
 * Name: Josue N Rivera
 * Date: November 24, 2019
 * 
 * NFA implementation
 * */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NFA {
	protected Set<String> states;
	protected Set<String> alphabet;
	protected HashMap<String, ArrayList<String>> transition;
	protected String startq;
	protected Set<String> endqs;

	private ArrayList<String> current;
	
	public NFA(Set<String> states, Set<String> alphabet, HashMap<String, ArrayList<String>> transition, String startq, Set<String> endqs) {
		
		this.states = states;
		this.alphabet = alphabet;
		this.transition = transition;
		this.startq = startq;
		this.endqs = endqs;
	}
	public ArrayList<String> getCurrent() {
		return current;
	}
	
	public boolean compute(String input) {
		
		current = new ArrayList<String>(); //reset current
		current.add(this.startq);
		updateEpsilon();
		
		//compute transitions
        for (int i=0; i<input.length(); i++) {
        	transit(input.charAt(i));
            
        	//add epsilon transitions to current
        	updateEpsilon();
        }
            
        if (isFinal()) return true;
        else return false;
    }
	
	protected void transit(char symbol) {
		
		ArrayList<String> temp; //list of available transition
			
		if(symbol == RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon").charAt(0)) return; //if epsilon character, do not read as other type
		int size = current.size();
			
		for(int i = 0; i < size; size--) {
			//transit non-epsilon transition    
	        if ((temp = transition.get(current.get(i)+ "+" +symbol)) != null) {
		        for(int j = 0; j < temp.size(); j++) {
		        	current.add(temp.get(j)); //add new states
		        }
	        }
        	current.remove(i); //add old state
		}
		
    }
	
	protected void updateEpsilon() {
		ArrayList<String> temp;
		for(int i = 0; i < current.size(); i++)
			if((temp = transition.get(current.get(i)+ "+" + RegExpToNFA.SPECIAL_SYMBOLS.get("epsilon"))) != null)
				for(int j = 0; j < temp.size(); j++)
					if(!current.contains(temp.get(j)))
						current.add(temp.get(j));
	}
	
	public boolean isFinal() {
		
		for(int i = 0; i < current.size(); i++) { //just one of the current states must be a final state to accept
	        if(endqs.contains(current.get(i))) return true;
		}
        
        return false;
    }
	
	public String toString() {
		
		String out = "";

		out += "States: \n";
		out += "\t" + states + "\n";
		
		out += "Alphabet: \n";
		out += "\t" + alphabet + "\n";
		
		out += "Transitions: \n";
		for (String name: transition.keySet()){
            String key = name;
            String value = transition.get(name).toString();  
            out += "\t" + key + " -> " + value + "\n"; 
		}
		
		out += "Start State: \n";
		out += "\t" + startq + "\n";
		
		out += "Final States: \n";
		out += "\t" + endqs + "\n";
		
		return out;
	}
	
	public static void main(String[] args) {
		
		//Epsilon: e
    	
		HashMap<String, ArrayList<String>> tra = new HashMap<String, ArrayList<String>>();
		
		// {w | W is any string that ends in a 1 or 10}
		tra.put("q0+0", new ArrayList<String>(Arrays.asList("q0")));
        tra.put("q0+1", new ArrayList<String>(Arrays.asList("q0", "q2"))); 
        tra.put("q0+e", new ArrayList<String>(Arrays.asList("q1")));
        tra.put("q1+1", new ArrayList<String>(Arrays.asList("q3")));
        tra.put("q3+0", new ArrayList<String>(Arrays.asList("q4")));
        
        String curState = "q0";
        Set<String> finalState = new HashSet<String>(Arrays.asList("q2", "q4"));
        Set<String> alpha = new HashSet<String>(Arrays.asList("0", "1"));
        Set<String> stat = new HashSet<String>(Arrays.asList("q0", "q1", "q2", "q3", "q4"));
        
        NFA nfa = new NFA(stat, alpha, tra, curState, finalState); //5-tuple NFA

        System.out.println(nfa);
        System.out.println(nfa.compute("0110000100100001"));
        System.out.println("\nCurrent:\n\t" + nfa.getCurrent());
        
    }
}
