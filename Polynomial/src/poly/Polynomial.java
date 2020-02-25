package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial 
{	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	
	public static Node read(Scanner sc) 
	throws IOException 
	{
		Node poly = null;
		while (sc.hasNextLine()) 
		{
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	
	
	// triverse the whole ll
	private static Node reverse(Node fir)
	{
		Node left = null;
		Node mid = fir;
		Node right = null;
		
		while(mid != null)
		{
			right = mid.next;
			mid.next = left;
			left = mid;
			mid = right;
		}
		fir = left;
		return fir;
	}
	
	
	
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2)
	{
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		
//		if(poly1 == null)
//		{
//			if(poly2 == null)
//				return null;
//			return poly2;
//		}
//		else if (poly2 == null)
//			return poly1;
		
		
		Node ptrPre = null;
		Node ptr = null;
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		
		while (ptr1 != null && ptr2 != null)
		{
			if(ptr1.term.degree == ptr2.term.degree)
			{
				if(ptr1.term.coeff + ptr2.term.coeff != 0)
				{
					ptr = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, ptrPre);
					ptrPre = ptr;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			
			}else if(ptr1.term.degree < ptr2.term.degree)
			{
				ptr = new Node(ptr1.term.coeff, ptr1.term.degree, ptrPre);
				ptr1 = ptr1.next;
				ptrPre = ptr;
			}else if(ptr1.term.degree > ptr2.term.degree)
			{
				ptr = new Node(ptr2.term.coeff, ptr2.term.degree, ptrPre);
				ptr2 = ptr2.next;
				ptrPre = ptr;
			}
		}
		
		
		if (ptr1 != null)
		{
			while(ptr1 != null)
			{
				ptr = new Node(ptr1.term.coeff, ptr1.term.degree, ptrPre);
				ptr1 = ptr1.next;
				ptrPre = ptr;
			}
		}
		else if (ptr2 != null)
		{
			while(ptr2 != null)
			{
				ptr = new Node(ptr2.term.coeff, ptr2.term.degree, ptrPre);
				ptr2 = ptr2.next;
				ptrPre = ptr;
			}
		}
		
		Node ret = reverse(ptr);
		
		return ret;
	}
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) 
	{
		if(poly1 == null || poly2 == null)
			return null;
		
		Node big = null;
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		
		while (ptr1 != null )
		{

			Node ptrSmall = null;
			Node ptrPrev = null;
			poly2 = ptr2;
			while (poly2 != null)
			{
				if(poly2.term.coeff*ptr1.term.coeff != 0)
					ptrSmall = new Node(ptr1.term.coeff * poly2.term.coeff, ptr1.term.degree + poly2.term.degree, ptrPrev);
				ptrPrev = ptrSmall;
				
				poly2 = poly2.next;
			}
			
			ptrSmall = Polynomial.reverse(ptrSmall);

			big = Polynomial.add(big, ptrSmall);
			
			ptr1 = ptr1.next;
			
		}
		
		return big;

	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) 
	{
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly == null)
			return 0;
		
		float ret = 0;
		
		while(poly != null)
		{
			float xToN = 1;
			
			for(int i = 1; i <= poly.term.degree; i++)
			{
				xToN *= x;
			}
			
			ret += poly.term.coeff*xToN;
			poly = poly.next;
		}
		
		return ret;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) 
			return "0";
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ; current = current.next) 
		{
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
