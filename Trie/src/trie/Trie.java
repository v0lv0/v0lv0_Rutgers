package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	private static int amountOfSameLetter(String st1, String st2)
	{
		int i = 0; 
		while(  i < st1.length()  &&   i< st2.length() && (  st1.charAt(i)==st2.charAt(i) ) )
		{i++;}
		return i;
	}
	
	
	private static void addToLocation(String[] allWords, int arrayIndex, String thisString, TrieNode leftmost )
	{
		TrieNode ptr = leftmost;
		
		while(ptr!= null)									//keeping toing to the right
		{
			
			String currnetString = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1);
			int commonLength = amountOfSameLetter(currnetString, thisString);
			
			System.out.println(commonLength);
			
			
			if( commonLength > 0 )						// go down if it can
			{
				if( ptr.firstChild != null)					// have child
				{
					System.out.println("have child");
					addToLocation(allWords, arrayIndex, thisString.substring(commonLength), ptr.firstChild); 	//add to the new sibling
				}
				else										// no child
				{
					System.out.println("no child");
					
					Indexes indexForFirstChild = ptr.substr;
					
					ptr.substr.endIndex = (short) (ptr.substr.startIndex + commonLength-1);		//shorten the parents
					
					indexForFirstChild.startIndex = (short)(indexForFirstChild.startIndex + commonLength);
					
					ptr.firstChild = new TrieNode( 												//second half of the parent
							indexForFirstChild,
							null, 
							null
							);
					
					ptr.firstChild.sibling = new TrieNode(
							new Indexes( 
									(short)(short)(indexForFirstChild.startIndex + commonLength),
									(short)0,
									(short)(thisString.length()-1) ),
							null, 
							null);
					
				}
			
			}
			ptr = ptr.sibling;
		}
		
		ptr = new TrieNode( 				// u dont fit
				new Indexes( 
						(short)arrayIndex,
						(short)0,
						(short)(thisString.length()-1) ),
				null, 
				null);
		return;
	}
	
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	
	public static TrieNode buildTrie(String[] allWords)
	{
		TrieNode root = new TrieNode( null,null,null );
		if(allWords.length == 0)
			return null;
		
		TrieNode theStart = new TrieNode( new Indexes( (short)0 , (short)0, (short)(allWords[0].length()-1) ), null, null);
		root.firstChild = theStart;
		
		for(int i = 1; i< allWords.length; i++)
		{
			addToLocation(allWords, i, allWords[i], theStart );
		}
		return root;
	}
	
	
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 	
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param pref ix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) 
	{
		
		
		
		
		
		
		return null;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
