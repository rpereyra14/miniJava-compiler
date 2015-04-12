/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassRef extends Reference {
	
	public ClassRef( Identifier ct ){
		super(ct.posn);
		this.classid = ct;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitClassRef(this, o);
	}

	public Identifier classid;
}