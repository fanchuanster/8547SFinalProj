package assignment4;

import common.Util;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import textprocessing.StdOut;
import textprocessing.TST;

import java.util.regex.*;

public class Task2 {
	
	public static void printMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    while (matcher.find()) {
	        System.out.print("Start index: " + matcher.start());
	        System.out.print(" End index: " + matcher.end());
	        System.out.println(" Found: " + matcher.group());
	    }
	}

	public static void main(String[] args) {
		final String datafile = "resources/Protein.txt";
		
		String content = Util.readFile(datafile);
		
		TST<ArrayList<Integer>> st = new TST<ArrayList<Integer>>();
		
		Pattern pattern = Pattern.compile("\\w+");
	    Matcher matcher = pattern.matcher(content);
	    // Check all occurrences
	    while (matcher.find()) {
//	        System.out.print(matcher.start());
//	        System.out.print(" - " + matcher.end());
//	        System.out.println(" : " + matcher.group());
	        
	        String key = matcher.group();
	        int index = matcher.start();
	        if (st.contains(key)) {
        		st.get(key).add(index);
        	} else {
        		ArrayList<Integer> val = new ArrayList<Integer>();
        		val.add(index);
        		st.put(key, val);
        	}
	    }

	    String[] keys = { "protein", "complex", "PPI", "prediction", "interaction" };
	    System.out.println("Searching keys - " + Arrays.toString(keys));
	    System.out.println("\nGet keys by complete match------------");
	    
        for (String key:keys) {
        	StdOut.println(key + " - "+ st.get(key));
        }
        
        System.out.println("\nGet keys by prefixMatch------------");
        for (String key:keys) {
        	Iterable<String> prefixedKeys = st.prefixMatch(key);
        	for (String pkey:prefixedKeys) {
        		StdOut.println(pkey + " - "+ st.get(pkey));
        	}
        }
	}

}
