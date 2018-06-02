package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class StringLit extends Exp {
    public Token value;
    {
        type = Type.STRING;
    }

    public StringLit() {
        this.value = new Token("");
    }

    public String value() {
        return value.name;
    }

    public StringLit(String value){
        this.value = new Token(value);
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }


}
