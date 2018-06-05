package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class TyDec extends Dec {
    public Token type;
    public Token tyId;
    public Ty ty;

    @Override
    public Type type() {
        return ty.type();
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
