package assignment1;

import hashTable.*;

/**
 * Test hash tables for questions 1, 2, and 3.
 * @author Wen Dong
 *
 */
public class TestHashTables {
	
	static private void insertHT(CuckooHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.insert(strList[i]);
		}		
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}

	static private void deleteFromHT(CuckooHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.remove(strList[i]);
		}
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}
	
	
	static private void insertHT(QuadraticProbingHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.insert(strList[i]);
		}		
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}

	static private void deleteFromHT(QuadraticProbingHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.remove(strList[i]);
		}
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}
	
	
	static private void insertHT(SeparateChainingHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.insert(strList[i]);
		}		
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}

	static private void deleteFromHT(SeparateChainingHashTable<String> hashTable, String[] strList)
	{
		int n = strList.length;
		
		long startTime = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			hashTable.remove(strList[i]);
		}
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);
	}
	
	
	
	public static void main(String[] args)
	{
		final int STRING_LENGTH = 10;
		final int MAX_EXP = 20;
		
		System.out.println("Insertion(I) and Deletion(D) average time in nano seconds along with pow(2, i):");
		System.out.println("----------------------------------------------------------------------");
		System.out.println("i\tCuck I\tCuck D\tQuad I\tQuad D\tSpCh I\tSpCh D");
		for (int i=1; i<=MAX_EXP; i++)
		{
			int n = (int) Math.pow(2, i);
			
			System.out.format("%d\t", i);
			
			String[] strList = RandomStringGenerator.createRandomStringArray(n, STRING_LENGTH);
			String[] stringArrayForDeletion = RandomStringGenerator.createRandomStringArray(n, STRING_LENGTH);
			
			CuckooHashTable<String> cuckooHashTable = new CuckooHashTable<String>(new StringHashFamily(3), 2000);
			insertHT(cuckooHashTable, strList);
			deleteFromHT(cuckooHashTable, stringArrayForDeletion);
			
			QuadraticProbingHashTable<String> quadraticProbingHashTable = new QuadraticProbingHashTable<String>();
			insertHT(quadraticProbingHashTable, strList);
			deleteFromHT(quadraticProbingHashTable, stringArrayForDeletion);
			
			SeparateChainingHashTable<String> separateChainingHashTable = new SeparateChainingHashTable<String>();
			insertHT(separateChainingHashTable, strList);
			deleteFromHT(separateChainingHashTable, stringArrayForDeletion);
			System.out.println();
		}
	}

}
