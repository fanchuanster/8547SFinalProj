/**
 * 
 */
package challenge;

import java.util.Hashtable;
import java.util.Scanner;
import java.io.StreamTokenizer;
import java.io.FileReader;
import java.io.IOException;

/**
 * Count occurrences for each word in input files.
 * @author donwen
 *
 */
public class WordsCounter {

	/**
	 * Count words frequencies in a given file, it answers Challenge.2
	 * @param filePath the file to count words in
	 * @return a hash table with each word as the key and the corresponding frequency as value.
	 */
	public static Hashtable<String, Integer> CountWords(String filePath) {
		FileReader fr = null;
		try {
			fr = new FileReader(filePath);
		} catch (IOException e) {
			System.err.println("Failed to open file " + filePath);
			return null;
		}
		
		StreamTokenizer tokenizer = new StreamTokenizer(fr);
		tokenizer.lowerCaseMode(true);
		
		tokenizer.ordinaryChars(0, (int)'A' - 1);
		tokenizer.ordinaryChars((int)'Z' + 1, (int)'a' - 1);
		tokenizer.ordinaryChars((int)'z' + 1, 256);
		tokenizer.wordChars((int)'0', (int)'9');
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		try {
			int token = tokenizer.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_WORD:
					String word = tokenizer.sval;
					int count = ht.containsKey(word) ? ht.get(word) : 0; 
					ht.put(word, count + 1);
					break;
				default:
					break;
				}
				token = tokenizer.nextToken();
			}
		} catch (IOException e) {
			System.err.println("Failed to nextToken " + filePath);
			e.printStackTrace();
		}
		return ht;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String pathOfFileToCheck = "resources\\W3C Web Pages\\Text\\Accessibility - W3C.txt";
		
		Scanner sc = new Scanner(System.in);
		
		String chosenFile = null;
		while (chosenFile == null) {
			System.out.println("Which file to process? choose one of the numbers to continue..");
			System.out.println("1 - default file '" + pathOfFileToCheck + "'");
			System.out.println("2 - enter the full path of your file");
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice) {
			case 1:
				chosenFile = pathOfFileToCheck;
				break;
			case 2:
				chosenFile = sc.nextLine();
				break;
			}
		}
		sc.close();
		
		System.out.println("Couting words frequencies in '" + chosenFile + "'...");
		Hashtable<String, Integer> ht = CountWords(chosenFile);

		System.out.println("In all " + ht.size() + " keywords in the file.");		
	}

}
