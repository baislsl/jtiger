package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FunDec;
import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.List;

public class DecFunSymbol implements FunSymbol {
    private FunDec dec;

    public DecFunSymbol(FunDec dec) {
        this.dec = dec;
    }

    @Override
    public boolean isSystemFunc() {
        return false;
    }

    @Override
    public Type retType() {
        return dec.type();
    }

    @Override
    public List<Type> paramsType() {
        List<Type> types = new ArrayList<>();
        for (FieldDec f : dec.decs) {
            types.add(f.type());
        }
        return types;
    }

    @Override
    public String name() {
        return dec.id.name;
    }

    @Override
    public String toString() {
        return name();
    }
}
