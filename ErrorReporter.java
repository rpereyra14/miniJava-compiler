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

package miniJava;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ErrorReporter {

	public int numErrors;
	int RC_ERROR;

	ErrorReporter( int rc_code ) {
		RC_ERROR = rc_code;
		numErrors = 0;
	}

	public void reportSyntaxError(String message, String tokenName, SourcePosition pos) {
		
		System.out.print ("Parse Error: ");

		for (int p = 0; p < message.length(); p++){
			if (message.charAt(p) == '%'){
				System.out.print( tokenName );
			}else{
				System.out.print( message.charAt(p) );
			}
		}
		
		System.out.println(" " + pos.start + ".." + pos.finish);
		numErrors++;
		System.exit( RC_ERROR );
	}

	public void reportIdentificationError( String message ){
		if( numErrors == 0 )
			System.out.println( message );
		numErrors++;
		System.exit( RC_ERROR );
	}

	public void reportTypeError( String message ){
		System.out.println( message );
		numErrors++;
	}

}