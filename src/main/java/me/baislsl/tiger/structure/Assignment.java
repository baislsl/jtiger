package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Assignment extends Exp {
    public Lvalue lv;
    public Token lvid;
    // public Token assign;
    public Exp exp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
