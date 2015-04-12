/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;

class TypeDenoter{

	public TypeKind typeKind;
	public String typeName;

	protected final static String INT_NAME = "<int>";
	protected final static String BOOL_NAME = "<boolean>";
	protected final static String ERROR_NAME = "<error>";
	protected final static String VOID_NAME = "<void>";
	protected final static String UNSP_NAME = "<unsupported>";
	protected final static String MISC_NAME = "<miscellaneous>";
	protected final static String MTHD_NAME = "<method>";

	TypeDenoter( TypeKind t, String s ){
		typeKind = t;
		typeName = s;
	}

	TypeDenoter( TypeKind t ){

		typeKind = t;

		switch( t ){

			case VOID:
				typeName = VOID_NAME;
				break;
			case INT:
				typeName = INT_NAME;
				break;
			case BOOLEAN:
				typeName = BOOL_NAME;
				break;
			case UNSUPPORTED:
				typeName = UNSP_NAME;
				break;
			case MISCELLANEOUS:
				typeName = MISC_NAME;
				break;
			case METHOD:
				typeName = MTHD_NAME;
				break;
			case ERROR:
				typeName = ERROR_NAME;
				break;

		}

	}

	TypeDenoter( Type t ){

		typeKind = t.typeKind;

		switch( t.typeKind ){

			case VOID:
				typeName = VOID_NAME;
				break;
			case INT:
				typeName = INT_NAME;
				break;
			case BOOLEAN:
				typeName = BOOL_NAME;
				break;
			case CLASS:
				typeName = ((ClassType)t).className;
				break;
			case ARRAY:
				TypeDenoter dummyDenoter = new TypeDenoter( ((ArrayType)t).eltType );
				typeName = dummyDenoter.typeName;
				break;
			case UNSUPPORTED:
				typeName = UNSP_NAME;
				break;
			case MISCELLANEOUS:
				typeName = MISC_NAME;
				break;
			case METHOD:
				typeName = MTHD_NAME;
				break;
			case ERROR:
				typeName = ERROR_NAME;
				break;

		}

	}

}