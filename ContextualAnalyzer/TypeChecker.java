/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.ErrorReporter;

public final class TypeChecker implements Visitor<Object,TypeDenoter>{

	public ErrorReporter reporter;
	private String currentClass;

	public TypeChecker( ErrorReporter reporter ){
		this.reporter = reporter;
		currentClass = new String();
	}

	public void checkTypes( AST ast ){
		ast.visit( this, null );
	}

	private boolean testAndSet( TypeDenoter t1, TypeDenoter t2, SourcePosition posn ){

		if( t1.typeName.equals( "String" ) )
			t1 = new TypeDenoter( TypeKind.UNSUPPORTED );
		if( t2.typeName.equals( "String" ) )
			t2 = new TypeDenoter( TypeKind.UNSUPPORTED );

		if( t1.typeKind == TypeKind.ERROR || t2.typeKind == TypeKind.ERROR ){

			t1 = new TypeDenoter( TypeKind.ERROR );
			t2 = new TypeDenoter( TypeKind.ERROR );
			return true;

		}else if( t1.typeKind == TypeKind.UNSUPPORTED || t2.typeKind == TypeKind.UNSUPPORTED ){

			reporter.reportTypeError( "***unsupported type : line " + posn.start );
			t1 = new TypeDenoter( TypeKind.ERROR );
			t2 = new TypeDenoter( TypeKind.ERROR );
			return true;

		}else if( (t1.typeKind == TypeKind.CLASS && t2.typeKind == TypeKind.MISCELLANEOUS) 
				|| (t1.typeKind == TypeKind.MISCELLANEOUS && t2.typeKind == TypeKind.CLASS) ){
			return true;
		}else{
			return t1.typeName.equals(t2.typeName) && t1.typeKind == t2.typeKind;
		}

	}

  // Package
	public TypeDenoter visitPackage(Package prog, Object o){

		for( ClassDecl c: prog.classDeclList ){
			currentClass = c.name;
			c.visit( this, null );
		}

		return null;

	}

  // Declarations
	public TypeDenoter visitClassDecl(ClassDecl cd, Object o){

		for( FieldDecl f: cd.fieldDeclList )
			f.visit( this, null );

		for( MethodDecl m: cd.methodDeclList )
			m.visit( this, null );
		return null;

	}

	public TypeDenoter visitFieldDecl(FieldDecl fd, Object o){
		if( fd.type.typeKind == TypeKind.VOID ){
			reporter.reportTypeError( "***cannot have a field of type void: line " + fd.posn.start );
		}
		return null;
	}

	public TypeDenoter visitMethodDecl(MethodDecl md, Object o){

		for( ParameterDecl p: md.parameterDeclList ){
			if( p.type.typeKind == TypeKind.VOID )
				reporter.reportTypeError( "***method parameter cannot be of type void: line " + md.posn.start );
		}

		for( Statement s: md.statementList )
			s.visit( this, null ); 

		TypeDenoter mdDenoter = new TypeDenoter(((MethodType)md.type).eltType);

		if( mdDenoter.typeKind == TypeKind.VOID && md.returnExp != null ){
			reporter.reportTypeError( "***void method cannot have a return expression: line " + md.returnExp.posn.start );
			return null;
		}

		if( md.returnExp != null ){
			String errorMsg = "***incorrect return type, expected " + mdDenoter.typeName + " : line " + md.returnExp.posn.start;
			TypeDenoter returnType = md.returnExp.visit( this, null );
			if( !testAndSet( returnType, mdDenoter, md.posn ) )
				reporter.reportTypeError( errorMsg );
		}else if( mdDenoter.typeKind != TypeKind.VOID ){
			String errorMsg = "***missing return expression of type " + mdDenoter.typeName + " : line " + md.posn.finish;
			reporter.reportTypeError( errorMsg );
		}

		return null;

	}

	public TypeDenoter visitParameterDecl(ParameterDecl pd, Object o){return null;}
	public TypeDenoter visitVarDecl(VarDecl decl, Object o){
		return new TypeDenoter( decl.type );
	}

