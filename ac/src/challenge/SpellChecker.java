/**
 * 
 */
package challenge;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * @author donwen
 *
 */
public class SpellChecker {

	/**
	 * It answers Challenge.6
	 * @param args
	 */
	public static void main(String[] args) {
		final String pathOfFileToCheck = "resources\\W3C Web Pages\\SpellCheckerTestPages\\Data - W3C.txt";
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
		
		System.out.println("Spell checking " + chosenFile + "...");
		Hashtable<String, Integer> wordsFrequencies = WordsCounter.CountWords(chosenFile);
		
		WebDict webDict = new WebDict();
		webDict.buildDict(WebSearchEngine.INPUT_PAGES_DIR);
		Hashtable<String, Integer> hashtable = webDict.getWordsFrequenciesAsHashtable();
		System.out.println("WebDict is of size " + hashtable.size());		
		
		System.out.println("----------------------------------");
		ArrayList<IntKeyObject> missingWords = new ArrayList<IntKeyObject>();
		wordsFrequencies.forEach((word, freq) -> {
			if (!hashtable.containsKey(word)) {
				missingWords.add(new IntKeyObject(freq, word, true));
				hashtable.put(word, freq);
			}
		});
		
		System.out.println(missingWords.size() + " word(s) were missing as below and added to WebDict:");
		for (IntKeyObject obj:missingWords) {
			System.out.println(obj.getContent());
		}
		
		System.out.println("----------------------------------");
		System.out.println("WebDict now is of size " + hashtable.size());
	}

}
