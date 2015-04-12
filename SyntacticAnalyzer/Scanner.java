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

import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.SourceFile;

/*Class in charge of making sense of the input stream from the source file.*/
/*Chars are read and once whitespace is found the read sequence is analyzed to determine the token type.*/
public final class Scanner {

	private SourceFile sourceFile;				/*The source file.*/
	private char currentChar;						/*The current character read from the source file.*/
	private int tokenType;							/*The determined token type for a non-whitespace sequence of characters.*/
	private StringBuffer currentToken;			/*The current token.*/
	private boolean scanningComment = false;	/*Falg set if a comment is being scanned.*/

	/*Constructor. Set the source file and get the first character from the stream.*/
	public Scanner ( SourceFile source ) {
		this.sourceFile = source;
		this.currentChar = sourceFile.getChar();
	}

	/*Append the current character to the current token and get the next character.*/
	private void appendChar () {
		currentToken.append( currentChar );
		currentChar = sourceFile.getChar();
	}

	/*Only get the next character (i.e. do not append the current char to current token).*/
	private void nextChar () {
		currentChar = sourceFile.getChar();
	}

	/*Scan a multiline comment.*/
	private void scanComment () {
		boolean searching = true;
		while( searching ){
			if( currentChar == '*' ){
				nextChar();
				if( currentChar == '/'){
					searching = false;
					scanningComment = false;
					break;
				}
			}else if( currentChar == SourceFile.eot ){
				searching = false;
			}else{
				nextChar();
			}
		}
		nextChar();			/*Note that next char will be EOT if the EOF of the source file was previously reached.*/
	}

	/*Scan a single line comment.*/
	private void scanLineComment () {
		boolean searching = true;
		while( searching ){
			nextChar();
			if( currentChar == '\n' || currentChar == '\r' || currentChar == SourceFile.eot ){
				searching = false;
				scanningComment = false;
			}
		}
		nextChar();			/*Note that next char will be EOT if the EOF of the source file was previously reached.*/
	}

	/*Scan a token by comparing the input characters to pre-established token specifiers. Return the token ID.*/
	private int scanToken() {

		if( charIsLetter() ){						/*An indentifier may contain letters or numbers but the first char must be a letter.*/
			appendChar();							/*Reserved keywords will be caught by this option. Their type will later be corrected.*/
			while( charIsLetter() || charIsDigit() || currentChar == '_' ){
				appendChar();
			}
			return Token.IDENTIFIER;
		}else if( charIsDigit() ){					/*An integer literal may only contain numbers.*/
			appendChar();
			while( charIsDigit() ){
				appendChar();
			}
			return Token.INTLITERAL;
		}else if( charIsOperator() ){				/*An operator sequence expected.*/
			appendChar();
			if( charIsOperator() && validSecondOperator() ){
				appendChar();
			}
			if( operatorSeqValid() ){
				return Token.OPERATOR;
			}else if( singleEquals() ){
				return Token.EQUALS;
			}else{
				return Token.ERROR;
			}
		}

		switch ( currentChar ) {					/*Compare single character tokens to known terminals in miniJava.*/

			case '.':
				appendChar();
				return Token.DOT;

			case ';':
				appendChar();
				return Token.SEMICOLON;

			case ',':
				appendChar();
				return Token.COMMA;

			case '(':
				appendChar();
				return Token.LPAREN;

			case ')':
				appendChar();
				return Token.RPAREN;

			case '[':
				appendChar();
				return Token.LSQUARE;

			case ']':
				appendChar();
				return Token.RSQUARE;

			case '{':
				appendChar();
				return Token.LCURLY;

			case '}':
				appendChar();
				return Token.RCURLY;

			case SourceFile.eot:
				return Token.EOT;

			default:
				appendChar();
				return Token.ERROR;
		}
	}

