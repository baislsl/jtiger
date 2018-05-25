package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunDec implements Tiger {
    public Token function;
    public Token id;
    public List<FieldDec> decs;

    @Nullable
    public Token tyid;

    public Token equal;
    public Exp exp;

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }


}
