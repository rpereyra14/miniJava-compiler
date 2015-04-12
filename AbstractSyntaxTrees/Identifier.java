/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.ContextualAnalyzer.IDEntry;

public class Identifier extends Terminal {

  public Identifier (String s, SourcePosition posn) {
    super (s,posn);
    decl = null;
    searchClass = null;
  }

  public <A,R> R visit(Visitor<A,R> v, A o) {
      return v.visitIdentifier(this, o);
  }
  
  public Declaration decl;  // resolved in contextual analysis
  public int scopeLevel;   //stores the scope level where the identifier was declared. Useful in restructuring QualifiedRef
  public String searchClass;   //stores the className where the identifier is expected to be found.
                              //Example: Given b.num where b is of type B, searchClass would be "B" for identifier "num"

}
