package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.TyDec;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public class UserTypeSymbol implements TypeSymbol {

    private TyDec dec;

    public UserTypeSymbol(TyDec dec) {
        this.dec = dec;
    }

    @Override
    public String name() {
        return dec.tyId.name;
    }

    @Override
    public Type type() {
        return new ObjectType(dec.tyId.name);
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }
}
