package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class TyDec extends Dec {
    public Token type;
    public Token tyId;
    public Token equal;

    public boolean isSingleTypeId = false;
    public Token tyOfTyId; // use when isSingleTypeId is true;
                            // type any = int

    // ty of tyId
    public Ty ty;

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }

}
