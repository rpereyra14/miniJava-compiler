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
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;

public class Parser {

	private Scanner lexicalAnalyzer;						//scans tokens
	private ErrorReporter errorReporter;					//prints parse error messages; printing disabled in PA2
	private Token currentToken;								//the current token
	private SourcePosition previousTokenPosition;			//the position of the previous token

	public Parser(Scanner lexer, ErrorReporter reporter) {
		lexicalAnalyzer = lexer;
		errorReporter = reporter;
		previousTokenPosition = new SourcePosition();
	}

	// accept checks whether the current token matches tokenExpected.
	// If so, fetches the next token.
	// If not, reports a syntactic error.

	private void accept (int tokenExpected) throws SyntaxError {
		if (currentToken.kind == tokenExpected) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyzer.scan();
		} else {
			syntacticError("\"%\" expected here", Token.spell(tokenExpected));
		}
	}

	//acceptIt fetches the next token

	private void acceptIt() {
		previousTokenPosition = currentToken.position;
		currentToken = lexicalAnalyzer.scan();
	}

	// start records the position of the start of a phrase.
	// This is defined to be the position of the first
	// character of the first token of the phrase.

	private void start(SourcePosition position) {
		position.start = currentToken.position.start;
	}

	// finish records the position of the end of a phrase.
	// This is defined to be the position of the last
	// character of the last token of the phrase.

	private void finish(SourcePosition position) {
		position.finish = previousTokenPosition.finish;
	}

	//syntacticError generates an error report for the errorReporter

	private void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
		SourcePosition pos = currentToken.position;
		errorReporter.reportSyntaxError(messageTemplate, tokenQuoted, pos);
		throw(new SyntaxError());
	}

	private void syntacticError(String messageTemplate, String tokenQuoted, SourcePosition pos) throws SyntaxError {
		errorReporter.reportSyntaxError(messageTemplate, tokenQuoted, pos);
		throw(new SyntaxError());
	}

	//parsePackage initiates parsing of the miniJava program.

	public miniJava.AbstractSyntaxTrees.Package parsePackage() {
		
		miniJava.AbstractSyntaxTrees.Package packageAST = null;

		currentToken = lexicalAnalyzer.scan();					//get the first token

		SourcePosition packagePos = new SourcePosition();		//start recording of the package position
		start( packagePos );

		try{

			ClassDeclList classList = new ClassDeclList();		//initialize the list of class declarations

			//parse classes
			while( currentToken.kind == Token.CLASS ){
				acceptIt();
				ClassDecl classDecl = parseClass();
				classList.add( classDecl );
			}

			//record end of package position
			finish( packagePos );

			//build AST
			packageAST = new miniJava.AbstractSyntaxTrees.Package( classList, packagePos );

			//ensure no tokens are found outside a class declaration
			if( currentToken.kind != Token.EOT ){
				syntacticError("\"%\" not expected after end of program", currentToken.spelling);
			}

		}catch( SyntaxError s ){ return null; }

		return packageAST;

	}

	//parseClass parses a class in the miniJava package

	private ClassDecl parseClass() throws SyntaxError {

		//record start of class
		SourcePosition classPos = new SourcePosition();
		start( classPos );

		//get classname
		String cn = currentToken.spelling;

		//initialize field and method lists
		MethodDeclList methodList = new MethodDeclList();
		FieldDeclList fieldList = new FieldDeclList();

		accept( Token.IDENTIFIER );			//accept the class name
		accept( Token.LCURLY );				//look for { to start class

		//read through the class fields/methods
		while( currentToken.kind != Token.RCURLY ){

			//record position of class member
			SourcePosition memberPos = new SourcePosition();
			start( memberPos );

			//parse member declaration
			MemberDecl memberDecl = parseMember();

			if( currentToken.kind == Token.SEMICOLON ){			//we have ourselves a field
				acceptIt();
				finish( memberPos );
				fieldList.add( new FieldDecl( memberDecl, memberPos) );
			}else if( currentToken.kind == Token.LPAREN ){		//we have ourselves a method
				acceptIt();
				finish( memberPos );
				memberDecl.type = new MethodType( memberDecl.type, memberPos );
				MethodDecl methodDecl = parseMethod( memberDecl, memberPos );
				methodList.add( methodDecl );
			}else{
				syntacticError("\"%\" not expected after end of member declaration", currentToken.spelling);
			}

		}

		accept( Token.RCURLY );

		finish( classPos );
		return new ClassDecl( cn, fieldList, methodList, classPos );

	}

	//parse a member decl

	private MemberDecl parseMember() throws SyntaxError {

		SourcePosition memberPos = new SourcePosition();
		start( memberPos );

		boolean isPrivate = false;
		boolean isStatic = false;

		switch ( currentToken.kind ){
			case Token.PUBLIC:
				acceptIt();
				if( currentToken.kind == Token.STATIC ){
					acceptIt();
					isStatic = true;
				}
				break;
			case Token.PRIVATE:
				acceptIt();
				isPrivate = true;
				if( currentToken.kind == Token.STATIC ){
					acceptIt();
					isStatic = true;
				}
				break;
			case Token.STATIC:
				acceptIt();
				isStatic = true;
				break;
		}

		Type type = parseType();

		String name = currentToken.spelling;
		accept( Token.IDENTIFIER );

		finish( memberPos );
		return new FieldDecl( isPrivate, isStatic, type, name, memberPos );

	}

	//parseMethod parses a method within a class

	private MethodDecl parseMethod( MemberDecl memberDecl, SourcePosition memberPos ) throws SyntaxError {

		ParameterDeclList pl = parseParameterList();
		accept( Token.RPAREN );
		accept( Token.LCURLY );
		StatementList sl = parseStatementList();	//the method's statements
		Expression e = parseReturn();				//get the return expression

		return new MethodDecl( memberDecl, pl, sl, e, memberPos );

	}

	//parse the method's parameter list

	private ParameterDeclList parseParameterList() throws SyntaxError {
		ParameterDeclList parameters = new ParameterDeclList();
		if( currentToken.kind != Token.RPAREN ){
			ParameterDecl param = parseParameter();
			parameters.add( param );
			while( currentToken.kind != Token.RPAREN ){
				accept( Token.COMMA );
				param = parseParameter();
				parameters.add( param );
			}
		}
		return parameters;
	}

	//parse a single parameter

	private ParameterDecl parseParameter() throws SyntaxError {

		SourcePosition paramPos = new SourcePosition();
		start( paramPos );

		Type type = parseType();				//get the parameter's type
		String name = currentToken.spelling;	//the variable name of the parameter
		accept( Token.IDENTIFIER );

		finish( paramPos );
		return new ParameterDecl( type, name, paramPos );

	}
	//parse a method's statement list

	private StatementList parseStatementList() throws SyntaxError{
		StatementList statements = new StatementList();
		while( currentToken.kind != Token.RETURN && currentToken.kind != Token.RCURLY ){
			Statement stmt = parseStatement();
			statements.add( stmt );
		}
		return statements;
	}

	//parse the return expression

	private Expression parseReturn() throws SyntaxError {
		Expression e = null;
		if( currentToken.kind == Token.RETURN ){
			acceptIt();
			e = parseExpression();
			accept( Token.SEMICOLON );
			accept( Token.RCURLY );
		}else{
			accept( Token.RCURLY );
		}
		return e;
	}

	//parse a single statement

	private Statement parseStatement() throws SyntaxError {
		Statement stmt = null;
		SourcePosition stmtPos = new SourcePosition();
		start( stmtPos );
		switch( currentToken.kind ){
			case Token.LCURLY:				//Statement := { Statement }
				acceptIt();
				StatementList list = new StatementList();
				while( currentToken.kind != Token.RCURLY ){
					Statement sub_stmt = parseStatement();
					list.add( sub_stmt );
				}
				accept( Token.RCURLY );
				finish( stmtPos );
				stmt = new BlockStmt( list, stmtPos );
				break;
			case Token.BOOLEAN:	case Token.VOID: case Token.INT:		//Statement := PrimType id = Expression;
				SourcePosition varPos = new SourcePosition();
				start( varPos );
				Type type = parseType();
				String name = currentToken.spelling;
				accept( Token.IDENTIFIER );
				finish( varPos );
				VarDecl vd = new VarDecl( type, name, varPos );
				accept( Token.EQUALS );
				Expression e = parseExpression();
				accept( Token.SEMICOLON );
				finish( stmtPos );
				stmt = new VarDeclStmt( vd, e, stmtPos );
				break;
			case Token.IDENTIFIER:							//Statement := id ... (either a VarDecl or some Reference Statment)
				String fID = currentToken.spelling;
				SourcePosition fIDPos = new SourcePosition();
				start( fIDPos );
				acceptIt();
				finish( fIDPos );
				switch( currentToken.kind ){
					case Token.EQUALS:					//Reference = Expression; where Reference is id
						Identifier id = new Identifier( fID, fIDPos );
						Reference ref = new QualifiedRef( id );
						acceptIt();
						e = parseExpression();
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new AssignStmt( ref, e, stmtPos );
						break;
					case Token.IDENTIFIER:				//Type id = Expression; where type is id
						SourcePosition varDeclPos = fIDPos;
						type = new ClassType( fID, fIDPos );
						name = currentToken.spelling;
						acceptIt();
						finish( varDeclPos );
						vd = new VarDecl( type, name, varDeclPos );
						accept( Token.EQUALS );
						e = parseExpression();
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new VarDeclStmt( vd, e, stmtPos );
						break;
					case Token.LSQUARE:					//Type id = Expression; where type is id[] or Reference[Expression] = Expression; where Reference is id
						acceptIt();
						if( currentToken.kind == Token.RSQUARE ){			//Type id = Expression; where type is id[]
							acceptIt();
							SourcePosition arrPos = fIDPos;
							SourcePosition vdPos = fIDPos;
							finish( arrPos );
							type = new ArrayType( new ClassType( fID, fIDPos ), arrPos );
							name = currentToken.spelling;
							accept( Token.IDENTIFIER );
							finish( vdPos );
							vd = new VarDecl( type, name, vdPos );
							accept( Token.EQUALS );
							e = parseExpression();
							accept( Token.SEMICOLON );
							finish( stmtPos );
							stmt = new VarDeclStmt( vd, e, stmtPos );
						}else{												//Reference[Expression] = Expression; where Reference is id
							SourcePosition refPos = fIDPos;
							id = new Identifier( fID, fIDPos );
							ref = new QualifiedRef( id );
							Expression e_index = parseExpression();
							accept( Token.RSQUARE );
							finish( refPos );
							IndexedRef i_ref = new IndexedRef( ref, e_index, refPos );
							accept( Token.EQUALS );
							e = parseExpression();
							accept( Token.SEMICOLON );
							finish( stmtPos );
							stmt = new AssignStmt( i_ref, e, stmtPos );
						}
						break;
					case Token.LPAREN:										//Reference\( ArgumentList? \); where Reference is id
						id = new Identifier( fID, fIDPos );
						ref = new QualifiedRef( id );
						acceptIt();
						ExprList elist = parseArguments();
						accept( Token.RPAREN );
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new CallStmt( ref, elist, stmtPos );
						break;

					//Reference\( ArgumentList? \); or Reference([Expression])? = Expression; where Reference is id(.id)+
					case Token.DOT:
						ref = parseReference( fID, previousTokenPosition, fIDPos );
						switch( currentToken.kind ){
							case Token.EQUALS:					//Statement := id(.id)+ = Expression;
								acceptIt();
								e = parseExpression();
								accept( Token.SEMICOLON );
								finish( stmtPos );
								stmt = new AssignStmt( ref, e, stmtPos );
								break;
							case Token.LSQUARE:					//Statement := id(.id)+[Expression] = Expression;
								SourcePosition refPos = fIDPos;
								acceptIt();
								Expression e_index = parseExpression();
								accept( Token.RSQUARE );
								finish( refPos );
								IndexedRef i_ref = new IndexedRef( ref, e_index, refPos );
								accept( Token.EQUALS );
								e = parseExpression();
								accept( Token.SEMICOLON );
								finish( stmtPos );
								stmt = new AssignStmt( i_ref, e, stmtPos );
								break;
							case Token.LPAREN:					//Statement := id(.id)+\(Arguments?\) = Expression;
								acceptIt();
								elist = parseArguments();
								accept( Token.RPAREN );
								accept( Token.SEMICOLON );
								finish( stmtPos );
								stmt = new CallStmt( ref, elist, stmtPos ); 
								break;
							default:
								syntacticError("\"%\" not expected after reference", currentToken.spelling);
								break;
						}
						break;
					default:
						syntacticError("\"%\" not expected after identifier", currentToken.spelling);
						break;
				}
				break;

			//Statement := some refence beginning with this
			case Token.THIS:
				SourcePosition thisPos = new SourcePosition();
				start( thisPos );
				Reference ref = parseReference( currentToken.spelling, currentToken.position, thisPos );
				switch( currentToken.kind ){
					case Token.EQUALS:					//Statement := this(.id)+ = Expression;
						acceptIt();
						e = parseExpression();
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new AssignStmt( ref, e, stmtPos );
						break;
					case Token.LSQUARE:					//Statement := this(.id)+[Expression] = Expression;
						SourcePosition refPos = thisPos;
						acceptIt();
						Expression e_index = parseExpression();
						accept( Token.RSQUARE );
						finish( refPos );
						IndexedRef i_ref = new IndexedRef( ref, e_index, refPos );
						accept( Token.EQUALS );
						e = parseExpression();
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new AssignStmt( i_ref, e, stmtPos );
						break;
					case Token.LPAREN:					//Statement := this(.id)+\(Arguments?\) = Expression;
						acceptIt();
						ExprList elist = parseArguments();
						accept( Token.RPAREN );
						accept( Token.SEMICOLON );
						finish( stmtPos );
						stmt = new CallStmt( ref, elist, stmtPos ); 
						break;
					default:
						syntacticError("\"%\" not expected after reference", currentToken.spelling);
						break;
				}
				break;
			case Token.IF:			//parse if statement
				acceptIt();
				accept( Token.LPAREN );
				e = parseExpression();
				accept( Token.RPAREN );
				Statement t = parseStatement();
				Statement elseStmt = null;
				if( currentToken.kind == Token.ELSE ){
					acceptIt();
					elseStmt = parseStatement();
				}
				finish( stmtPos );
				stmt = new IfStmt( e, t, elseStmt, stmtPos );
				break;
			case Token.WHILE:		//parse while statement
				acceptIt();
				accept( Token.LPAREN );
				e = parseExpression();
				accept( Token.RPAREN );
				Statement s = parseStatement();
				finish( stmtPos );
				stmt = new WhileStmt( e, s, stmtPos );
				break;
			case Token.FOR:		//parse for statement
				acceptIt();
				accept( Token.LPAREN );
				SourcePosition curr = currentToken.position;
				Statement i = parseStatement();
				if( !(i instanceof VarDeclStmt) && !(i instanceof CallStmt) && !(i instanceof AssignStmt) ){
					syntacticError("illegal start of expression", currentToken.spelling, curr);
				}
				e = parseExpression();
				accept( Token.SEMICOLON );
				Expression u_1 = parseExpression();
				if( !(u_1 instanceof RefExpr) ){
					syntacticError("illegal start of expression", currentToken.spelling, u_1.posn);
				}
				accept( Token.EQUALS );
				Expression u_2 = parseExpression();
				SourcePosition p = new SourcePosition( u_1.posn.start, u_2.posn.finish );
				Statement u = new AssignStmt( ((RefExpr)u_1).ref, u_2, p );
				accept( Token.RPAREN );
				s = parseStatement();
				finish( stmtPos );
				stmt = new ForStmt( i, e, u, s, stmtPos );
				break;
			default:
				syntacticError("\"%\" not expected at beginning of statement", currentToken.spelling);
				break;
		}
		return stmt;
	}

	//parse the arguments of a reference

	private ExprList parseArguments() throws SyntaxError {
		ExprList elist = new ExprList();
		if( currentToken.kind != Token.RPAREN ){
			Expression e = parseExpression();
			elist.add( e );
			while( currentToken.kind == Token.COMMA ){
				acceptIt();
				e = parseExpression();
				elist.add( e );
			}
		}
		return elist;
	}

	//parse a reference
	//init and initPos are passed because if the reference does not begin with this, a single identifier could be part of a type decl
	//			which means the parser must read the next token before resolving what statement ist should parse
	//			so init and initPos contain the name and position of the first id in the reference sequence.
	private Reference parseReference( String init, SourcePosition initPos, SourcePosition pos ) throws SyntaxError {
		boolean thisRelative = false;
		IdentifierList ilist = new IdentifierList();
		if( currentToken.kind == Token.THIS ){
			acceptIt();
			thisRelative = true;
		}else if( currentToken.kind == Token.NULL ){
			acceptIt();
			finish(pos);
			return new NullRef( pos );
		}else{
			Identifier id = new Identifier( init, initPos );
			ilist.add( id );
		}
		while( currentToken.kind == Token.DOT ){
			acceptIt();
			SourcePosition idPos = new SourcePosition();
			start( idPos );
			String name = currentToken.spelling;
			accept( Token.IDENTIFIER );
			finish( idPos );
			Identifier id = new Identifier( name, idPos );
			ilist.add( id );
		}
		finish( pos );
		return new QualifiedRef( thisRelative, ilist, pos );
	}

	//stub method for ease of expression parsing
	//the following sequence of methods represent the stratified grammar generated as part of PA2
	private Expression parseExpression() throws SyntaxError {
		return parseDisjunctionExpression();
	}

	//parse a disjunction expression
	private Expression parseDisjunctionExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseConjunctionExpression();
		while( currentToken.spelling.compareTo( "||" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseConjunctionExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//parse a conjunction expression
	private Expression parseConjunctionExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseEqualityExpression();
		while( currentToken.spelling.compareTo( "&&" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseEqualityExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//parse an equality expression
	private Expression parseEqualityExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseRelationalExpression();
		while( currentToken.spelling.compareTo( "==" ) == 0 || currentToken.spelling.compareTo( "!=" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseRelationalExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//parse a relational expression
	private Expression parseRelationalExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseAdditiveExpression();
		while( currentToken.spelling.compareTo( "<=" ) == 0 || currentToken.spelling.compareTo( ">=" ) == 0
			|| currentToken.spelling.compareTo( "<" ) == 0 || currentToken.spelling.compareTo( ">" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseAdditiveExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//parse an additive expression
	private Expression parseAdditiveExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseMultiplicativeExpression();
		while( currentToken.spelling.compareTo( "+" ) == 0 || currentToken.spelling.compareTo( "-" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseMultiplicativeExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//parse a multiplicative expression
	private Expression parseMultiplicativeExpression() throws SyntaxError {
		SourcePosition ePos = new SourcePosition();
		start( ePos );
		Expression e1 = parseHighestPrecedenceExpression();
		while( currentToken.spelling.compareTo( "*" ) == 0 || currentToken.spelling.compareTo( "/" ) == 0 ){
			Operator op = new Operator( currentToken.spelling, currentToken.position );
			acceptIt();
			Expression e2 = parseHighestPrecedenceExpression();
			finish( ePos );
			e1 = new BinaryExpr( op, e1, e2, ePos );
		}
		return e1;
	}

	//expression as defined in PA1 (minus the Expression binop Expression definition) have the highest precende
	//			and should be parsed at the bottom of the cascading expression parsing methods
	private Expression parseHighestPrecedenceExpression() throws SyntaxError {
		Expression expr = null;
		SourcePosition pos = new SourcePosition();
		start( pos );
		switch( currentToken.kind ){
			case Token.IDENTIFIER: case Token.THIS: case Token.NULL:				//Expression := something starting with a reference
				String nm = currentToken.spelling;

				//in the case of identifiers, the parseReference method expects the parser to have skipped the first identifier because 
				//		in the case of Statements a statement could start with an identifier
				//		and be a var decl so parser "skips" the first id of a reference
				if( currentToken.kind == Token.IDENTIFIER ){
					acceptIt();
				}

				Reference ref = parseReference( nm, previousTokenPosition ,pos );

				switch( currentToken.kind ){
					case Token.LSQUARE:						//Reference Expression where reference is indexed
						acceptIt();
						Expression e = parseExpression();
						accept( Token.RSQUARE );
						finish( pos );
						IndexedRef i_ref = new IndexedRef( ref, e, pos );
						expr = new RefExpr( i_ref, pos );
						break;
					case Token.LPAREN:						//call expression
						acceptIt();
						ExprList elist = parseArguments();
						accept( Token.RPAREN );
						finish( pos );
						expr = new CallExpr( ref, elist, pos );
						break;
					default:								//reference expression where reference is not indexed
						finish( pos );
						expr = new RefExpr( ref, pos );
						break;
				}
				break;
			case Token.OPERATOR:		//handle unary operators
				if( currentToken.spelling.compareTo("!") != 0 && currentToken.spelling.compareTo("-") != 0 ){
					syntacticError("unary operator exprected here", "");
				}
				Operator op = new Operator( currentToken.spelling, currentToken.position );
				acceptIt();
				Expression e = parseHighestPrecedenceExpression();
				finish( pos );
				expr = new UnaryExpr( op, e, pos );
				break;
			case Token.LPAREN:			//Expression := ( Expression )
				acceptIt();
				e = parseExpression();
				accept( Token.RPAREN );
				expr = e;
				break;
			case Token.INTLITERAL:		//IntLiteral Expression
				Literal lt = new IntLiteral( currentToken.spelling, currentToken.position );
				acceptIt();
				finish( pos );
				expr = new LiteralExpr( lt, pos );
				break;
			case Token.TRUE: case Token.FALSE:	//BooleanLiteral Expression
				lt = new BooleanLiteral( currentToken.spelling, currentToken.position );
				acceptIt();
				finish( pos );
				expr = new LiteralExpr( lt, pos );
				break;
			case Token.NEW:				//Expression := new ...
				acceptIt();
				switch( currentToken.kind ){
					case Token.IDENTIFIER:
						String cn = currentToken.spelling;
						SourcePosition cnPos = currentToken.position;
						acceptIt();
						switch( currentToken.kind ){
							case Token.LPAREN:				//new object expression
								acceptIt();
								accept( Token.RPAREN );
								finish( pos );
								ClassType ct = new ClassType( cn, cnPos );
								expr = new NewObjectExpr( ct, pos );
								break;
							case Token.LSQUARE:				//new array expression where type is id
								acceptIt();
								e = parseExpression();
								accept( Token.RSQUARE );
								finish( pos );
								ct = new ClassType( cn, cnPos );
								expr = new NewArrayExpr( ct, e, pos );
								break;
							default:
								syntacticError("\"%\" not expected after new <identifier>", currentToken.spelling);
								break;
						}
						break;
					case Token.INT:				//new int array expression
						BaseType bt = new BaseType( TypeKind.INT, currentToken.position );
						acceptIt();
						accept( Token.LSQUARE );
						e = parseExpression();
						accept( Token.RSQUARE );
						finish( pos );
						expr = new NewArrayExpr( bt, e, pos );
						break;
				}
				break;
			default:
			syntacticError("\"%\" not expected at the start of an expression", currentToken.spelling);
			break;
		}
		return expr;
	}

	//parse a type in a var decl

	private Type parseType() throws SyntaxError {
		
		SourcePosition typePos = new SourcePosition();
		start( typePos );

		Type type = null;

		switch( currentToken.kind ){
			case Token.VOID:
				acceptIt();
				finish( typePos );
				type = new BaseType( TypeKind.VOID, typePos );
				break;
			case Token.BOOLEAN:
				acceptIt();
				finish( typePos );
				type = new BaseType( TypeKind.BOOLEAN, typePos );
				break;
			case Token.INT:
				acceptIt();
				if( currentToken.kind == Token.LSQUARE ){
					acceptIt();
					accept( Token.RSQUARE );
					finish( typePos );
					type = new ArrayType( new BaseType( TypeKind.INT, previousTokenPosition), typePos );
				}else{
					finish( typePos );
					type = new BaseType( TypeKind.INT, typePos );
				}
				break;
			case Token.IDENTIFIER:
				String cn = currentToken.spelling;
				acceptIt();
				if( currentToken.kind == Token.LSQUARE ){
					acceptIt();
					accept( Token.RSQUARE );
					finish( typePos );
					type = new ArrayType( new ClassType(cn, previousTokenPosition), typePos );
				}else{
					finish( typePos );
					type = new ClassType(cn, typePos);
				}
				break;
			default:
				finish( typePos );
				syntacticError("type declaration expected here", "");
		}

		return type;

	}

}