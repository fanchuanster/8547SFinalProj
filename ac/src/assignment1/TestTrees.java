/**
 * 
 */
package assignment1;

import java.util.Random;
import java.util.stream.IntStream;

import searchtrees.AVLTree;
import searchtrees.BinarySearchTree;
import searchtrees.RedBlackBST;
import searchtrees.SplayTree;

/**
 * Test trees for questions 4, 5, and 6.
 * @author Wen Dong
 *
 */
public class TestTrees {
	
	/**
	 * Perform insertion, search, or deletion action on a given tree object.
	 * @param treeObject the tree object to action on.
	 * @param action the action type - insertion, search or deletion.
	 * @param numbers the action parameters.
	 */
	@SuppressWarnings("unchecked")
	private static void actionTree(Object treeObject, String action, int[] numbers) {
		
		int n = numbers.length;
		
		long startTime = System.nanoTime();
		
		if (treeObject instanceof AVLTree<?>) {
			AVLTree<Integer> tree = (AVLTree<Integer>) treeObject;
			for (int i=0; i<n; i++)
			{
				if (action.equals("insert")) {
					tree.insert(numbers[i]);
//					tree.checkBalance();
				} else if (action.equals("search")) {
					tree.contains(numbers[i]);
				} else if (action.equals("deletion")) {
					tree.remove(numbers[i]);
//					tree.checkBalance();
				}
			}
		} else if (treeObject instanceof BinarySearchTree<?>) {
			BinarySearchTree<Integer> tree = (BinarySearchTree<Integer>) treeObject;
			for (int i=0; i<n; i++) {
				if (action.equals("insert")) {
					tree.insert(numbers[i]);
				} else if (action.equals("search")) {
					tree.contains(numbers[i]);
				} else if (action.equals("deletion")) {
					tree.remove(numbers[i]);
				}
			}
		} else if (treeObject instanceof RedBlackBST<?, ?>) {
			RedBlackBST<Integer, Integer> tree = (RedBlackBST<Integer, Integer>) treeObject;
			for (int i=0; i<n; i++)
			{
				if (action.equals("insert")) {
					tree.put(numbers[i], numbers[i]);
				} else if (action.equals("search")) {
					tree.get(numbers[i]);
				} else if (action.equals("deletion")) {
					if (tree.get(numbers[i]) != null) {
						tree.delete(numbers[i]);
					}
				}
			}
		} else if (treeObject instanceof SplayTree<?>) {
			SplayTree<Integer> tree = (SplayTree<Integer>) treeObject;
			for (int i=0; i<n; i++)
			{
				if (action.equals("insert")) {
					tree.insert(numbers[i]);
				} else if (action.equals("search")) {
					tree.contains(numbers[i]);
				} else if (action.equals("deletion")) {
					tree.remove(numbers[i]);
				}
			}
		}
		
		long totalTime = System.nanoTime() - startTime;
		
		System.out.format("%d\t", totalTime/n);	
	}
	
	/**
	 * Create a tree instance of a given tree type.
	 * @param treeType the tree type to create - BST, AVL, RedBlck, or Splay.
	 * @return the created tree instance.
	 */
	private static Object CreateTree(String treeType) {
		switch (treeType) {
		case "BST":
			return new BinarySearchTree<Integer>();
		case "AVL":
			return new AVLTree<Integer>();
		case "RedBlck":
			return new RedBlackBST<Integer, Integer>();
		case "Splay":
			return new SplayTree<Integer>();
		default:
			return null;
		}
	}
	
	/**
	 * Test a series of actions on a given tree in response to questions 4, 5, and 6.
	 * @param treeType
	 * @param numbers
	 * @param randomNumbers
	 * @param randomNumbers2
	 * @param reversedNumbers
	 */
	private static void testTree(String treeType, int[] numbers, int[] randomNumbers, int[] randomNumbers2, int[] reversedNumbers) {
		System.out.print(String.format("%s\t", treeType));
		
		Object treeObject = CreateTree(treeType);
		actionTree(treeObject, "insert", numbers);
		actionTree(treeObject, "search", randomNumbers);
		actionTree(treeObject, "deletion", reversedNumbers);
		
		
		Object treeObject2 = CreateTree(treeType);
		actionTree(treeObject2, "insert", randomNumbers);
		actionTree(treeObject2, "search", randomNumbers2);
		actionTree(treeObject2, "deletion", randomNumbers2);
		
		System.out.println();
	}
	
	/**
	 * The main entry of the tree test for questions 4, 5, and 6.
	 * @param args
	 */
	public static void main(String[] args) {
		Random aR = new Random();
		final int n = 100000;
		
		int[] numbers = IntStream.rangeClosed(1, n).toArray();
		int[] randomNumbers = aR.ints(n, 1, n+1).toArray();
		int[] randomNumbers2 = aR.ints(n, 1, n+1).toArray();
		int[] reversedNumbers = IntStream.rangeClosed(1, n).map(i -> numbers[n-i]).toArray();
		
		assert n==numbers.length : "must equal";
		assert n==randomNumbers.length : "must equal";
		assert n==randomNumbers2.length : "must equal";
		assert n==reversedNumbers.length : "must equal";
		
		System.out.println("Average time in nano seconds for questions in 4 and 5:");
		System.out.println("----------------------------------------------------------------------");
		System.out.println("Tree\t4.a\t4.b\t4.c\t5.a\t5.b\t5.c");
		
		testTree("BST", numbers, randomNumbers, randomNumbers2, reversedNumbers);
		testTree("AVL", numbers, randomNumbers, randomNumbers2, reversedNumbers);
		testTree("RedBlck", numbers, randomNumbers, randomNumbers2, reversedNumbers);
		testTree("Splay", numbers, randomNumbers, randomNumbers2, reversedNumbers);
	}

}
