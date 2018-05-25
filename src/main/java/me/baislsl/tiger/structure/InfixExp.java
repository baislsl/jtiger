package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class InfixExp extends Exp {
    public Exp exp1, exp2;
    public Token infixOp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
