/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;

class OperatorDenoter{

	TypeDenoter left;
	TypeDenoter right;
	TypeDenoter returnType;

	OperatorDenoter( Operator o ){

		String s = o.spelling;

		if( s.equals( ">" ) || s.equals( "<" ) || s.equals( "<=" ) || s.equals( ">=" ) ){

			left = new TypeDenoter( TypeKind.INT );
			right = new TypeDenoter( TypeKind.INT );
			returnType = new TypeDenoter( TypeKind.BOOLEAN );

		}else if( s.equals( "==" ) ){

			left = new TypeDenoter( TypeKind.MISCELLANEOUS );
			right = new TypeDenoter( TypeKind.MISCELLANEOUS );
			returnType = new TypeDenoter( TypeKind.BOOLEAN );

		}else if( s.equals( "!=" ) ){

			left = new TypeDenoter( TypeKind.MISCELLANEOUS );
			right = new TypeDenoter( TypeKind.MISCELLANEOUS );
			returnType = new TypeDenoter( TypeKind.BOOLEAN );

		}else if( s.equals( "&&" ) || s.equals( "||" ) ){

			left = new TypeDenoter( TypeKind.BOOLEAN );
			right = new TypeDenoter( TypeKind.BOOLEAN );
			returnType = new TypeDenoter( TypeKind.BOOLEAN );

		}else if( s.equals( "!" ) ){

			left = null;
			right = new TypeDenoter( TypeKind.BOOLEAN );
			returnType = new TypeDenoter( TypeKind.BOOLEAN );

		}else if( s.equals( "+" ) || s.equals( "-" ) || s.equals( "*" ) || s.equals( "/" ) ){

			left = new TypeDenoter( TypeKind.INT );
			right = new TypeDenoter( TypeKind.INT );
			returnType = new TypeDenoter( TypeKind.INT );

		}

	}

}