/* 
 * University of North Carolina at Chapel Hill
 * Spring 2013 COMP 520 - Compilers
 * miniJava Compiler Project
 * Renato Pereyra
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ForStmt extends Statement
{
    public ForStmt(Statement i, Expression c, Statement u, Statement b, SourcePosition posn){
        
        super(posn);

        init = i;
        updt = u;
        cond = c;
        body = b;
        
    }
        
    public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitForStmt(this, o);
    }

    public Statement init;
    public Expression cond;
    public Statement updt;
    public Statement body;
    
}
