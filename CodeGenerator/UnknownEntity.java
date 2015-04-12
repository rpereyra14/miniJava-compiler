/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.CodeGenerator;

import mJAM.Machine.Reg;

public class UnknownEntity extends RuntimeEntity{

	public UnknownEntity( String n, int labelLocation ){
		super( n, Reg.CB, 0 );
		this.labelLocation = labelLocation;
	}

	public int labelLocation;

}