/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class RuntimeEntity{

	public RuntimeEntity( String n, Reg r, int d ){
		this.name = n;
		this.register = r;
		this.displacement = d;
	}

	public RuntimeEntity( String n ){
		this.name = n;
	}

	public String name;
	public Reg register;
	public int displacement;

}