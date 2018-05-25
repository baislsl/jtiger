package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.TyDec;

public class TypeSymbol extends Symbol {

    private TyDec dec;

    public TypeSymbol(TyDec dec) {
        this.dec = dec;
    }

    @Override
    public String name() {
        return null;
    }
}
