/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.ContextualAnalyzer;

import java.util.*;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.SyntacticAnalyzer.SourcePosition;

class IdentificationTable{

	/*

		This identification table will be implemented by splitting identifiers into two tables: 
		an internal table to the current class being analyzed and an external table which will contain all classnames and members for each class.

	*/

	private int level;
	private String flaggedIdentifier;				//when declaring an identifier, it is "flagged" to prevent it from appearing in its initializing expression
	private HashMap<Integer,HashMap<String,IDEntry>> internalTable;
	private HashMap<String,HashMap<String,IDEntry>> externalTable;

	private final int PARAM_LEVEL = 2;				//scope level for parameters
	private final int CLASS_LEVEL = 0;				//scope level for classnames

	private boolean SEEN_MAIN;						//ensure there is only one public static void main with String[] parameter

	IdentificationTable(){
		level = 0;
		flaggedIdentifier = new String();
		SEEN_MAIN = false;
		internalTable = new HashMap<Integer,HashMap<String,IDEntry>>();
		externalTable = new HashMap<String,HashMap<String,IDEntry>>();
		loadStdEnv();
	}

	private void loadStdEnv(){

		SourcePosition dummyPos = new SourcePosition();

		//Load String class with zero members
		ClassDecl c = new ClassDecl( "String", new FieldDeclList(), new MethodDeclList(), dummyPos );
		HashMap<String,IDEntry> classTable = new HashMap<String,IDEntry>();
		IDEntry entry = new IDEntry( c.name, c, true, false, CLASS_LEVEL );
		classTable.put( c.name, entry );

		//Load System class
		c = new ClassDecl( "System", new FieldDeclList(), new MethodDeclList(), dummyPos );
		entry = new IDEntry( c.name, c, true, false, CLASS_LEVEL );
		classTable.put( c.name, entry );

		//Load member out of System class
		HashMap<String,IDEntry> memberTable = new HashMap<String,IDEntry>();
		FieldDecl f = new FieldDecl( false, true, new ClassType( "_PrintStream", dummyPos ) , "out", dummyPos );
		entry = new IDEntry( f.name, f, f.isStatic, f.isPrivate, CLASS_LEVEL + 1 );
		memberTable.put( f.name, entry );
		externalTable.put( c.name, memberTable );

		//Load method println of class _PrintStream
		memberTable = new HashMap<String,IDEntry>();
		f = new FieldDecl( false, false, new MethodType( new BaseType( TypeKind.VOID, dummyPos ), dummyPos ) , "println", dummyPos );
		ParameterDeclList plist = new ParameterDeclList();
		plist.add( new ParameterDecl( new BaseType( TypeKind.INT, dummyPos ), "n", dummyPos )  );
		MethodDecl m = new MethodDecl( (MemberDecl)f, plist, new StatementList(), null, dummyPos );
		entry = new IDEntry( m.name, m, m.isStatic, m.isPrivate, CLASS_LEVEL + 1 );
		memberTable.put( m.name, entry );
		externalTable.put( "_PrintStream", memberTable );

		//Register String and System as classnames
		externalTable.put( "<classes>", classTable );

		//allow arrays to have a length property
		HashMap<String,IDEntry> arrayTable = new HashMap<String,IDEntry>();
		f = new FieldDecl( false, false, new BaseType( TypeKind.INT, dummyPos ), "length", dummyPos );
		f.index = -1;
		entry = new IDEntry( f.name, f, f.isStatic, f.isPrivate, CLASS_LEVEL + 1 );
		arrayTable.put( "length", entry );
		externalTable.put( "<array>", arrayTable );

	}

	public void insertExternals( ClassDeclList clist ) throws IdentificationError {

		HashMap<String,IDEntry> memberTable = new HashMap<String,IDEntry>();	//will store members of each class as we loop through classes
		HashMap<String,IDEntry> classTable = externalTable.get( "<classes>" );

		for( ClassDecl c: clist ){

			//Register classname in class list
			if( !c.name.equals( "System" ) && classTable.containsKey( c.name ) )
				throw new IdentificationError( "***Duplicate class declaration: line " + c.posn.start );
			IDEntry entry = new IDEntry( c.name, c, true, false, CLASS_LEVEL );
			classTable.put( c.name, entry );

			//Load fields for class c
			for( FieldDecl f: c.fieldDeclList ){
				if( memberTable.containsKey( f.name ) )
					throw new IdentificationError( "***Duplicate field declaration: line " + f.posn.start );
				entry = new IDEntry( f.name, f, f.isStatic, f.isPrivate, CLASS_LEVEL + 1 );
				memberTable.put( f.name, entry );
			}

			//Load methods for class c
			for( MethodDecl m: c.methodDeclList ){

				//ensure there is only one public static void main with String[] parameters
				if( m.name.equals("main") && m.isStatic && !m.isPrivate && ((MethodType)m.type).eltType.typeKind == TypeKind.VOID){

					ParameterDeclList pd = m.parameterDeclList;
					if( !SEEN_MAIN && pd.size() == 1 && pd.get(0).type.typeKind == TypeKind.ARRAY && 
						((ArrayType)pd.get(0).type).eltType.typeKind == TypeKind.CLASS 
						&& ((ClassType)((ArrayType)pd.get(0).type).eltType).className.equals("String") ){

						SEEN_MAIN = true;
						m.main = true;
					}else if( SEEN_MAIN && pd.size() == 1 && pd.get(0).type.typeKind == TypeKind.ARRAY && 
						((ArrayType)pd.get(0).type).eltType.typeKind == TypeKind.CLASS 
						&& ((ClassType)((ArrayType)pd.get(0).type).eltType).className.equals("String") ){

						throw new IdentificationError( "***PA3 program cannot have more than one public static void main: line " + m.posn.start );
					}else{
						m.main = false;
					}

				}else{
					m.main = false;
				}

				if( memberTable.containsKey( m.name ) )
					throw new IdentificationError( "***Duplicate method declaration: line " + m.posn.start );

				entry = new IDEntry( m.name, m, m.isStatic, m.isPrivate, CLASS_LEVEL + 1 );
				memberTable.put( m.name, entry );
			}

			//Register member list for class c
			externalTable.put( c.name, memberTable );
			memberTable = new HashMap<String,IDEntry>();

		}

		externalTable.put( "<classes>", classTable );

		//ensure there was at least one main method
		if( !SEEN_MAIN )
			throw new IdentificationError( "***PA3 program must have a public static void main method" );

	}

