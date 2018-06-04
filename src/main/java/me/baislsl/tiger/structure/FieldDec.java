package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class FieldDec extends Dec implements Tiger {
    public Token id;
    public Token tyId;
    public Type type;

    public FieldDec(){}

    public FieldDec(Token id, Token tyId) {
        this.id = id;
        this.tyId = tyId;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

    public Token getId() {
        return id;
    }

    public Token getTyId() {
        return tyId;
    }
}
