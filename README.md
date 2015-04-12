# miniJava-compiler
Java compiler for a subset of the Java language

Note-worthy Java features not supported:
1. Abstract classes
2. Inheritance
3. Interfaces

This compiler will accept an input file ending in .java, run it through syntactical analysis, semantic analysis, contextual analysis, and finally through code generation.

This compiler is a multi-pass LALR(1) compiler.