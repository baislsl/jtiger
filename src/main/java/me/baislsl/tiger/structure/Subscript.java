package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Subscript extends Lvalue {
    // lvalue[exp]
    public Lvalue lvalue;
    public Exp exp;


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
