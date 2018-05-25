package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.List;

public class SeqExp extends Exp {
    public List<Exp> exps;


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
