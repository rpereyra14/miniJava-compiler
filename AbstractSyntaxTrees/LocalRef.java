/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class LocalRef extends Reference {
	
	public LocalRef( Identifier id ){
		super(id.posn);
		this.qualifier = id;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitLocalRef(this, o);
	}

	public Identifier qualifier;
}