package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
	{
		if( g==null || p1== null || p2 == null )return null;
		
		ArrayList<String> 	returnList 	= new ArrayList<String>();
		if(p1.equals(p2)) 	{ returnList.add(p1); return returnList; }
		boolean[] 			visitedNodes= new boolean[g.members.length];
		
		Stack<Person> stackIsGreat = new Stack<Person>();
		
		Queue<Person> 		mainQueue 	= new Queue<Person>();
		mainQueue.enqueue( g.members[ g.map.get(p1) ] );
		
		while( !mainQueue.isEmpty() )
		{
			Person firstInQueue = mainQueue.dequeue();
			
			visitedNodes[g.map.get(firstInQueue.name)] = true;
			Friend thisFriend = firstInQueue.first;
			while( thisFriend != null )
			{
				if( !visitedNodes[thisFriend.fnum] )						//check if visited
				{
					stackIsGreat.push(firstInQueue);
					visitedNodes[thisFriend.fnum] = true;
					if( g.members[thisFriend.fnum].name.equals(p2) )		//found it
					{
						stackIsGreat.push( g.members[thisFriend.fnum] );
						while( !stackIsGreat.isEmpty() )
						{
							Person thisPerson = stackIsGreat.pop();
							Friend killThisFriend = thisPerson.first;
							
							if( stackIsGreat.isEmpty() ) 
							{
								returnList.add(g.members[thisFriend.fnum].name);
								break;
							}
							Person lastPerson = stackIsGreat.peek();
							
							while(killThisFriend!=null)
							{
								if( g.members[ killThisFriend.fnum ].name.equals( lastPerson.name ) )
								{
									returnList.add(0,lastPerson.name);
									break;
								}
								killThisFriend = killThisFriend.next;
							}
						}
						return returnList;
					}
					mainQueue.enqueue( g.members[ thisFriend.fnum ]);
				}
				thisFriend = thisFriend.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		
		return null;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) 
	{
		
		return null;
	}
}
