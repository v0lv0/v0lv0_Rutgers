package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() 
	{
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	//complete
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException 
	{
		if(docFile == null)
			return null;
		
		
		Scanner scanner = new Scanner(new File(docFile));
		HashMap<String, Occurrence> returnMap = new HashMap<String, Occurrence>();
		
		while(scanner.hasNext())
		{
			String cur = scanner.next();
			
			cur = getKeyword(cur);
			if(cur != null)
			{
				cur = cur.toLowerCase();
				if( returnMap.containsKey(cur) )
					returnMap.get(cur).frequency++;
				else
					returnMap.put(cur, new Occurrence(docFile, 1));
			}
		}
		scanner.close();
		return returnMap;
	}

	//complete
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) 
	{
		for( String currentKey : kws.keySet())
		{
			if(keywordsIndex.containsKey(currentKey))
			{
				keywordsIndex.get(currentKey).add( kws.get(currentKey) );
				insertLastOccurrence( keywordsIndex.get(currentKey) );
			}
			else
			{
				ArrayList<Occurrence> smallList= new ArrayList<Occurrence>();
				smallList.add( kws.get(currentKey) );
				keywordsIndex.put(currentKey,smallList);
			}
		}
	}
	
	//complete
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) 
	{
		if (word.length() <= 1 || word ==null)
			return null;
		
		String input = word;
		if(input.replaceAll("[[a-zA-Z][.,?:;!]]", "").length()>0)
			return null;
		
		for(int i = 0; i < input.length(); i++)
		{
			if(Character.isAlphabetic(input.charAt(i)))
			{
				do{i++;} while(  i < input.length() && Character.isAlphabetic(input.charAt(i)) );
				
				while(i<input.length() && !Character.isAlphabetic(input.charAt(i)))
					i++;
				
				if(i<input.length())
					return null;
			}
		}
		String returnString = input.replaceAll("[^a-zA-Z]", "").toLowerCase();
		if( noiseWords.contains(returnString) )
			return null;
		return returnString;
	}
	
	//complete
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) 
	{
		ArrayList<Integer> testArray= new ArrayList<Integer>();
		if(occs.size() > 1)
		{
			{
				int right = occs.size()-2;
				int left  = 0;
				int frequency = occs.get(occs.size()-1).frequency;
				int mid =(left+right)/2;
				while(left<=right)
				{
					mid = (left+right)/2;
					testArray.add(mid);
					if(frequency < occs.get(mid).frequency)
						left = mid+1;
					if(frequency > occs.get(mid).frequency)
						right = mid-1;
					if(frequency == occs.get(mid).frequency)
						break;
				}
				if( occs.get(mid).frequency < occs.get(occs.size()-1).frequency )
				{
					occs.add( mid, occs.get(occs.size()-1 ));
					occs.remove(occs.size()-1);
				}
				else
				{
					occs.add( mid+1, occs.get(occs.size()-1 ));
					occs.remove(occs.size()-1);
				}
			}
		}
		return testArray;
	}
	
	/*
	public ArrayList<Integer> insertLastOccurrences(ArrayList<Integer> occs) 
	{
		ArrayList<Integer> testArray= new ArrayList<Integer>();
		if(occs.size() > 1)
		{
			{
				int right = occs.size()-2;
				int left  = 0;
				int frequency = occs.get(occs.size()-1);
				int mid =(left+right)/2;
				while(left<=right)
				{
					mid = (left+right)/2;
					testArray.add(mid);
					if(frequency < occs.get(mid))
						left = mid+1;
					if(frequency > occs.get(mid))
						right = mid-1;
					if(frequency == occs.get(mid))
						break;
				}
				if( occs.get(mid) < occs.get(occs.size()-1 ) )
				{
					occs.add( mid, occs.get(occs.size()-1 ));
					occs.remove(occs.size()-1);
				}
				else
				{
					occs.add( mid+1, occs.get(occs.size()-1 ));
					occs.remove(occs.size()-1);
				}
			}
		}
		return testArray;
	}
	*/
	
	//complete defalt
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) 
		{
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	//complete
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) 
	{
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		ArrayList<Occurrence> ArrayList1    = new ArrayList<Occurrence>();		
		ArrayList<Occurrence> ArrayList2    = new ArrayList<Occurrence>();
		ArrayList<String> topFiveString = new ArrayList<String>();
		
		boolean occKW1 = keywordsIndex.containsKey(kw1);
		boolean occKW2 = keywordsIndex.containsKey(kw2);
		
		if((!occKW1)&&(!occKW2))
			return null;
		
		if(occKW1)
		{
			ArrayList1 = keywordsIndex.get(kw1);
			if(occKW2)													//in both
			{
				ArrayList2 = keywordsIndex.get(kw2);
				
				

//				for(int i = 0; i<ArrayList1.size(); i++)
//					System.out.println(ArrayList1.get(i).frequency + " " + ArrayList1.get(i).document);
//				System.out.println("*******************");
//				
//				for(int i = 0; i<ArrayList2.size(); i++)
//					System.out.println(ArrayList2.get(i).frequency + " " + ArrayList2.get(i).document);
//				System.out.println("*******************");
				
				int i=0, j=0;
				while( topFiveString.size()<5  )
				{
					if(ArrayList1.size() > i)
					{
						if(ArrayList2.size() > j)
						{
							if(ArrayList1.get(i).frequency < ArrayList2.get(j).frequency)
							{
//								System.out.println(ArrayList2.get(j).document + " " +ArrayList2.get(j).frequency);
								if(!topFiveString.contains(ArrayList2.get(j).document) )
									topFiveString.add(ArrayList2.get(j).document);
								j++;
							}
							else
							{
//								System.out.println(ArrayList1.get(i).document + " " +ArrayList1.get(i).frequency);
								if(!topFiveString.contains(ArrayList1.get(i).document))
									topFiveString.add(ArrayList1.get(i).document);
								i++;
							}
						}
						else
						{
//							System.out.println(ArrayList1.get(i).document +  " " +ArrayList1.get(i).frequency);
							if(!topFiveString.contains(ArrayList1.get(i).document))
								topFiveString.add(ArrayList1.get(i).document);
							i++;
						}
					}
					else
					{
						if(ArrayList2.size() > j)
						{
//							System.out.println(ArrayList2.get(j).document + " " + ArrayList2.get(j).frequency);
							if(!topFiveString.contains(ArrayList2.get(j).document) )
								topFiveString.add(ArrayList2.get(j).document);
							j++;
						}
						else
						{
							break;
						}
					}
				}
			}
			else														//in 1 not in 2
			{
				for(int i = 0; i < ArrayList1.size() && i < 5; i++)
					topFiveString.add( ArrayList1.get(i).document );
			}
		}
		else
		{
			if(occKW2)													//in 2 not in 1
			{
				ArrayList2 = keywordsIndex.get(kw2);
				
				for(int i = 0; i < ArrayList2.size() && i < 5; i++)
					topFiveString.add( ArrayList2.get(i).document );
			}
			else														//neither
				return null;
		}
		return topFiveString;
	}
}