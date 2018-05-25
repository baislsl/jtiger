package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class ArrTy extends Ty {
    public Token tyId;


    @Override
    public Type type() {
        return null;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
