package edu.buffalo.cse.irf14.query;

import java.util.Scanner;

public class QueryParserTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner terminalInput = new Scanner(System.in);
		String s = terminalInput.nextLine();
		//"Category:War AND Author:Divya Dutta AND Place:Baghdad AND prisoners detainees rebels"
		QueryParser.parse(s, "OR");
	}

}
