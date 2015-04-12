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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * Handler class for source file.
 */
public class SourceFile {

	static final char eol = '\n';			/*Newline character.*/
	static final int EOT = -1; 				/*Used to determine where the input file's EOF occurs.*/
	static final char eot = '\u0000'; 		/*EOF unicode character code.*/
	static final int rc = 4; 				/*Response value if file cannot be opened.*/

	private int currentLine;				/*Tracker variable to determine input line number.*/

	File sourceFile;					/*The input file to be parsed.*/
	FileInputStream source;				/*Handler to have a constant stream of bytes from sourceFile.*/

	/*Constructor. Open file byte stream and initialize currentLine, colNumber.*/
	public SourceFile ( String filename ) {
		try {
			sourceFile = new File( filename );
			source = new FileInputStream( sourceFile );
			currentLine = 1;
		}
		/*Exit with status code -1 if the input file cannot be opened.*/
		catch ( IOException s ) {
			System.err.println( "Fatal Error: " + filename + " could not be opened." );
			System.exit( rc );
		}
	}

	/*Get the next character from the input stream. Adjust currentLine and colNumber appropriately.*/
	public char getChar () {
		try {
			int c = source.read();
			if (c == EOT) { 
				return eot;
			}else if( c == eol ){
				currentLine++;
			}
			
			return (char) c;
			
		}
		/*In the event of reading past the EOF, keep returning EOF.*/
		/*This allowes for the parser to realize there was a syntax error.*/
		catch ( IOException s ) {
			return eot;
		}
	}
	
	public int getCurrentLine () {
		return currentLine;
	}

}
