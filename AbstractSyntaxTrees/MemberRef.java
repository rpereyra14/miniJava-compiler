/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class MemberRef extends Reference {
	
	public MemberRef( Identifier id, boolean thisRelative ){
		super(id.posn);
		this.qualifier = id;
		this.thisRelative = thisRelative;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitMemberRef(this, o);
	}

	public Identifier qualifier;
	public boolean thisRelative;
}