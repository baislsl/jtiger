package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.List;

public class RecCreate extends Exp {
    public Token tyId;
    public List<FieldCreate> fieldCreates;


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
