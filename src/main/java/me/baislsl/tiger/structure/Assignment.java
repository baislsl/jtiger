package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Assignment extends Exp {
    public Lvalue lv;
    public Exp exp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
