package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IntLit extends Exp {
    public Token value;

    public IntLit() {
    }

    public IntLit(Token value) {
        this.value = value;
    }

    {
        type = Type.INT;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
