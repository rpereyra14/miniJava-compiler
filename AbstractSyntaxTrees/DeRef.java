/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class DeRef extends Reference {
	
	public DeRef( Reference l, Identifier r ){
		super(l.posn);
		this.left = l;
		this.right = r;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitDeRef(this, o);
	}

	public Reference left;
	public Identifier right;
	
}