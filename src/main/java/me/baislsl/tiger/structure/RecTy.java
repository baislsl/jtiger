package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.List;

public class RecTy extends Ty {
    public List<FieldDec> decs = new ArrayList<>();


    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