	public void openScope(){
		level++;
		internalTable.put( level, new HashMap<String,IDEntry>() );
	}

	public void closeScope(){
		level--;
	}

	public void flag( String id ){
		flaggedIdentifier = id;
	}

	public void releaseflag(){
		flaggedIdentifier = new String();
	}

	public void insertInternal( String id, Declaration attr ) throws IdentificationError{

		IDEntry entry = null;

		//local variable
		if( level > PARAM_LEVEL ){

			HashMap<String,IDEntry> prevLevelTable;

			//ensure we are not redeclaring any other local variables
			for( int i = PARAM_LEVEL; i < level; i++ ){

				prevLevelTable = internalTable.get( i );
				if( prevLevelTable.containsKey( id ) )
					throw new IdentificationError( "***cannot redeclare \"" + id + "\" : line " + attr.posn.start );

			}

			entry = new IDEntry( id, attr, true, false, level );

		//member variable
		}else{

			HashMap<String,IDEntry> prevLevelTable = internalTable.get( level );
			if( prevLevelTable.containsKey( id ) )
				throw new IdentificationError( "***cannot redeclare \"" + id + "\" : line " + attr.posn.start );

			if( level < PARAM_LEVEL )
				entry = new IDEntry( id, attr, ((MemberDecl)attr).isStatic, ((MemberDecl)attr).isPrivate, level );
			else
				entry = new IDEntry( id, attr, true, false, level );
		}

		HashMap<String,IDEntry> scopeTable = internalTable.get( level );
		scopeTable.put( id, entry );
		internalTable.put( level, scopeTable );

	}


	//retrieve an identifier from either internals or classnames. Allowing classnames lets us reference static members in classes
	public IDEntry retrieveInternalAllowClassNames( String id, boolean staticContext, SourcePosition pos, boolean thisContext ) throws IdentificationError{

		IDEntry entry;

		try{
			entry = retrieveInternal( id, staticContext, pos, thisContext );
			return entry;
		}catch( IdentificationError e ){
			//do nothing; try looking in class names but if not found throw exception below.
		}

		try{
			entry = retrieveExternal( "<classes>", id, true, pos );
			return entry;
		}catch( IdentificationError e ){
			//do nothing; throw exception below.
		}

		throw new IdentificationError( "***undeclared identifier \"" + id + "\" : line " + pos.start );

	}

	public IDEntry retrieveInternal( String id, boolean staticContext, SourcePosition pos, boolean thisContext ) throws IdentificationError{

		HashMap<String,IDEntry> levelTable;

		for( int i = level; i > 0; i-- ){

			levelTable = internalTable.get( i );

			if( levelTable.containsKey( id ) ){
				IDEntry entry = levelTable.get( id );
				if( staticContext && !entry.isStatic )
					throw new IdentificationError( "***cannot reference \"" + id + "\" in a static context: line " + pos.start );
				if( thisContext && entry.level == 1 )
					return entry;
				else if( !thisContext && flaggedIdentifier.equals(id) )
					throw new IdentificationError( "***cannot reference \"" + id + "\" in its initializing expression: line " + pos.start );
				else if( !thisContext )
					return entry;
			}

		}

		if( thisContext )
			throw new IdentificationError( "***undeclared member \"" + id + "\" : line " + pos.start );
		else
			throw new IdentificationError( "***undeclared identifier \"" + id + "\" : line " + pos.start );

	}

	public IDEntry retrieveExternal( String classname, String member, boolean staticContext, SourcePosition pos ) throws IdentificationError{

		if( !externalTable.containsKey( classname ) ){
			throw new IdentificationError( "***undeclared class \"" + classname + "\" : line " + pos.start );
		}

		HashMap<String,IDEntry> memberTable = externalTable.get( classname );

		if( !memberTable.containsKey( member ) ){
			if( !classname.equals( "<classes>" ) ){
				throw new IdentificationError( "***undeclared member \"" + member + "\" of class " + classname +" : line " + pos.start );
			}else{
				throw new IdentificationError( "***undeclared class \"" + member + "\" : line " + pos.start );
			}
		}

		IDEntry entry = memberTable.get( member );
		if( staticContext && !entry.isStatic )
			throw new IdentificationError( "***cannot make a static reference to \"" + member + "\" : line " + pos.start );
		else if( entry.isPrivate )
			throw new IdentificationError( "***cannot reference private member \"" + member + "\" from outside class \"" + classname + "\" : line " + pos.start );

		return entry;

	}


}