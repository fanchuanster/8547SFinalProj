package common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {
	
	 public static String[] findPattern(File file, Pattern pattern) {
		
	    Matcher matcher = pattern.matcher(util.readFile(file));
	    // Check all occurrences
	    ArrayList<String> emails = new ArrayList<String>();
	    while (matcher.find()) {	        
	        String key = matcher.group();
	        emails.add(key);
	    }
	    String[] array = new String[emails.size()];
	    emails.toArray(array);
	    return array;
	}

	public static String readFile(String filename) {
		File file = new File(filename);
		return readFile(file);	    
	}
	
	public static String readFile(File file) {
		try {
		    StringBuilder fileContents = new StringBuilder((int)file.length());        

		    try (Scanner scanner = new Scanner(file)) {
		        while(scanner.hasNextLine()) {
		            fileContents.append(scanner.nextLine() + System.lineSeparator());
		        }
		        return fileContents.toString();
		    }
		} catch (IOException e) {
			System.err.println("Failed to open file " + file.getName());
			return null;
		}
	}
	
}
