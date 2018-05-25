package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class FieldDec implements Tiger {
    public Token id;
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