	/*Changes token type if the token spelling is determined to match a reserved keyword.*/
	/*Called only if a token was initially thought to be an identifier.*/
	private void changeTypeIfKeyword () {

		int keywordID = Token.getKeywordID( currentToken.toString() );
		if( keywordID != Token.NO_TOKEN ){
			tokenType = keywordID;
		}

	}

	/*Returns a scanned token to the parser.*/
	public Token scan () {

		Token token;
		SourcePosition pos = new SourcePosition();

		/*Skip whitespace characters.*/
		while( currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t' ){
			nextChar();
		}

		/*Initilize new token and get the token's starting positions.*/
		currentToken = new StringBuffer("");
		pos.start = sourceFile.getCurrentLine();

		/*Scan the token.*/
		tokenType = scanToken();

		pos.finish = sourceFile.getCurrentLine();

		/*Check if the token indicated the beginning of a comment.*/
		/*If so, scan is called again to get first token after comment.*/
		if( currentToken.toString().compareTo("//") == 0 ){
			scanningComment = true;
			scanLineComment();
			if( scanningComment ){
				pos.finish = sourceFile.getCurrentLine();
				return new Token( tokenType, Token.spell( tokenType ), pos );
			}else{
				return this.scan();
			}
		}else if( currentToken.toString().compareTo("/*") == 0 ){
			scanningComment = true;
			scanComment();
			if( scanningComment ){
				pos.finish = sourceFile.getCurrentLine();
				return new Token( tokenType, Token.spell( tokenType ), pos );
			}else{
				return this.scan();
			}
		}

		/*Correct mislabeled reserved keywords.*/
		if( tokenType == Token.IDENTIFIER ){
			changeTypeIfKeyword();
		}

		token = new Token( tokenType, currentToken.toString(), pos );
		return token;

	}

	/*Check if letter.*/
	private boolean charIsLetter () {
		return (( currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z' ));
	}

	/*Check if number.*/
	private boolean charIsDigit () {
		return ( currentChar >= '0' && currentChar <= '9' );
	}

	/*Check if operator.*/
	private boolean charIsOperator () {
		return ( currentChar == '>' || currentChar == '<' || currentChar == '=' || currentChar == '!' ||
			currentChar == '&' || currentChar == '|' || currentChar == '+' || currentChar == '-' ||
			currentChar == '*' || currentChar == '/' );
	}

	/*Check if a single equals sign was found.*/
	private boolean singleEquals () {
		String spelling = currentToken.toString();
		if( spelling.compareTo("=") == 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	/*Check the operator sequence obtained.*/
	private boolean operatorSeqValid () {
		String spelling = currentToken.toString();
		if( spelling.compareTo(">") != 0 && spelling.compareTo("<") != 0 && spelling.compareTo("==") != 0 && 
			spelling.compareTo("<=") != 0 && spelling.compareTo(">=") != 0 && spelling.compareTo("!=") != 0 && 
			spelling.compareTo("&&") != 0 && spelling.compareTo("||") != 0 && spelling.compareTo("!") != 0 &&
			spelling.compareTo("+") != 0 && spelling.compareTo("-") != 0 && spelling.compareTo("*") != 0 &&
			spelling.compareTo("/") != 0 ){
			return false;
		}
		return true;
	}
	
	private boolean validSecondOperator(){
		String spelling = currentToken.toString();
		if( (spelling.compareTo(">") == 0 || spelling.compareTo("<") == 0 || spelling.compareTo("=") == 0 
			|| spelling.compareTo("!") == 0) && currentChar == '=' ){
			return true;
		}else if( spelling.compareTo("&") == 0 && currentChar == '&' ){
			return true;
		}else if( spelling.compareTo("|") == 0 && currentChar == '|' ){
			return true;
		}else if( spelling.compareTo("/") == 0 && ( currentChar == '/' || currentChar == '*' ) ){
			return true;
		}else if( spelling.compareTo("-") == 0 && currentChar == '-' ){
			return true;
		}
		return false;
	}

}