package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Nil extends Exp {

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
