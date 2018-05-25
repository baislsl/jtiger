package me.baislsl.tiger.symbol;

import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.Type;

public class LocalSymbol extends Symbol {

    private LocalVariableGen lg;

    public LocalSymbol(LocalVariableGen lg) {
        this.lg = lg;
    }

    public Type type() {
        return lg.getType();
    }

    public int index() {
        return lg.getIndex();
    }


    @Override
    public String name() {
        return lg.getName();
    }
}
