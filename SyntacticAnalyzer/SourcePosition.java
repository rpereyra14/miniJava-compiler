/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

/*
 * NOTE: Some of the code in this file may have been obtained from the Triangle compiler
 * provided by D.A. Watt and D.F. Brown in support of their textbook 
 * "Programming Language Processors in Java: Compilers and Interpreters" (ISBN 0-130-25786-9)
 */

package miniJava.SyntacticAnalyzer;

//maintains the position of a token in the input program
public class SourcePosition {

	public int start, finish;

	public SourcePosition () {
		start = 0;
		finish = 0;
	}

	public SourcePosition (int s, int f) {
		start = s;
		finish = f;
	}

}