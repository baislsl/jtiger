package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.FieldDec;
import org.apache.bcel.generic.Type;

public class ClassFieldSymbol implements FieldSymbol {

    private FieldDec dec;

    public ClassFieldSymbol(FieldDec dec) {
        this.dec = dec;
    }

    @Override
    public boolean isLocalVariable() {
        return false;
    }

    public Type type(){
        return dec.type();
    }

    @Override
    public String name() {
        return dec.id.name;
    }
}
