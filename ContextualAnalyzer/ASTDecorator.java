/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ErrorReporter;

public final class ASTDecorator implements Visitor<Object,Object>{

	protected IdentificationTable idTable;
	protected boolean staticContext;			//determines if a reference is made in a static context
	protected String currentClass;				//the current class being decorated
	public ErrorReporter reporter;

	public ASTDecorator( ErrorReporter reporter ){
		idTable = new IdentificationTable();
		staticContext = false;
		this.reporter = reporter;
	}

	public void decorateAST( AST ast ){
		ast.visit( this, null );
	}

  // Package
	public Object visitPackage(Package prog, Object o){
		try{
			idTable.insertExternals( prog.classDeclList );
		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
			return null;
		}
		for( ClassDecl c: prog.classDeclList ){
			currentClass = c.name;
			c.visit( this, null );
		}
		return null;
	}

  // Declarations
	public Object visitClassDecl(ClassDecl cd, Object o){

		idTable.openScope();

		int i = 0;

		for( FieldDecl f: cd.fieldDeclList ){
			f.index = i;
			i++;
			f.type.visit( this, null );
			try{
				idTable.insertInternal( f.name, f );
			}catch( IdentificationError e ){
				reporter.reportIdentificationError( e.getMessage() );
				return null;
			}
		}

		i = 0;
		for( MethodDecl m: cd.methodDeclList ){
			m.index = i;
			i++;
			m.type.visit( this, null );
			try{
				idTable.insertInternal( m.name, m );
			}catch( IdentificationError e ){
				reporter.reportIdentificationError( e.getMessage() );
				return null;
			}
		}

		for( MethodDecl m: cd.methodDeclList ){
			staticContext = m.isStatic;
			m.visit( this, null );
		}

		idTable.closeScope();

		return null;

	}

	public Object visitFieldDecl(FieldDecl fd, Object o){return null;}

	public Object visitMethodDecl(MethodDecl md, Object o){

		idTable.openScope();
		for( ParameterDecl pd: md.parameterDeclList )
			pd.visit( this, null );

		idTable.openScope();
		for( Statement s: md.statementList )
			s.visit( this, null );

		if( md.returnExp != null )
			md.returnExp.visit( this, null );

		idTable.closeScope();
		idTable.closeScope();

		return null;

	}
	
	public Object visitParameterDecl(ParameterDecl pd, Object o){

		pd.type.visit( this, null );
		try{
			idTable.insertInternal( pd.name, pd );
		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
		}
		return null;

	}

	public Object visitVarDecl(VarDecl decl, Object o){

		decl.type.visit( this, null );
		try{
			idTable.insertInternal( decl.name, decl );
		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
		}
		return null;

	}
 
  // Types
	public Object visitBaseType(BaseType type, Object o){return null;}

	public Object visitClassType(ClassType type, Object o){
		try{	
			type.classDecl = (ClassDecl)idTable.retrieveExternal( "<classes>", type.className, false, type.posn ).attr;
		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
		}
		return null;
	}

	public Object visitArrayType(ArrayType type, Object o){
		type.eltType.visit( this, null );
		return null;
	}

	public Object visitMethodType(MethodType type, Object o){
		type.eltType.visit( this, null );
		return null;
	}
	
  // Statements
	public Object visitBlockStmt(BlockStmt stmt, Object o){

		idTable.openScope();

		for( Statement s: stmt.sl )
			s.visit( this, null );

		idTable.closeScope();

		return null;

	}

	public Object visitVardeclStmt(VarDeclStmt stmt, Object o){

		stmt.varDecl.visit( this, null );
		idTable.flag( stmt.varDecl.name );
		stmt.initExp.visit( this, null );
		idTable.releaseflag();
		return null;

	}

	public Object visitAssignStmt(AssignStmt stmt, Object o){

		stmt.ref = (Reference)stmt.ref.visit( this, null );

		if( stmt.ref instanceof ThisRef )
			reporter.reportIdentificationError( "***reference \"this\" is not assignable: line " + stmt.ref.posn.start );
		else if( stmt.ref instanceof DeRef && ((((DeRef)stmt.ref).left instanceof MemberRef && ((MemberRef)((DeRef)stmt.ref).left).qualifier.decl.type.typeKind == TypeKind.ARRAY)||
				(((DeRef)stmt.ref).left instanceof LocalRef && ((LocalRef)((DeRef)stmt.ref).left).qualifier.decl.type.typeKind == TypeKind.ARRAY)) && 
				((DeRef)stmt.ref).right.spelling.equals("length") )
			reporter.reportIdentificationError( "***length property is not assignable: line " + stmt.ref.posn.start );

		stmt.val.visit( this, null );
		return null;

	}

	public Object visitCallStmt(CallStmt stmt, Object o){

		stmt.methodRef = (Reference)stmt.methodRef.visit( this, null );
		for( Expression e: stmt.argList )
			e.visit( this, null );
		return null;

	}

