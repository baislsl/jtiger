package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class While extends Exp {
    public Exp whileExp, doExp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
