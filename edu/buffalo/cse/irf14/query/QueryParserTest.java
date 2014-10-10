package edu.buffalo.cse.irf14.query;

import java.util.Scanner;
/////////////////////////////////
// infinite loop query tester////
/////////////////////////////////
public class QueryParserTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner terminalInput = new Scanner(System.in);
		while(true){ 
		System.out.println("Enter your Query:");
		String s = terminalInput.nextLine();
		Query q =QueryParser.parse(s, "OR");
		q.toString();
		for(String s1: q.queryString) if(!s1.trim().equals("")) System.out.print(s1+" ");
		System.out.println();
		}
	}

}
