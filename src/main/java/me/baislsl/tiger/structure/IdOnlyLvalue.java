package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IdOnlyLvalue extends Lvalue {
    public Token id;

    public IdOnlyLvalue(){}

    public IdOnlyLvalue(Token id) {
        this.id = id;
    }

    @Override
    public Type type() {
        return type;
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