  // Types
	public TypeDenoter visitBaseType(BaseType type, Object o){return null;}
	public TypeDenoter visitClassType(ClassType type, Object o){return null;}
	public TypeDenoter visitArrayType(ArrayType type, Object o){return null;}
	public TypeDenoter visitMethodType(MethodType type, Object o){return null;}

	
  // Statements
	public TypeDenoter visitBlockStmt(BlockStmt stmt, Object o){
		for( Statement s: stmt.sl )
			s.visit( this, null );
		return null;
	}

	public TypeDenoter visitVardeclStmt(VarDeclStmt stmt, Object o){
		TypeDenoter t1 = stmt.varDecl.visit( this, null );
		TypeDenoter t2 = stmt.initExp.visit( this, null );
		if( !testAndSet( t1, t2, stmt.posn ) ){
			if( !t1.typeName.equals( t2.typeName ) )
				reporter.reportTypeError( "***mismatched types, \"" + t1.typeName + "\" type expected but found \"" 
													+ t2.typeName + "\" : line " + stmt.posn.start );
			else{
				if( t1.typeKind == TypeKind.ARRAY )
					reporter.reportTypeError( "***array type expected as rvalue: line " + stmt.posn.start );
				else
					reporter.reportTypeError( "***unexpected array type as rvalue : line " + stmt.posn.start );
			}
		}
		return null;
	}

	public TypeDenoter visitAssignStmt(AssignStmt stmt, Object o){
		TypeDenoter t1 = stmt.ref.visit( this, null );
		TypeDenoter t2 = stmt.val.visit( this, null );	

		if( t1.typeKind == TypeKind.METHOD ){
			reporter.reportTypeError( "***cannot reference a method as an lvalue: line " + stmt.posn.start );
			return null;
		}
		if( t2.typeKind == TypeKind.METHOD ){
			reporter.reportTypeError( "***cannot reference a method as an rvalue: line " + stmt.posn.start );
			return null;
		}
		if( !testAndSet( t1, t2, stmt.posn ) ){
			if( !t1.typeName.equals( t2.typeName ) )
				reporter.reportTypeError( "***mismatched types, \"" + t1.typeName + "\" type expected but found \"" 
													+ t2.typeName + "\" : line " + stmt.posn.start );
			else{
				if( t1.typeKind == TypeKind.ARRAY )
					reporter.reportTypeError( "***array type expected as rvalue: line " + stmt.posn.start );
				else
					reporter.reportTypeError( "***unexpected array type as rvalue : line " + stmt.posn.start );
			}
		}
		return null;
	}

	public TypeDenoter visitCallStmt(CallStmt stmt, Object o){

		int i = 0;
		ParameterDeclList plist = new ParameterDeclList();

		if( stmt.methodRef instanceof DeRef )
			plist = ((MethodDecl)((DeRef)stmt.methodRef).right.decl).parameterDeclList;
		else if( stmt.methodRef instanceof MemberRef )
			plist = ((MethodDecl)((MemberRef)stmt.methodRef).qualifier.decl).parameterDeclList;
		else
			reporter.reportTypeError( "***unexpected reference : line " + stmt.posn.start );
			
		if( stmt.argList.size() > plist.size() ){
			reporter.reportTypeError( "***too many arguments : line " + stmt.posn.start );
			return null;
		}else if( stmt.argList.size() < plist.size() ){
			reporter.reportTypeError( "***too few arguments : line " + stmt.posn.start );
			return null;
		}

		for( Expression e: stmt.argList ){

			TypeDenoter t = e.visit( this, null );
			TypeDenoter p = new TypeDenoter( plist.get(i).type );
			int l = i + 1;					//for error printing below
			if( !testAndSet( t, p, e.posn ) )
				reporter.reportTypeError( "***incorrect type for parameter " + l + " : line " + stmt.posn.start );
			i++;

		}

		return null;
	}

	public TypeDenoter visitIfStmt(IfStmt stmt, Object o){
		TypeDenoter t = stmt.cond.visit( this, null );
		if( !testAndSet( t, new TypeDenoter(TypeKind.BOOLEAN), stmt.posn ) )
			reporter.reportTypeError( "***expected an expression of boolean type : line " + stmt.posn.start );
		stmt.thenStmt.visit( this, null );
		if( stmt.elseStmt != null )
			stmt.elseStmt.visit( this, null );
		return null;
	}

