package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IdOnlyLvalue extends Lvalue {
    public Token token;

    public IdOnlyLvalue(){}

    public IdOnlyLvalue(Token token) {
        this.token = token;
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
