package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class StringLit extends Exp {
    public String value;
    {
        type = Type.STRING;
    }

    public StringLit(String value){
        this.value = value;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }


}
