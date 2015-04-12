/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class NullRef extends Reference {
	
	public NullRef( SourcePosition posn ){
		super(posn);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitNullRef(this, o);
	}

}