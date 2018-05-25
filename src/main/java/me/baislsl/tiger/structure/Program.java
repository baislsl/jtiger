package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Program implements Tiger{
    public  Exp exp;
    @Override
    public Type type() {
        return exp.type();
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
