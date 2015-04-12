/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.CodeGenerator;

import java.util.*;
import mJAM.Machine.Reg;

public class EntityTable{

	private HashMap<String,RuntimeEntity> entities;
	private Stack<UnknownEntity> unknownAddressEntities;

	public EntityTable(){

		entities = new HashMap<String,RuntimeEntity>();
		unknownAddressEntities = new Stack<UnknownEntity>();		
	
	}

	public void insert( String n, RuntimeEntity r ){
		entities.put( n, r );
	}

	public RuntimeEntity get( String n ){
		return entities.get( n );
	}

	public RuntimeEntity get_or_setUnknown( String n, int labelLocation ){
		if( entities.containsKey( n ) ){
			return entities.get( n );
		}else{
			UnknownEntity ured = new UnknownEntity( n, labelLocation );
			if( !n.equals( "System.out.println" ) )
				unknownAddressEntities.push( ured );
			return (RuntimeEntity)ured;
		}
	}

	public UnknownEntity[] getUnknowns(){
		UnknownEntity[] toReturn = new UnknownEntity[ unknownAddressEntities.size() ];
		int i = 0;
		while( !unknownAddressEntities.empty() ){
			toReturn[i] = unknownAddressEntities.pop();
			i++;
		}
		return toReturn;
	}

}