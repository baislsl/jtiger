package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.Dec;
import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FieldExp;
import me.baislsl.tiger.structure.VarDec;
import org.apache.bcel.generic.Type;

import java.util.Objects;

public class ClassFieldSymbol implements FieldSymbol {

    private Dec dec;
    private Type type;
    private String name;

    public ClassFieldSymbol(FieldDec dec) {
        this.dec = dec;
        this.name = dec.id.name;
        this.type = dec.type();
        Objects.requireNonNull(type, dec.id.name);
    }

    public ClassFieldSymbol(VarDec dec) {
        this.dec = dec;
        this.type = dec.type();
        this.name = dec.id.name;
        Objects.requireNonNull(type, name);
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
        return name;
    }
}
