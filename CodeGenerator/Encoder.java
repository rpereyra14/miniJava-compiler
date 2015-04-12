/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import mJAM.Machine;
import mJAM.Machine.Reg;
import mJAM.Machine.Op;
import mJAM.Machine.Prim;
import mJAM.ObjectFile;
import mJAM.Disassembler;
import mJAM.Interpreter;
import java.util.*;

public class Encoder implements Visitor<RuntimeEntity,RuntimeEntity> {

	private static final int NO_SUPERCLASS = -1;
	private int countPushed;
	private int offsetFromLB;
	private EntityTable entities;
	private String currentClass;
	private String currentLocation;
	private String previousLocation;
	private String fullNameOfMain;
	private String filename;

	public Encoder( String filename ){
		Machine.initCodeGen();
		entities = new EntityTable();
		currentClass = new String();
		currentLocation = new String();
		previousLocation = new String();
		fullNameOfMain = new String();
		this.filename = filename;
		offsetFromLB = 3;
		countPushed = 0;
	}

	public void generateCode( AST ast ){
		entities.insert( "System.out.println", null );
		ast.visit( this, null );

		/* write code as an object file */
		String objectCodeFileName = filename + ".mJAM";
		ObjectFile objF = new ObjectFile(objectCodeFileName);
		System.out.print("Writing object code file " + objectCodeFileName + " ... ");
		if (objF.write()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");	
		
		/* create asm file using disassembler */
		System.out.print("Writing assembly file ... ");
		Disassembler d = new Disassembler(objectCodeFileName);
		if (d.disassemble()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");
		/* run code */
		System.out.println("Running code ... ");
		Interpreter.interpret(objectCodeFileName);

		System.out.println("*** mJAM execution completed");
	}

	private void updateLocation( String newLocation ){
		previousLocation = currentLocation;
		currentLocation = newLocation;
	}

	private void revertLocation(){
		currentLocation = previousLocation;
	}

  // Package
	public RuntimeEntity visitPackage(Package prog, RuntimeEntity r){

		RuntimeEntity red;
		int displacement = 0;

		for( ClassDecl c: prog.classDeclList ){
			red = new RuntimeEntity( c.name, Reg.SB, displacement );
			displacement += 2 + c.methodDeclList.size();
			entities.insert( c.name, red );
		}

		for( ClassDecl c: prog.classDeclList )
			c.visit( this, null );

		UnknownEntity[] unknowns = entities.getUnknowns();
		for( UnknownEntity unknown: unknowns ){
			red = entities.get( unknown.name );
			Machine.patch( unknown.labelLocation, red.displacement );
		}

		red = entities.get( fullNameOfMain );
		Machine.emit( Op.LOADL, NO_SUPERCLASS );
		Machine.emit( Op.CALL, red.register, red.displacement );
		Machine.emit( Op.HALT );

		return null;

	}

  // Declarations
	public RuntimeEntity visitClassDecl(ClassDecl cd, RuntimeEntity r){

		int patchme = Machine.nextInstrAddr();
		Machine.emit( Op.JUMP, Reg.CB, 0 );

		currentClass = cd.name;
		updateLocation( cd.name );
		
		for( MethodDecl m: cd.methodDeclList )
			m.visit( this, null );

		int label = Machine.nextInstrAddr();
		Machine.patch( patchme, label );
		Machine.emit( Op.LOADL, NO_SUPERCLASS );
		Machine.emit( Op.LOADL, cd.methodDeclList.size() );

		RuntimeEntity red;
		for( MethodDecl m: cd.methodDeclList ){
			red = entities.get( cd.name + "." + m.name );
			Machine.emit( Op.LOADA, red.register, red.displacement );
		}

		return null;

	}
	public RuntimeEntity visitFieldDecl(FieldDecl fd, RuntimeEntity r){return null;}
	public RuntimeEntity visitMethodDecl(MethodDecl md, RuntimeEntity r){

		updateLocation( currentClass + "." + md.name );

		if( md.main )
			fullNameOfMain = currentLocation;

		RuntimeEntity red = new RuntimeEntity( currentLocation, Reg.CB, Machine.nextInstrAddr() );
		entities.insert( currentLocation, red );

		int i = -1;
		for( ParameterDecl p : md.parameterDeclList ){
			red = new RuntimeEntity( currentLocation + "." + p.name, Reg.LB, i );
			entities.insert( currentLocation + "." + p.name, red );
			i--;
		}

		for( Statement s: md.statementList )
			s.visit( this, null );

		if( md.returnExp != null ){
			md.returnExp.visit( this, null );
			Machine.emit( Op.RETURN, 1, 0, md.parameterDeclList.size() );			
		}else if( !md.main ){
			Machine.emit( Op.RETURN, 0, 0, md.parameterDeclList.size() );
		}else{
			Machine.emit( Op.RETURN, 0, 0, 0 );
		}

		revertLocation();
		
		return null;

	}
	public RuntimeEntity visitParameterDecl(ParameterDecl pd, RuntimeEntity r){return null;}
	public RuntimeEntity visitVarDecl(VarDecl decl, RuntimeEntity r){return null;}
 
  // Types
	public RuntimeEntity visitBaseType(BaseType type, RuntimeEntity r){return null;}
	public RuntimeEntity visitClassType(ClassType type, RuntimeEntity r){return null;}
	public RuntimeEntity visitArrayType(ArrayType type, RuntimeEntity r){return null;}
	public RuntimeEntity visitMethodType(MethodType type, RuntimeEntity r){return null;}
	
  // Statements
	public RuntimeEntity visitBlockStmt(BlockStmt stmt, RuntimeEntity r){

		int temp = countPushed;
		countPushed = 0;

		for( Statement s: stmt.sl )
			s.visit( this, null );

		if( countPushed != 0 ){
			Machine.emit( Op.POP, countPushed );
			offsetFromLB = offsetFromLB - countPushed;
		}
		countPushed = temp;

		return null;

	}
	public RuntimeEntity visitVardeclStmt(VarDeclStmt stmt, RuntimeEntity r){

		Machine.emit( Op.PUSH, 1 );
		countPushed++;
		RuntimeEntity red = new RuntimeEntity( currentLocation + stmt.varDecl.name, Reg.LB, offsetFromLB );
		entities.insert( currentLocation + "." + stmt.varDecl.name, red );
		offsetFromLB++;
		stmt.initExp.visit( this, null );
		Machine.emit( Op.STORE, red.register, red.displacement );

		return null;

	}
	public RuntimeEntity visitAssignStmt(AssignStmt stmt, RuntimeEntity r){

		RuntimeEntity red = stmt.ref.visit( this, null );
		stmt.val.visit( this, null );
		if( stmt.ref instanceof MemberRef || stmt.ref instanceof DeRef )
			Machine.emit( Prim.fieldupd );
		else if( stmt.ref instanceof IndexedRef )
			Machine.emit( Prim.arrayupd );
		else if( stmt.ref instanceof LocalRef )
			Machine.emit( Op.STORE, red.register, red.displacement );
		else{
			//System.out.println( "red was null but not an accounted type of ref in assign stmt, line" + stmt.posn.start );
		}

		return null;

	}
	public RuntimeEntity visitCallStmt(CallStmt stmt, RuntimeEntity r){

		for( Expression e: stmt.argList ){
			e.visit( this, null );
		}

		if( checkPrintln( stmt.methodRef ) ){
			Machine.emit( Prim.putint );
			return null;
		}

		RuntimeEntity red = stmt.methodRef.visit( this, null );
		Machine.emit( Op.CALL, red.register, red.displacement );

		if( stmt.methodRef instanceof MemberRef && ((MethodDecl)((MemberRef)stmt.methodRef).qualifier.decl).returnExp != null )
			Machine.emit( Op.POP, 1 );
		else if( stmt.methodRef instanceof DeRef && ((MethodDecl)((DeRef)stmt.methodRef).right.decl).returnExp != null )
			Machine.emit( Op.POP, 1 );

		return null;

	}

	private boolean checkPrintln( Reference ref ){

		return ref instanceof DeRef && ((DeRef)ref).left instanceof DeRef && ((DeRef)((DeRef)ref).left).left instanceof ClassRef
		&& ((ClassRef)((DeRef)((DeRef)ref).left).left).classid.spelling.equals("System") && ((DeRef)((DeRef)ref).left).right.spelling.equals("out") 
		&& ((DeRef)ref).right.spelling.equals("println") && ((DeRef)((DeRef)ref).left).right.decl.type instanceof ClassType && 
		((ClassType)((DeRef)((DeRef)ref).left).right.decl.type).className.equals("_PrintStream");

	}

	public RuntimeEntity visitIfStmt(IfStmt stmt, RuntimeEntity r){

		int jumpToElseAddress, jumpToEndAddress;

		stmt.cond.visit( this, null );

		jumpToElseAddress = Machine.nextInstrAddr();
		Machine.emit( Op.JUMPIF, Machine.falseRep, Reg.CB, 0 );
		stmt.thenStmt.visit( this, null );

		if( stmt.elseStmt == null ){
			Machine.patch( jumpToElseAddress, Machine.nextInstrAddr() );
			return null;
		}

		jumpToEndAddress = Machine.nextInstrAddr();
		Machine.emit( Op.JUMP, Reg.CB, 0 );
		Machine.patch( jumpToElseAddress, Machine.nextInstrAddr() );

		stmt.elseStmt.visit( this, null );

		Machine.patch( jumpToEndAddress, Machine.nextInstrAddr() );

		return null;

	}
	public RuntimeEntity visitWhileStmt(WhileStmt stmt, RuntimeEntity r){

		int jumpAddress, loopAddress;
		jumpAddress = Machine.nextInstrAddr();
		Machine.emit( Op.JUMP, Reg.CB, 0 );
		loopAddress = Machine.nextInstrAddr();
		stmt.body.visit( this, null );
		Machine.patch( jumpAddress, Machine.nextInstrAddr() );
		stmt.cond.visit( this, null );
		Machine.emit( Op.JUMPIF, Machine.trueRep, Reg.CB, loopAddress );

		return null;

	}

	public RuntimeEntity visitForStmt(ForStmt stmt, RuntimeEntity r){
		int temp = countPushed;
		countPushed = 0;

		stmt.init.visit( this, null );

		int jumpAddress, loopAddress;
		jumpAddress = Machine.nextInstrAddr();
		Machine.emit( Op.JUMP, Reg.CB, 0 );
		loopAddress = Machine.nextInstrAddr();
		stmt.body.visit( this, null );
		stmt.updt.visit( this, null );

		Machine.patch( jumpAddress, Machine.nextInstrAddr() );

		stmt.cond.visit( this, null );
		Machine.emit( Op.JUMPIF, Machine.trueRep, Reg.CB, loopAddress );

		if( countPushed != 0 ){
			Machine.emit( Op.POP, countPushed );
			offsetFromLB = offsetFromLB - countPushed;
		}
		countPushed = temp;

		return null;
	}
	
  // Expressions
	public RuntimeEntity visitUnaryExpr(UnaryExpr expr, RuntimeEntity r){

		expr.expr.visit( this, null );

		if( expr.operator.spelling.equals( "!" ) )
			Machine.emit( Prim.not );
		else
			Machine.emit( Prim.neg );

		return null;

	}
	public RuntimeEntity visitBinaryExpr(BinaryExpr expr, RuntimeEntity r){

		expr.left.visit( this, null );

		String s = expr.operator.spelling;

		switch( s ){
			case ">":
				expr.right.visit( this, null );
				Machine.emit( Prim.gt );
				break;
			case "<":
				expr.right.visit( this, null );
				Machine.emit( Prim.lt );
				break;
			case "==":
				expr.right.visit( this, null );
				Machine.emit( Prim.eq );
				break;
			case "<=":
				expr.right.visit( this, null );
				Machine.emit( Prim.le );
				break;
			case ">=":
				expr.right.visit( this, null );
				Machine.emit( Prim.ge );
				break;
			case "!=":	
				expr.right.visit( this, null );
				Machine.emit( Prim.ne );
				break;
			case "&&":
				Machine.emit( Op.LOAD, Reg.ST, -1 );
				int jumpAddress = Machine.nextInstrAddr();
				Machine.emit( Op.JUMPIF, Machine.falseRep, Reg.CB, 0 );
				expr.right.visit( this, null );
				Machine.emit( Prim.and );
				Machine.patch( jumpAddress, Machine.nextInstrAddr() );
				break;
			case "||":
				Machine.emit( Op.LOAD, Reg.ST, -1 );
				jumpAddress = Machine.nextInstrAddr();
				Machine.emit( Op.JUMPIF, Machine.trueRep, Reg.CB, 0 );
				expr.right.visit( this, null );
				Machine.emit( Prim.or );
				Machine.patch( jumpAddress, Machine.nextInstrAddr() );
				break;
			case "+":
				expr.right.visit( this, null );
				Machine.emit( Prim.add );
				break;
			case "-":
				expr.right.visit( this, null );
				Machine.emit( Prim.sub );
				break;
			case "*":
				expr.right.visit( this, null );
				Machine.emit( Prim.mult );
				break;
			case "/":
				expr.right.visit( this, null );
				Machine.emit( Prim.div );
				break;
		}

		return null;

	}

	public RuntimeEntity visitRefExpr(RefExpr expr, RuntimeEntity r){

		RuntimeEntity red = expr.ref.visit( this, null );
		if( expr.ref instanceof MemberRef || ( expr.ref instanceof DeRef && !((DeRef)expr.ref).right.spelling.equals("length") ) )
			Machine.emit( Prim.fieldref );
		else if( expr.ref instanceof IndexedRef )
			Machine.emit( Prim.arrayref );
		else if( expr.ref instanceof LocalRef ){
			Machine.emit( Op.LOAD, red.register, red.displacement );
		}
		return null;

	}
	public RuntimeEntity visitCallExpr(CallExpr expr, RuntimeEntity r){

		for( Expression e: expr.argList ){
			e.visit( this, null );
		}

		RuntimeEntity red = expr.functionRef.visit( this, null );
		Machine.emit( Op.CALL, red.register, red.displacement );
		return null;

	}

	public RuntimeEntity visitLiteralExpr(LiteralExpr expr, RuntimeEntity r){

		expr.literal.visit( this, null );
		return null;

	}
	public RuntimeEntity visitNewObjectExpr(NewObjectExpr expr, RuntimeEntity r){

		RuntimeEntity red = entities.get( expr.classtype.className );
		Machine.emit( Op.LOADA, red.register, red.displacement );
		Machine.emit( Op.LOADL, expr.classtype.classDecl.fieldDeclList.size() );
		Machine.emit( Prim.newobj );
		return null;

	}
	public RuntimeEntity visitNewArrayExpr(NewArrayExpr expr, RuntimeEntity r){

		expr.sizeExpr.visit( this, null );
		Machine.emit( Prim.newarr );
		return null;

	}
	
  // References
	public RuntimeEntity visitQualifiedRef(QualifiedRef ref, RuntimeEntity r){return null;}
	
	public RuntimeEntity visitNullRef(NullRef ref, RuntimeEntity r){

		Machine.emit( Op.LOADL, Machine.nullRep );
		return null;

	}
	public RuntimeEntity visitLocalRef(LocalRef ref, RuntimeEntity r){
		
		RuntimeEntity red = entities.get( currentLocation + "." + ref.qualifier.spelling );
		return red;

	}
	public RuntimeEntity visitMemberRef(MemberRef ref, RuntimeEntity r){
		
		RuntimeEntity red = null;

		if( ref.qualifier.decl.type instanceof MethodType ){
			Machine.emit( Op.LOADA, Reg.OB, 0 );
			red = entities.get_or_setUnknown( currentClass + "." + ref.qualifier.spelling, Machine.nextInstrAddr() );
		}else{
			Machine.emit( Op.LOADA, Reg.OB, 0 );
			Machine.emit( Op.LOADL, ((MemberDecl)ref.qualifier.decl).index );
			red = new RuntimeEntity( currentClass + "." + ref.qualifier.spelling, Reg.OB, ((MemberDecl)ref.qualifier.decl).index );
		}

		return red;

	}
	public RuntimeEntity visitClassRef(ClassRef ref, RuntimeEntity r){return null;}
	public RuntimeEntity visitThisRef(ThisRef ref, RuntimeEntity r){
		Machine.emit( Op.LOADA, Reg.OB, 0 );
		return null;
	}
	public RuntimeEntity visitDeRef(DeRef ref, RuntimeEntity r){

		RuntimeEntity red = ref.left.visit( this, null );

		if( !(ref.right.decl.type instanceof MethodType) ){

			int index = ((MemberDecl)ref.right.decl).index;
			if( index == -1 ){
				Machine.emit( Op.LOAD, red.register, red.displacement + index );
				return red;
			}
			if( ref.left instanceof LocalRef ){
				Machine.emit( Op.LOAD, red.register, red.displacement );
				Machine.emit( Op.LOADL, index );
			}else if( ref.left instanceof MemberRef ){
				Machine.emit( Prim.fieldref );
				Machine.emit( Op.LOADL, index );
			}else if( ref.left instanceof DeRef ){
				Machine.emit( Prim.fieldref );
				Machine.emit( Op.LOADL, index );
			}

		}else{

			if( ref.left instanceof LocalRef ){
				Machine.emit( Op.LOAD, red.register, red.displacement );
				red = entities.get_or_setUnknown( ((ClassType)((LocalRef)ref.left).qualifier.decl.type).className + 
													"." + ref.right.spelling, Machine.nextInstrAddr() );
			}else if( ref.left instanceof MemberRef ){
				Machine.emit( Prim.fieldref );
				red = entities.get_or_setUnknown( ((ClassType)((MemberRef)ref.left).qualifier.decl.type).className + 
													"." + ref.right.spelling, Machine.nextInstrAddr() );
			}else if( ref.left instanceof DeRef ){
				Machine.emit( Prim.fieldref );
				red = entities.get_or_setUnknown( ((ClassType)((DeRef)ref.left).right.decl.type).className + 
													"." + ref.right.spelling, Machine.nextInstrAddr() );
			}

		}

		return red;

	}

	public RuntimeEntity visitIndexedRef(IndexedRef ref, RuntimeEntity r){

		if( ref.ref instanceof LocalRef ){
			RuntimeEntity red = entities.get( currentLocation + "." + ((LocalRef)ref.ref).qualifier.spelling );
			Machine.emit( Op.LOAD, red.register, red.displacement );
		}else{
			ref.ref.visit( this, null );
			Machine.emit( Prim.add );
		}
		ref.indexExpr.visit( this, null );
		return null;

	}

  // Terminals
	public RuntimeEntity visitIdentifier(Identifier id, RuntimeEntity r){return null;}
	public RuntimeEntity visitOperator(Operator op, RuntimeEntity r){return null;}
	public RuntimeEntity visitIntLiteral(IntLiteral num, RuntimeEntity r){

		Machine.emit( Op.LOADL, Integer.parseInt(num.spelling) );
		return null;

	}
	public RuntimeEntity visitBooleanLiteral(BooleanLiteral bool, RuntimeEntity r){

		if( bool.spelling.equals("true") )
			Machine.emit( Op.LOADL, Machine.trueRep );
		else
			Machine.emit( Op.LOADL, Machine.falseRep );
		return null;

	}
}
