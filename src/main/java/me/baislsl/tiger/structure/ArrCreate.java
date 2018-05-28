package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class ArrCreate extends Exp {
    // tyId [ exp1 ] of exp2
    public Token tyId;
    public Token exp;
    public Token exp1;

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
