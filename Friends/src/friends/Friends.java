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
		
		ArrayList<String> 	returnList 	= new ArrayList<>();
		if(p1.equals(p2)) 	{ returnList.add(p1); return returnList; }
		boolean[] 			visitedNodes = new boolean[g.members.length];
		
		Stack<Person> stackIsGreat = new Stack<>();
		
		Queue<Person> 		mainQueue 	= new Queue<>();
		mainQueue.enqueue( g.members[ g.map.get(p1) ] );
		visitedNodes[ g.map.get(p1) ] = true;
		
		
		
		while( !mainQueue.isEmpty() )
		{
			Person firstInQueue = mainQueue.dequeue();
			stackIsGreat.push(firstInQueue);
			Friend thisFriend = firstInQueue.first;
			while( thisFriend != null )
			{
				if( !visitedNodes[thisFriend.fnum] )						//check if visited
				{
					visitedNodes[thisFriend.fnum] = true;
					mainQueue.enqueue( g.members[ thisFriend.fnum ]);
					if( g.members[thisFriend.fnum].name.equals(p2) )		//found it
					{
						stackIsGreat.push( g.members[thisFriend.fnum] );
						while( !stackIsGreat.isEmpty() )
						{
							Person thisPerson = stackIsGreat.pop();
							Friend removeFriend = thisPerson.first;
							
							if( stackIsGreat.isEmpty() ) 
							{
								returnList.add(g.members[thisFriend.fnum].name);
								break;
							}
							Person secondPerson = stackIsGreat.peek();
							while(removeFriend!=null)
							{
								String thisname = g.members[ removeFriend.fnum ].name;					
								if( thisname.equals( secondPerson.name ) )
								{
									returnList.add(0,secondPerson.name);
									break;
								}
								removeFriend = removeFriend.next;
							}
						}
						for( int i=returnList.size()-1; i > 1; i-- )
						{
							String thisName = returnList.get(i);
							Person right = g.members[ g.map.get(thisName) ];
							Friend allFriends = right.first;
							
							while(allFriends != null)
							{
								if( g.members[allFriends.fnum].name.equals(returnList.get(i-2)) )
								{
									returnList.remove(i-1);
									i++;
								}
								allFriends = allFriends.next;
							}
						}
						return returnList;
					}
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
		if(g == null || school == null)return null;
		ArrayList<ArrayList<String>> finalReturnArrayList = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		
		for(int i = 0; i < g.members.length; i++ )
		{
			if(g.members[i].school!= null && g.members[i].school.equals(school) && !visited[i] )
			{
				visited[i] = true;
				ArrayList<String> thisArrayList = new ArrayList<>();
				thisArrayList.add(g.members[i].name);
				Queue<Person> 		mainQueue 	= new Queue<>();
				mainQueue.enqueue( g.members[i] );
				
				while( !mainQueue.isEmpty() )
				{
					Person firstInQueue = mainQueue.dequeue();
					Friend thisFriend = firstInQueue.first;
					
					while( thisFriend != null )
					{
						if(g.members[thisFriend.fnum].school!= null && g.members[thisFriend.fnum].school.equals(school) && !visited[thisFriend.fnum] )
						{
							visited[thisFriend.fnum] = true;
							thisArrayList.add(g.members[thisFriend.fnum].name);
							mainQueue.enqueue( g.members[ thisFriend.fnum ]);
						}
						thisFriend = thisFriend.next;
					}
				}
				finalReturnArrayList.add(thisArrayList);
			}
		}
		return finalReturnArrayList;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	
	private static int depth = 0;
	
	private static ArrayList<String> dfs( int[] dfsnum, int[] back, boolean[] visited, Graph g, int index)
	{
		if(g == null || index >= g.members.length )return null;
		ArrayList<String> arrayList	= new ArrayList<String>();
		Person thisPerson			= g.members[index];
		Friend allTheFriends 		= thisPerson.first;
		depth = depth+1;
		visited[index] = true;
		dfsnum[index] = depth;
		back[index] = depth;
		
		int thisDepth = depth;
		boolean visitedHead = false;
		boolean stuff = false;
		while(allTheFriends != null)
		{
			
			if(!visited[allTheFriends.fnum])
			{
				visited[allTheFriends.fnum] = true;
				arrayList.addAll(dfs(dfsnum,back,visited, g, allTheFriends.fnum));
				if( dfsnum[index]<=back[allTheFriends.fnum]  )		//is a connector
				{
					if( thisDepth > 1 )
					{
						if(arrayList.indexOf(g.members[index].name)<0 )
							arrayList.add(g.members[index].name);
					}
					else
					{
						if(arrayList.indexOf(g.members[index].name)<0 )
						{	
							visitedHead = true;
						}
						else
							visitedHead = false;
						
						if(allTheFriends.next == null && visitedHead && stuff)
							arrayList.add(g.members[index].name);
						stuff = true;
					}
				}
				else
				{
					back[index] = Math.min(back[index], back[allTheFriends.fnum]);
				}
			}
			else
			{
				if(dfsnum[index] > back[allTheFriends.fnum])
					back[index] = Math.min(back[index], dfsnum[allTheFriends.fnum]);
			}
			allTheFriends = allTheFriends.next;
		}
		return arrayList;
	}
	
	
	
	
	
	
	
	
	public static ArrayList<String> connectors(Graph g) 
	{
		ArrayList<String> returnList = new ArrayList<String>();
		
		boolean[] 	visited	= new boolean[g.members.length];
		
		for(int i = 0; i< g.members.length; i++)
		{
			
			if(!visited[i]) {
				int[] 		dfsnum 	= new int[g.members.length];
				int[] 		back 	= new int[g.members.length];
				depth = 0;
				ArrayList<String> thisList = dfs(dfsnum,back,visited,g,i);
				
				returnList.addAll(thisList);
			}
		}
		return returnList;
	}
}