	public TypeDenoter visitWhileStmt(WhileStmt stmt, Object o){
		TypeDenoter t = stmt.cond.visit( this, null );
		if( !testAndSet( t, new TypeDenoter(TypeKind.BOOLEAN), stmt.posn ) )
			reporter.reportTypeError( "***expected an expression of boolean type : line " + stmt.posn.start );
		stmt.body.visit( this, null );
		return null;
	}

	public TypeDenoter visitForStmt(ForStmt stmt, Object o){
		stmt.init.visit( this, null );
		TypeDenoter t = stmt.cond.visit( this, null );
		if( !testAndSet( t, new TypeDenoter(TypeKind.BOOLEAN), stmt.posn ) )
			reporter.reportTypeError( "***expected an expression of boolean type : line " + stmt.posn.start );
		stmt.updt.visit( this, null );
		stmt.body.visit( this, null );
		return null;
	}

  // Expressions
	public TypeDenoter visitUnaryExpr(UnaryExpr expr, Object o){

		TypeDenoter t = expr.expr.visit( this, null );
		OperatorDenoter op = new OperatorDenoter( expr.operator );
		String expected = op.right.typeName;

		if( !testAndSet( t, op.right, expr.posn ) ){
			reporter.reportTypeError( "***expected an expression of " + expected + " type: line " + expr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}else{
			return op.returnType;
		}
		
	}

	public TypeDenoter visitBinaryExpr(BinaryExpr expr, Object o){

		TypeDenoter l = expr.left.visit( this, null );
		TypeDenoter r = expr.right.visit( this, null );
		OperatorDenoter op = new OperatorDenoter( expr.operator );
		String expected = op.right.typeName.replaceAll("<|>", "");

		/*

			Miscellaneous type is given to left and right of the == and != comparison operators to differentiate them.

		*/

		if( op.right.typeKind != TypeKind.MISCELLANEOUS ){
			if( !testAndSet( l, op.left, expr.posn ) || !testAndSet( r, op.right, expr.posn ) ){
				reporter.reportTypeError( "***expected an expression of " + expected + " type: line " + expr.posn.start );
				return new TypeDenoter( TypeKind.ERROR );
			}else{
				return op.returnType;
			}
		}else{
			if( !testAndSet( l, r, expr.posn ) ){
				reporter.reportTypeError( "***incomparable types: line " + expr.posn.start );
				return new TypeDenoter( TypeKind.ERROR );
			}else{
				return op.returnType;
			}
		}
	}

	public TypeDenoter visitRefExpr(RefExpr expr, Object o){
		return expr.ref.visit( this, null );
	}

	public TypeDenoter visitCallExpr(CallExpr expr, Object o){

		int i = 0;
		
		MethodDecl mdecl = null;

		//can only call a method
		if( expr.functionRef instanceof DeRef && ((DeRef)expr.functionRef).right.decl instanceof MethodDecl )
			mdecl = (MethodDecl)((DeRef)expr.functionRef).right.decl;
		else if( expr.functionRef instanceof MemberRef && ((MemberRef)expr.functionRef).qualifier.decl instanceof MethodDecl )
			mdecl = (MethodDecl)((MemberRef)expr.functionRef).qualifier.decl;
		else
			reporter.reportTypeError( "***improper reference : line " + expr.posn.start );

		ParameterDeclList plist = mdecl.parameterDeclList;

		//check the types of parameters passed
		for( Expression e: expr.argList ){

			TypeDenoter t = e.visit( this, null );
			TypeDenoter p = new TypeDenoter( plist.get(i).type );
			int l = i + 1;
			if( !testAndSet( t, p, e.posn ) )
				reporter.reportTypeError( "***expected an expression of " + p.typeName + 
					" type for parameter " + l + " : line " + expr.posn.start );
			i++;

		}

		return new TypeDenoter( ((MethodType)mdecl.type).eltType );

	}

	public TypeDenoter visitLiteralExpr(LiteralExpr expr, Object o){
		return expr.literal.visit( this, null );
	}

	public TypeDenoter visitNewObjectExpr(NewObjectExpr expr, Object o){
		return new TypeDenoter( expr.classtype );
	}

	public TypeDenoter visitNewArrayExpr(NewArrayExpr expr, Object o){

		TypeDenoter t = expr.sizeExpr.visit( this, null );

		if( !testAndSet( t, new TypeDenoter( TypeKind.INT ), expr.posn ) ){
			reporter.reportTypeError( "***expected an expression of int type: line " + expr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}

		if( expr.eltType.typeKind == TypeKind.INT ){
			return new TypeDenoter( TypeKind.ARRAY, TypeDenoter.INT_NAME );
		}else{
			return new TypeDenoter( TypeKind.ARRAY, ((ClassType)expr.eltType).className );
		}
	}

	
  // References
	public TypeDenoter visitQualifiedRef(QualifiedRef ref, Object o){return null;}
	public TypeDenoter visitNullRef(NullRef ref, Object o){
		return new TypeDenoter( TypeKind.MISCELLANEOUS );
	}

	public TypeDenoter visitLocalRef(LocalRef ref, Object o){
		return ref.qualifier.visit( this, null );
	}

	public TypeDenoter visitMemberRef(MemberRef ref, Object o){
		return ref.qualifier.visit( this, null );
	}

	public TypeDenoter visitClassRef(ClassRef ref, Object o){return null;}
	
	public TypeDenoter visitThisRef(ThisRef ref, Object o){
		return new TypeDenoter( TypeKind.CLASS, currentClass );
	}

	public TypeDenoter visitDeRef(DeRef ref, Object o){
		return ref.right.visit( this, null );
	}

	public TypeDenoter visitIndexedRef(IndexedRef ref, Object o){

		if( ref.ref instanceof ThisRef || ref.ref instanceof ClassRef ){
			reporter.reportTypeError( "***expected array type: line " + ref.indexExpr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}else if( ref.ref instanceof DeRef && ((DeRef)ref.ref).right.decl.type.typeKind != TypeKind.ARRAY ){
			reporter.reportTypeError( "***expected array type: line " + ref.indexExpr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}else if( ref.ref instanceof LocalRef && ((LocalRef)ref.ref).qualifier.decl.type.typeKind != TypeKind.ARRAY ){
			reporter.reportTypeError( "***expected array type: line " + ref.indexExpr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}else if( ref.ref instanceof MemberRef &&((MemberRef)ref.ref).qualifier.decl.type.typeKind != TypeKind.ARRAY ){
			reporter.reportTypeError( "***expected array type: line " + ref.indexExpr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}

		TypeDenoter t = ref.indexExpr.visit( this, null );

		if( !testAndSet( t, new TypeDenoter( TypeKind.INT ), ref.indexExpr.posn ) ){
			reporter.reportTypeError( "***expected an expression of int type: line " + ref.indexExpr.posn.start );
			return new TypeDenoter( TypeKind.ERROR );
		}

		TypeDenoter t2 = ref.ref.visit( this, null );
		TypeKind kind = TypeKind.CLASS;
		if( t2.typeName.equals( TypeDenoter.INT_NAME ) )
			kind = TypeKind.INT;
		else if( t2.typeName.equals( TypeDenoter.ERROR_NAME ) )
			kind = TypeKind.ERROR;
		else if( t2.typeName.equals( TypeDenoter.BOOL_NAME ) || t2.typeName.equals( TypeDenoter.VOID_NAME ) ){
			reporter.reportTypeError( "***unrecognized array type: line " + ref.indexExpr.posn.start );
			kind = TypeKind.ERROR;
		}else if( t2.typeName.equals( TypeDenoter.UNSP_NAME ) ){
			reporter.reportTypeError( "***array of unsupported type is unsupported: line " + ref.indexExpr.posn.start );
			kind = TypeKind.ERROR;
		}else if( t2.typeName.equals( TypeDenoter.MTHD_NAME ) ){
			reporter.reportTypeError( "***cannot generate an array of type <method_signature>: line " + ref.indexExpr.posn.start );
			kind = TypeKind.ERROR;
		}

		return new TypeDenoter( kind, t2.typeName ); 
	}


  // Terminals
	public TypeDenoter visitIdentifier(Identifier id, Object o){
		return new TypeDenoter( id.decl.type );
	}

	public TypeDenoter visitOperator(Operator op, Object o){return null;}

	public TypeDenoter visitIntLiteral(IntLiteral num, Object o){
		return new TypeDenoter( TypeKind.INT );
	}

	public TypeDenoter visitBooleanLiteral(BooleanLiteral bool, Object o){
		return new TypeDenoter( TypeKind.BOOLEAN );
	}


}