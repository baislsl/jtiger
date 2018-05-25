package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.List;

public class LetExp extends Exp {
    public List<Dec> decs = new ArrayList<>();
    public List<Exp> exps = new ArrayList<>();

    // for translate
    public String className;

    @Override
    public Type type() {
        if(exps.isEmpty()) {
            return Type.VOID;
        }
        return exps.get(exps.size() - 1).type();
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
