package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class ForExp extends Exp implements Tiger {
    public Token id;
    public Exp fromExp, toExp, doExp;
    @Override
    public Type type() {
        return type;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
