package assignment4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;

public class Task5 {
	
	static List<String> searchFolderContent(String dir, String patternString) {
		File directory = new File(dir);
		File[] files = directory.listFiles();
		
		ArrayList<String> searchingResult = new ArrayList<String>();
		for ( File f : files) {
			if (!f.isFile()) {
				continue;
			}
			
			String[] res = Util.findPattern(f, Pattern.compile(patternString, Pattern.CASE_INSENSITIVE));
			for (String r:res) {
				searchingResult.add(r);
			}
		}
		
		return searchingResult;
	}

	public static void main(String[] args) throws IOException {
		
		String urlcharacterset = "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Map<String, String> map = Stream.of(new String[][] {
			  { "Links with domain w3.org", "\\b(https?|ftp|file)://[\\w.]*w3.org" + urlcharacterset }, 
			  { "Links that contain folders", "\\b(https?|ftp|file)://[\\w.]+/[\\w.]+/" + urlcharacterset },
			  { "Links that contain references", "\\b(https?|ftp|file)://" + urlcharacterset.replaceAll("#", "") + "#" + urlcharacterset.replaceAll("#", "") },
			  { "Links with domain .net .com .org", "\\b(https?|ftp|file)://[\\w.]+(.net|.com|.org)/" + urlcharacterset }
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        
		File directory = new File(Task3.INPUT_PAGES_DIR);
		File[] files = directory.listFiles();
		
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
	        it.remove();
	        List<String> searchingResult = searchFolderContent(Task3.INPUT_PAGES_DIR, pair.getValue());

	        String outfile = Task3.output + pair.getKey().replaceAll(" ", "") + ".dat";
			System.out.println(String.format("Found %d %s\t==>\t%s", searchingResult.size(), pair.getKey(), outfile));
			FileWriter fw = new FileWriter(outfile);
	    	for (String s:searchingResult) {
//				System.out.println(s);
				fw.write(s+"\n");
			}
	    	fw.close();
	    }
		
	}

}
