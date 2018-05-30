package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class ArrCreate extends Exp {
    // tyId [ exp1 ] of exp2
    public Token tyId;
    public Exp exp1;
    public Exp exp2;

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
