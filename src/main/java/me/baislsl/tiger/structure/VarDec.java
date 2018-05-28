package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;
import org.jetbrains.annotations.Nullable;

public class VarDec extends Dec {
    public Token id;
    @Nullable public Token tyId;
    public Exp exp;

    @Override
    public Type type() {
        return exp.type();
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
