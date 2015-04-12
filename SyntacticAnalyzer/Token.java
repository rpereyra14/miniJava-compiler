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

/*
 * Wrapper class for tokens obtained from the source file.
 */
final class Token extends Object {

	protected int kind;							/*The Token kind selected from list below.*/
	protected SourcePosition position;			/*The line where the token was found.*/
	protected String spelling;					/*The actual character sequence of the Token.*/

	public static final int NO_TOKEN = -1;	/*Return value when a possible identifier is not determined to be a keyword.*/

	/*Constructor.*/
	public Token ( int kind, String spelling, SourcePosition pos ) {
		this.kind = kind;
		this.spelling = spelling;
		this.position = pos;
	}

	/*Attempts to match String s to a known reserved keyword.*/
	/*If successful, returns the index of the keyword in the tokenTable.*/
	/*Else, retuns the NO_TOKEN flag.*/
	public static int getKeywordID ( String s ){
		int i = firstKeywordID;
		int comparison;
		while( i <= lastKeywordID ){
			comparison = tokenTable[i].compareTo( s );
			if( comparison == 0 ){
				return i;
			/*Reserved keywords in tokenTable AND the Token kinds MUST be listed in alphabetical order.*/
			}else if( comparison > 0 ){
				break;
			}
			i++;
		}
		return NO_TOKEN;
	}

	public static String spell( int tokenKind ){
		return tokenTable[ tokenKind ];
	}

	/*Scanner will utilize this array to differentiate between identifiers and reserved keywords.*/
	/*LIST OF RESERVED KEYWORDS MUST BE IN ALPHABETICAL ORDER.*/
	private static final String[] tokenTable = new String[] {
		"<int>",
		"<identifier>",
		"<operator>",
    	"boolean",
		"class",
		"else",
		"false",
		"for",
		"if",
		"int",
		"new",
		"null",
		"private",
		"public",
		"return",
		"static",
		"this",
		"true",
		"void",
		"while",
		"=",
		".",
		";",
		",",
		"{",
		"}",
		"(",
		")",
		"[",
		"]",
		"",
		"<error>"
	};

	/*Used to identify what Token kind IDs pertain to reserved keywords.*/
	private static final int firstKeywordID = Token.BOOLEAN;
	private static final int lastKeywordID = Token.WHILE;

	/*Allowed token kinds.*/
	public static final int

		/*Literals, identifiers, and operators:*/
		INTLITERAL		= 0,
		IDENTIFIER		= 1,
		OPERATOR		= 2,

		/*Reserved keywords (LIST MUST BE IN ALPHABETICAL ORDER):*/
		BOOLEAN			= 3,
		CLASS			= 4,
		ELSE			= 5,
		FALSE			= 6,
		FOR 			= 7,
		IF				= 8,
		INT				= 9,
		NEW				= 10,
		NULL 			= 11,
		PRIVATE			= 12,
		PUBLIC			= 13,
		RETURN			= 14,
		STATIC			= 15,
		THIS			= 16,
		TRUE			= 17,
		VOID			= 18,
		WHILE			= 19,

		/*Punctuation:*/
		EQUALS			= 20,
		DOT				= 21,
		SEMICOLON		= 22,
		COMMA			= 23,

		/*Brackets and parenthesis:*/
		LCURLY			= 24,
		RCURLY			= 25,
		LPAREN			= 26,
		RPAREN			= 27,
		LSQUARE			= 28,
		RSQUARE			= 29,

		/*Special Tokens:*/
		EOT				= 30,
		ERROR			= 31;

}