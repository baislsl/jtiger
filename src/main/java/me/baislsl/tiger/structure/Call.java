package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.List;

public class Call extends Exp {
    public Token id;
    public List<Exp> exps;

    @Override
    public Type type() {
        // TODO:
        return null;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
