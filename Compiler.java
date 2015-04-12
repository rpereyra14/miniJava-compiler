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

import miniJava.ErrorReporter;
import miniJava.SyntacticAnalyzer.*;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.ContextualAnalyzer.*;
import miniJava.CodeGenerator.*;

class Compiler{

	private static final int ERROR = 4;
	private static final int SUCCESS = 0;

	private static String filename;

	private static ErrorReporter reporter;
	private static SourceFile source;
	private static Scanner scanner;
	private static Parser parser;
	private static ASTDecorator decorator;
	private static TypeChecker checker;
	private static Encoder encoder;

	private static void compileProgram () {
		try{
			reporter = new ErrorReporter( ERROR );
			source = new SourceFile( filename );
			scanner = new Scanner( source );
			parser = new Parser( scanner, reporter );
			decorator = new ASTDecorator( reporter );
			checker = new TypeChecker( reporter );
			encoder = new Encoder( filename.replaceAll("(\\.mjava)|(\\.java)","") );
			ASTDisplay displayer = new ASTDisplay();
	
			AST ast = parser.parsePackage();
			decorator.decorateAST( ast );
			checker.checkTypes( ast );
			
			if( reporter.numErrors > 0 )
				System.exit( ERROR );
				
			encoder.generateCode( ast ); 
				
			//displayer.showTree( ast );
			System.exit( SUCCESS );
		}catch( Exception e ){
			System.out.println( "***Compilation error encountered" );
			System.exit( ERROR );
		}
	}

	public static void main ( String[] args ) {

		if( args.length != 1 ){
			System.err.println( "Usage: java Compiler <filename>" );
			System.exit( ERROR );
		}
		
		filename = args[0];
		
		if( !filename.endsWith( ".mjava" ) && !filename.endsWith( ".java" ) ){
			System.err.println( "Usage: <filename> must have .mjava or .java extension" );
			System.exit( ERROR );
		}

		compileProgram();

	}
}