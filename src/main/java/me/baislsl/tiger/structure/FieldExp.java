package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class FieldExp extends Exp {
    public Lvalue lvalue;
    public Token point;
    public Token id;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
