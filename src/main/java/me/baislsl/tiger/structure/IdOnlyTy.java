package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IdOnlyTy extends Ty {
    public Token id;


    @Override
    public Type type() {
        return null;
    }


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return id.name;
    }
}
