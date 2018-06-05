package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.List;

public class Call extends Exp {
    public Token id;
    public List<Exp> exps = new ArrayList<>();

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
