package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IfThenElse extends Exp {
    public Exp ifExp, thenExp, elseExp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
