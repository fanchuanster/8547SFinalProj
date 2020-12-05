package assignment2;

import assignment1.RandomStringGenerator;
import algorithmDesign.Sequences;

public class TestEditDistances {
	
	private static long testEditInstances(int randomStringPairsNumber, int randomStringLen) {
		String[][] stringPairs = new String[randomStringPairsNumber][2];
		for (int i=0; i<randomStringPairsNumber; i++) {
			stringPairs[i][0] = RandomStringGenerator.createRandomString(randomStringLen);
			stringPairs[i][1] = RandomStringGenerator.createRandomString(randomStringLen);
		}
		
		long start = System.nanoTime();
		for (int i=0; i<randomStringPairsNumber; i++) {
			int dist = Sequences.editDistance(stringPairs[i][0], stringPairs[i][1]);
//			System.out.println(String.format("%d\t%s\t%s", dist, stringPairs[i][0], stringPairs[i][1]));
		}
		long average = (System.nanoTime() - start) / randomStringPairsNumber;
		
		System.out.println(String.format("%d\t%d\t%d", randomStringPairsNumber, randomStringLen, average));
		return average;
	}

	public static void main(String[] args) {
		final int PAIRS_NUMBER = 1000;
		final int[] STRING_LENGTHS = {10, 20, 50, 100};
		
		System.out.println(String.format("Calculating Edit Distance for %d pairs of random strings:", PAIRS_NUMBER));
		System.out.println(("Pairs\tStrLen\tAvg time(ns)"));
		
		for (int len : STRING_LENGTHS) {
			testEditInstances(PAIRS_NUMBER, len);
		}
	}
}
