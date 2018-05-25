package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.List;

public class LetExp extends Exp {
    public List<Dec> decs;
    public List<Exp> exps;

    // for translate
    public String className;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