	public Object visitIfStmt(IfStmt stmt, Object o){

		if( stmt.thenStmt instanceof VarDeclStmt || 
			(stmt.thenStmt instanceof BlockStmt && ((BlockStmt)stmt.thenStmt).sl.size() == 1 && 
				((BlockStmt)stmt.thenStmt).sl.get(0) instanceof VarDeclStmt ))
			reporter.reportIdentificationError( "***cannot have a lone var decl in then block : line " + stmt.thenStmt.posn.start );

		if( stmt.elseStmt != null && (stmt.elseStmt instanceof VarDeclStmt || 
			(stmt.elseStmt instanceof BlockStmt && ((BlockStmt)stmt.elseStmt).sl.size() == 1 && 
				((BlockStmt)stmt.thenStmt).sl.get(0) instanceof VarDeclStmt ) ))
			reporter.reportIdentificationError( "***cannot have a lone var decl in else block : line " + stmt.elseStmt.posn.start );

		stmt.cond.visit( this, null );
		stmt.thenStmt.visit( this, null );
		if( stmt.elseStmt != null )
			stmt.elseStmt.visit( this, null );
		return null;

	}

	public Object visitWhileStmt(WhileStmt stmt, Object o){

		if( stmt.body instanceof VarDeclStmt || (stmt.body instanceof BlockStmt 
			&& ((BlockStmt)stmt.body).sl.size() == 1 && ((BlockStmt)stmt.body).sl.get(0) instanceof VarDeclStmt ) )
			reporter.reportIdentificationError( "***cannot have a lone var decl in body : line " + stmt.body.posn.start );

		stmt.cond.visit( this, null );
		stmt.body.visit( this, null );
		return null;

	}
	
	public Object visitForStmt(ForStmt stmt, Object o){

		if( stmt.body instanceof VarDeclStmt || (stmt.body instanceof BlockStmt 
			&& ((BlockStmt)stmt.body).sl.size() == 1 && ((BlockStmt)stmt.body).sl.get(0) instanceof VarDeclStmt ) )
			reporter.reportIdentificationError( "***cannot have only a var decl in then block : line " + stmt.body.posn.start );

		idTable.openScope();
		stmt.init.visit( this, null );
		stmt.cond.visit( this, null );
		stmt.updt.visit( this, null );
		stmt.body.visit( this, null );
		idTable.closeScope();
		return null;

	}
	
  // Expressions
	public Object visitUnaryExpr(UnaryExpr expr, Object o){

		expr.expr.visit( this, null );
		return null;

	}

	public Object visitBinaryExpr(BinaryExpr expr, Object o){

		expr.left.visit( this, null );
		expr.right.visit( this, null );
		return null;

	}

	public Object visitRefExpr(RefExpr expr, Object o){

		expr.ref = (Reference)expr.ref.visit( this, null );
		return null;

	}

	public Object visitCallExpr(CallExpr expr, Object o){

		expr.functionRef = (Reference)expr.functionRef.visit( this, null );

		for( Expression e: expr.argList )
			e.visit( this, null );

		return null;

	}

	public Object visitLiteralExpr(LiteralExpr expr, Object o){return null;}

	public Object visitNewObjectExpr(NewObjectExpr expr, Object o){

		expr.classtype.visit( this, null );
		return null;

	}

	public Object visitNewArrayExpr(NewArrayExpr expr, Object o){

		expr.eltType.visit( this, null );
		expr.sizeExpr.visit( this, null );
		return null;

	}

	public Object visitNullRef(NullRef ref, Object o){return ref;}
	
