package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression 
{

	public static String delims = " \t*+-/()[]";
	
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	
	// return true if its a string of letters
	private static boolean isStringLetter(final String input) 
	{
		
		if (input == null || input.length()==0)
			return false;
		
		for(int i = 0; i < input.length(); i++)
			if(Character.isLetter(input.charAt(i)))
				return true;
		return false;
	}
	
	private static boolean isStringNumber(final String input) 
	{
		
		if (input == null || input.length()==0)
			return false;
		
		for(int i = 0; i < input.length(); i++)
			if(Character.isDigit(input.charAt(i)))
				return true;
		return false;
	}
    private static boolean doesArrExist(ArrayList<Array> arrays, String name)
    {
    	for(int i = 0; i < arrays.size(); i++)
    	{
    		if(arrays.get(i).name.equals(name))
    			return true;
    	}
		return false;
    }
    
    /*finding the right Variable object*/
    private static boolean doesVarExist(ArrayList<Variable> vars, String name)
    {
    	for(int i = 0; i < vars.size(); i++)
    	{
    		if(vars.get(i).name.equals(name))
    			return true;
    	}
    	return false;
    }
    
    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	StringTokenizer st = new StringTokenizer(expr, " \t*+-/()]1234567890");
    	
    	while(st.hasMoreTokens())
    	{
    		String variableAndArray = st.nextToken();
    		
    		if(variableAndArray.contains("["))
    		{
    			StringTokenizer arrayTokenizer=new StringTokenizer(variableAndArray,"[",true);
				Stack<String> checkBracket = new Stack<String>();
				
    			while(arrayTokenizer.hasMoreElements())
    			{
    				String arrayNameString = arrayTokenizer.nextToken();
    				
    				if(arrayNameString.equals("[") )
    				{
    					String thisArray = checkBracket.pop();
    					if(!doesArrExist(arrays ,thisArray))
    						arrays.add( new Array(thisArray) );
    				}
    				else
    				{
    					checkBracket.push(arrayNameString);
    				}
    			}
    			while(!checkBracket.isEmpty())
    			{
    				String thisVar = checkBracket.pop();
					if(!doesVarExist(vars ,thisVar))
						vars.add( new Variable(thisVar) );
    			}
    		}
    		else
    		{
    			if(!doesVarExist(vars ,variableAndArray))
					vars.add( new Variable(variableAndArray) );
    		}
    	}
    	
    	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /* auto compute stuff */
    private static float autoCompute(float num1, char operation, float num2)
    {
		switch (operation) 
		{
		case '+':
			return num1+num2;
		case '-':
			return num1-num2;
		case '*':
			return num1*num2;
		case '/':
			return num1/num2;
		default:
			break;
		}
		return 0;
	}
    
    /*finding the right array object*/
    private static Array findArr(ArrayList<Array> arrays, String name)
    {
    	for(int i = 0; i < arrays.size(); i++)
    	{
    		if(arrays.get(i).name.equals(name))
    			return arrays.get(i);
    	}
		return null;
    }
    
    /*finding the right Variable object*/
    private static Variable findVar(ArrayList<Variable> vars, String name)
    {
    	for(int i = 0; i < vars.size(); i++)
    	{
    		if(vars.get(i).name.equals(name))
    			return vars.get(i);
    	}
    	return null;
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    {
    	expr = expr.replaceAll(" "	 ,"");
    	expr = expr.replaceAll("\t" ,"");
    	
		StringTokenizer st = new StringTokenizer(expr, delims, true);		// \t*+-/()[]
		
    	Stack<String> operatorInStack = new Stack<String>();
    	Stack<Float>  numberInStack	  = new Stack<Float>();
    	String varOrArray = null;
    	
		while( st.hasMoreTokens() )
		{			
			String thisString = st.nextToken();
//			System.out.println("current string:  " + thisString);
			
			
			if( thisString.equals("["))
			{
				String arrayName = varOrArray;
				
				int leftBracketCount = 1;
				int lengthCount = 0;
				while( leftBracketCount != 0 ) 
				{
					String skippedTokens = st.nextToken();
					if(skippedTokens.equals("]"))
						leftBracketCount--;
					if(skippedTokens.equals("["))
						leftBracketCount++;
					lengthCount++;
				}


				int leftBracketLocation = expr.indexOf('[')+1;
				int indexOfArray = (int)evaluate(expr.substring( leftBracketLocation/*, leftBracketLocation + lengthCount*/), vars, arrays);
				
				int floatInArray = findArr(arrays, arrayName).values[indexOfArray];

				numberInStack.push((float)floatInArray);
				varOrArray = null;
				expr = expr.substring(leftBracketLocation+lengthCount);
			}
			
			if( thisString.equals("("))
			{
//				numberInStack.push( evaluate(expr.substring( expr.indexOf('(')+1 ), vars, arrays));
//				break;
				int leftBracketCount = 1;
				int lengthCount = 0;
				while( leftBracketCount != 0 ) 
				{
//					System.out.println("parenthese count " + leftBracketCount);
					String skippedTokens = st.nextToken();
//					System.out.println("Possible parenthese " + skippedTokens);
					if(skippedTokens.equals(")"))
						leftBracketCount--;
					if(skippedTokens.equals("("))
						leftBracketCount++;
					lengthCount++;
				}
				
				int leftParenthesesLocation = expr.indexOf('(')+1;
				float insideParenthese = evaluate(expr.substring( leftParenthesesLocation/*, leftParenthesesLocation */), vars, arrays);

				numberInStack.push(insideParenthese);
				expr = expr.substring(leftParenthesesLocation+lengthCount);
			}
//			if( thisString.equals(")"))
//			{
//				while( operatorInStack.size() > 0)
//				{
//					float	secondThingy	= numberInStack.pop();
//					char	operation		= operatorInStack.pop().charAt(0);
//					float	firstThingy		= numberInStack.pop();
//					
//					numberInStack.push( autoCompute( firstThingy, operation, secondThingy) );
//				}
//			}
			
			if(varOrArray != null)
			{	numberInStack.push( (float)findVar(vars, varOrArray).value);		//last number was a var
				varOrArray = null;			}
			

			
			if(isStringNumber(thisString))								//push in numbers
			{ numberInStack.push(Float.parseFloat(thisString) ); }
			
			if(isStringLetter(thisString))					//temp string varOrArray, and next loop to check.
			{
				if(st.hasMoreTokens())
				{
					varOrArray = thisString;
				}
				else
				{
					numberInStack.push( (float)findVar(vars, thisString).value);
				}
			}
			
			
			
			if( thisString.equals("+") || thisString.equals("-") ||
					thisString.equals("*") || thisString.equals("/")   )
			{

				
				if( operatorInStack.size() >= 2 && numberInStack.size()>=3 && (thisString.equals("*")||thisString.equals("/")) )
				{
					float	secondThingy	= numberInStack.pop();
					float	firstThingy		= numberInStack.pop();
					char 	opI 			= operatorInStack.pop().charAt(0);

//					System.out.println( "Ive done : " + autoCompute( firstThingy, opI, secondThingy)  ) ;
					numberInStack.push( autoCompute( firstThingy, opI, secondThingy) );
				}
				
				
				
				if( operatorInStack.size() >= 2 && numberInStack.size()>=3 && (thisString.equals("+")||thisString.equals("-")) )
				{
					float	thirdThingy		= numberInStack.pop();
					float	secondThingy	= numberInStack.pop();
					float	firstThingy		= numberInStack.pop();
					char 	opI 			= operatorInStack.pop().charAt(0);
					char 	opII 			= operatorInStack.pop().charAt(0);

					numberInStack.push( autoCompute( firstThingy, opII, autoCompute(secondThingy, opI, thirdThingy)) );
				}
				
				
				if(operatorInStack.size() > 0 )
				{	
					char lastOp = operatorInStack.peek().charAt(0);
					if( !((lastOp == '+' || lastOp == '-') && (thisString.equals("*") || thisString.equals("/")) ) )
					{
						float	secondThingy	= numberInStack.pop();
						float	firstThingy		= numberInStack.pop();
						operatorInStack.pop();
//						System.out.println( "Ive done it: " + firstThingy+" "+ lastOp + " "+ secondThingy + " = " +
//								autoCompute( firstThingy, lastOp, secondThingy));
							
						numberInStack.push( autoCompute( firstThingy, lastOp, secondThingy) );
					}
				}
				
				
				
				
				operatorInStack.push(thisString);
			}
			
						
			if( thisString.equals("]") || thisString.equals(")") )						// can only be seen inside a recursion
			{break;}
		}
		
		
		
		while( operatorInStack.size() > 0)
		{
			float	secondThingy	= numberInStack.pop();
			char	operation		= operatorInStack.pop().charAt(0);
			float	firstThingy		= numberInStack.pop();
			numberInStack.push( autoCompute( firstThingy, operation, secondThingy) );
		}
		if(numberInStack.size()>0)
			return numberInStack.pop();
		return 0;
    }
}