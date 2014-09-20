package edu.buffalo.cse.irf14.analysis;

public class SymbolTokenFilter {
/* 	It should act on the following symbols with actions as 
	described:
*  	Any punctuation marks that possibly mark the end of a sentence (. ! ?) should be removed. 
	Obviously if the symbol appears within a token it should be retained (a.out for example).
*  	Any possessive apostrophes should be removed (‘s s’ or just ‘ at the end of a word). 
	Common contractions should be replaced with expanded forms but treated as one token. (e.g. 
	should’ve => should have). All other apostrophes should be removed.
*  	If a hyphen occurs within a alphanumeric token it should be retained (B-52, at least one 
	of the two constituents must have a number). If both are alphabetic, it should be replaced with 
	a whitespace and retained as a single token (week-day => week day). Any other hyphens 
	padded by spaces on either or both sides should be removed.
*/
}