  // References
	public Object visitQualifiedRef(QualifiedRef ref, Object o){

		try{

			if( ref.thisRelative && staticContext )
				reporter.reportIdentificationError( "***cannot reference \"this\" from a static context: line " + ref.posn.start );

			IdentifierList ilist = ref.qualifierList;

			if( ilist.size() == 0 )
				return restructureRef( ref );

			Identifier fname = ilist.get(0);
	
			IDEntry entry = null;

			//if the identifier list is of size one, classnames should not be allowed; ensure search is internal
			if( ilist.size() == 1 ){
				entry = idTable.retrieveInternal( fname.spelling, staticContext, fname.posn, ref.thisRelative );
			}else{
				entry = idTable.retrieveInternalAllowClassNames( fname.spelling, staticContext, fname.posn, ref.thisRelative );
			}
			
			fname.decl = entry.attr;
			fname.scopeLevel = entry.level;

			String searchClass = null;

			boolean temp = staticContext;		//while going through all the dereferencing, static context is impossible except for the first identifier.
												//Temp will store the context of the referencing method until we are done dereferencing

			if( fname.scopeLevel == 0 ){

				searchClass = fname.spelling;	//Only classnames would have been declared in scope level 0; search for next identifier in the classname found

			}else{

				staticContext = false;

				//determine classname where to look for next identifier
				if( fname.decl.type.typeKind == TypeKind.CLASS ){
					searchClass = ((ClassType)fname.decl.type).className;
				}else if( fname.decl.type.typeKind == TypeKind.ARRAY ){
					searchClass = "<array>";
				}else if( fname.decl.type.typeKind == TypeKind.ARRAY && ((ArrayType)fname.decl.type).eltType.typeKind == TypeKind.CLASS ){
					searchClass = ((ClassType)((ArrayType)fname.decl.type).eltType).className;
				}else if( ilist.size() != 1 ){
					reporter.reportIdentificationError( "***cannot dereference identifier " + fname.spelling + " of non-class/non-array type : line " + fname.posn.start );
				}

			}

			Identifier lastID = null;
			int refCount = 0;			//reference count; helps determine which references must be static, if we are on the second reference
										//in a list and fname had scope level 0 then we must be looking for a member in a static context
			
			for( Identifier id: ilist ){

				refCount++;

				lastID = id;			//helpful in determining if last identifier was static or not

				if( id == fname )		//already handled fname
					continue;

				if( refCount == 2 && fname.scopeLevel == 0 )		//referencing a static member by classname
					staticContext = true;
				else
					staticContext = false;

				id.searchClass = searchClass;						//set the search class

				id.visit( this, null );
				
				if( id.decl instanceof MemberDecl && ((MemberDecl)id.decl).isStatic && !searchClass.equals( "System" ) )
					reporter.reportIdentificationError( "***PA4 disallows access through static fields: line " + ref.posn.start );

				//determine next search class
				if( id.decl.type.typeKind == TypeKind.CLASS ){
					searchClass = ((ClassType)id.decl.type).className;
				}else if( id.decl.type.typeKind == TypeKind.ARRAY ){
					searchClass = "<array>";
				}else if( id.decl.type.typeKind == TypeKind.ARRAY && ((ArrayType)id.decl.type).eltType.typeKind == TypeKind.CLASS ){
					searchClass = ((ClassType)((ArrayType)id.decl.type).eltType).className;
				}else if( refCount != ilist.size() ){
					reporter.reportIdentificationError( "***cannot dereference identifier " + id.spelling + " of non-class/non-array type : line " + id.posn.start );
				}
				
			}

			//return staticContext to whatever context was set by the referencing method
			staticContext = temp;

			//determine if the last identifier is static
			if( lastID.decl instanceof MemberDecl && ((MemberDecl)lastID.decl).isStatic )
				reporter.reportIdentificationError( "***PA3 no static access: line " + ref.posn.start );

		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
		}

		return restructureRef( ref );

	}


	//restructuring will occur by checking the scope levels of identifiers set previously
	public Reference restructureRef( QualifiedRef qr ){

		Reference ref = null;

		IdentifierList ilist = qr.qualifierList;
		int size = ilist.size();		

		if( size >= 1 ){

			Identifier id = ilist.get(0);
			if( id.scopeLevel == 0 ){				//only classnames are in scope 0
				ref = new ClassRef( id );
			}else if( id.scopeLevel == 1 ){			//members are in scope 1
				ref = new MemberRef( id, qr.thisRelative );
			}else{
				ref = new LocalRef( id );
			}
			
			if( size > 1 ){

				boolean firstID = true;
	
				//Build DeRef
				for( Identifier i: ilist ){
					
					if( firstID ){
						firstID = false;
						continue;
					}
	
					ref	= new DeRef( ref, i );
	
				}
				
			}

		//lists of size 0 must contain only the keyword "this"
		}else{
			ref = new ThisRef( currentClass, qr.posn );
		}

		return ref;
	}

	public Object visitLocalRef(LocalRef ref, Object o){return null;}
	public Object visitMemberRef(MemberRef ref, Object o){return null;}
	public Object visitClassRef(ClassRef ref, Object o){return null;}
	public Object visitThisRef(ThisRef ref, Object o){return null;}
	public Object visitDeRef(DeRef ref, Object o){return null;}

	public Object visitIndexedRef(IndexedRef ref, Object o){

		ref.ref = (Reference)ref.ref.visit( this, null );
		ref.indexExpr.visit( this, null );
		return ref;

	}

  // Terminals
	public Object visitIdentifier(Identifier id, Object o){

		boolean notLookingforMember = false;
	
		IDEntry entry = null;
		try{
			if( id.searchClass == null )
				entry = idTable.retrieveInternal( id.spelling, staticContext, id.posn, notLookingforMember );
			else if( id.searchClass.equals( currentClass ) )
				entry = idTable.retrieveInternal( id.spelling, staticContext, id.posn, !notLookingforMember );
			else
				entry = idTable.retrieveExternal( id.searchClass, id.spelling, staticContext, id.posn );
			id.decl = entry.attr;
			id.scopeLevel = entry.level;
		}catch( IdentificationError e ){
			reporter.reportIdentificationError( e.getMessage() );
		}
		return null;

	}

	public Object visitOperator(Operator op, Object o){return null;}
	public Object visitIntLiteral(IntLiteral num, Object o){return null;}
	public Object visitBooleanLiteral(BooleanLiteral bool, Object o){return null;}

}