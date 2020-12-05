/**
 * 
 */
package assignment1;

import java.util.Random;

/**
 * For generating random strings of given length.
 * @author donwen
 *
 */
public class RandomStringGenerator {
	static private final int MAX_STRING_LENGTH = 256;
	static private Random aR = new Random();
	static private char[] charArray = new char[MAX_STRING_LENGTH];
	
	/**
	 * Create a random string with given length.
	 * @param length the length of the string to create.
	 * @return the created random string.
	 */
	static public String createRandomString(int length)
	{
		assert length <= MAX_STRING_LENGTH : "too long a length for random string to create.";
		
		int[] intArray = aR.ints(length, 'a', 'z'+1).toArray();
		
		for (int i=0; i<length; i++)
		{
			charArray[i] = (char)intArray[i];
		}
		return String.valueOf(charArray).substring(0, length);
	}
	
	/**
	 * Create an array of random string with given length.
	 * @param n the size of the array to create
	 * @param stringLength the length of the string in the array
	 * @return the created array of strings.
	 */
	public static String[] createRandomStringArray(int n, int stringLength)
	{
		String[] strList = new String[n];
		for (int i=0; i<n; i++)
		{
			strList[i] = createRandomString(stringLength);
		}
		return strList;
	}
	
	public static void main(String[] args) 
	{
		String[] ss = createRandomStringArray(10, 2);
		for (String s:ss)
		{
			System.out.print(s + ",");
		}
	}
}
