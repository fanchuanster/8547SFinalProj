/**
 * 
 */
package assignment2;

import sorting.*;

import assignment1.RandomStringGenerator;

/**
 * @author donwen
 *
 */
public class TestSortingStrings {
	
	private static long testSorting(String algorithmName, String[] strings) {
		
		if (algorithmName.equals("Radixsort")) {
			long start = System.currentTimeMillis();
			RadixSort.radixSortA(strings, strings[0].length());
			return System.currentTimeMillis() - start;
		} else {
			return TestSorting.testSorting(algorithmName, strings);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int n = 100000;
		final int TEST_TIMES = 100;
		final int[] STRING_LENGTHS = { 4, 6, 8, 10 };
		
		
		String[] algorithms = { "Mergesort" , "Quicksort", "Heapsort", "dual-pivot", "Radixsort" };
		
		System.out.println(String.format("Testing sorting %d random strings for %d times:", n, TEST_TIMES));
		System.out.print("Avg time(ms)\t|\t");
		for (int i=0; i<STRING_LENGTHS.length; i++) {
			System.out.print(STRING_LENGTHS[i] + "\t");
		}
		System.out.println("\n------------------------------------------------------");
		
		for (String algorithm:algorithms) {
			System.out.print(algorithm + "\t|\t");
			for (int i=0; i<STRING_LENGTHS.length; i++) {
				int totalTime = 0;
				for (int j=0; j<TEST_TIMES; j++) {
					String[] randomStrings = RandomStringGenerator.createRandomStringArray(n, STRING_LENGTHS[i]);
					totalTime += testSorting(algorithm, randomStrings);
				}
				
				System.out.print(String.format("%d\t", totalTime/TEST_TIMES));
			}
			System.out.println();
		}
		

	}

}
