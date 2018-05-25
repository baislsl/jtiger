package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Negation extends Exp {
    public Token neg;
    public Exp exp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
