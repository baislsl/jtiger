package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.List;

public class RecTy extends Ty {
    public List<FieldDec> decs;


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
