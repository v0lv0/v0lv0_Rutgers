package lse;	

import java.io.IOException;
import java.util.ArrayList;
public class TestMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		LittleSearchEngine engine= new LittleSearchEngine();
		/*ArrayList<Occurrence>occs = new ArrayList<Occurrence>();
		Occurrence a = 12;
		occs.add(0,12);*/
		
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
//		System.out.print("Enter document file name => ");//docs.txt
		
		String docsFile="docs.txt";

		//		System.out.print("Enter noise words file name => ");//noisewords.txt
		//		HashMap<String, Occurrence> example = engine.loadKeywordsFromDocument("Tyger.txt");
		//		for (String name : example.keySet()) {
		//		    System.out.println(name);
		//		}
		//		
		//		
		//		
		////		insertLastOccurrence
		//		int[] temp = {6,4};
		//        ArrayList<Integer> array_list = new ArrayList<Integer>(); 
		//        for (int i = 0; i < temp.length; i++) 
		//            array_list.add(new Integer(temp[i])); 
		//		ArrayList<Integer> intArray = engine.insertLastOccurrences(array_list);
		//		System.out.println(intArray + "      " + array_list); 
		String noiseWordsFile="noisewords.txt";
		engine.makeIndex(docsFile, noiseWordsFile);
		

		
		System.out.println(engine.top5search("bus", "Drops"));
	}
}