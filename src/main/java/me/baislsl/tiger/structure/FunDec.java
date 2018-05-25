package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunDec extends Dec implements Tiger {
    public Token function;
    public Token id;
    public List<FieldDec> decs = new ArrayList<>();

    @Nullable
    public Token tyid;

    public Token equal;
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
