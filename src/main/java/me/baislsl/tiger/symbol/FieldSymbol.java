package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.FieldDec;
import org.apache.bcel.generic.Type;

public class FieldSymbol implements Symbol {

    private FieldDec dec;

    public FieldSymbol(FieldDec dec) {
        this.dec = dec;
    }

    public Type type(){
        return dec.type();
    }

    @Override
    public String name() {
        return dec.id.name;
    }
}
