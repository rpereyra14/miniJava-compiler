/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

/*
 * NOTE: Some of the code in this file may have been obtained from the Triangle compiler
 * provided by D.A. Watt and D.F. Brown in support of their textbook 
 * "Programming Language Processors in Java: Compilers and Interpreters" (ISBN 0-130-25786-9)
 */

package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.Declaration;

public class IDEntry{

	protected String id;
	protected Declaration attr;
	protected boolean isStatic;
	protected boolean isPrivate;
	protected int level;

	IDEntry( String s, Declaration d, boolean st, boolean p, int l ){
		this.id = s;
		this.attr = d;
		this.isStatic = st;
		this.isPrivate = p;
		this.level = l;
	}

}