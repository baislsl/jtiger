package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class IfThen extends Exp implements Tiger{
    public Exp ifExp, thenExp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
