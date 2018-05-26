package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FieldExp;
import org.apache.bcel.generic.Type;

public class ClassFieldSymbol implements FieldSymbol {

    private FieldDec dec;
    private Type type;

    public ClassFieldSymbol(FieldDec dec) {
        this.dec = dec;
        this.type = dec.type();
    }

    public ClassFieldSymbol(FieldDec dec, Type type) {
        this.dec = dec;
        this.type = type;
    }

    @Override
    public boolean isLocalVariable() {
        return false;
    }

    public Type type(){
        return type;
    }

    @Override
    public String name() {
        return dec.id.name;
    }
}
