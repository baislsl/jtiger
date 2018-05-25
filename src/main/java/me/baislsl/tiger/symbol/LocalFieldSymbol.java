package me.baislsl.tiger.symbol;

import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.Type;

public class LocalFieldSymbol implements FieldSymbol {

    private LocalVariableGen lg;

    public LocalFieldSymbol(LocalVariableGen lg) {
        this.lg = lg;
    }

    @Override
    public boolean isLocalVariable() {
        return true;
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
