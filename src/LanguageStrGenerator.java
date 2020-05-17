/*
 * Name: Josue N Rivera
 * Date: December 2, 2019
 * 
 * Return a sequence of the set of string that belong to the language 
 * */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LanguageStrGenerator {
	
	private NFA nfa;
	private Set<String> alphabet;
	private Queue<String> buffer;
	private int strlen = 0;
	private int dim = 0;
	private Set<String> blocked;
	
	private final static int MAXString = 200; //Maximum string size possible
	
	public LanguageStrGenerator(NFA nfa) {
		
		this.nfa = nfa;
		this.alphabet = nfa.alphabet;
		buffer = new LinkedList<String>();
		blocked = new HashSet<String>();
	}
	
	public String next() {
		
		while(buffer.size() == 0) {	//if empty add more strings
			
			if(strlen >= MAXString) { //if maximum length possible for a string is reached, don't add any string
				buffer.add(null);
				break;
			}
			generateStrings("");
			strlen++;
		}
		
		return buffer.poll();
	}

	public void reset() {
		reset(0);
		blocked.clear();
	}

	public void reset(int minsize) { //reset generator to return numbers from 
		buffer.clear();
		strlen = minsize;
	}

	public String block(String block) { //remove string from appearing again
		blocked.add(block);
		
		return block;
	}
	
	private void generateStrings(String string) {
		
		boolean value = nfa.compute(string);
		if(nfa.getCurrent().isEmpty()) return; //stop permutation for strings that cannot end in a final state (cut branch)
		
		
		if(dim >= strlen) { 
			if(value && !(blocked.contains(string))) {
				buffer.add(string); //add string to buffer if it can be accepted by a nfa
			}
			return;
		}
		
		dim++;
		for(String s: alphabet) {
			generateStrings(string + s);
		}
		dim--;
	}

	public static void main(String[] args) {
		
		RegExpToNFA reg = new RegExpToNFA();
		
        NFA nfa = reg.getNFA("(<+>)*U9"); 
        
        System.out.println(nfa);
        
        LanguageStrGenerator lan = new LanguageStrGenerator(nfa);
        
        for(int i = 0; i < 10; i++) {
        	System.out.println(">" + lan.next());
        }
        
	}

}
