package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.FieldDec;
import me.baislsl.tiger.structure.FunDec;
import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.List;

public class FunSymbol extends Symbol {
    private FunDec dec;

    public FunSymbol(FunDec dec) {

    }

    public Type retType() {
        return dec.type();
    }

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
