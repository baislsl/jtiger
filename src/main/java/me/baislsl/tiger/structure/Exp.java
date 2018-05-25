package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public abstract class Exp implements Tiger {
    public Type type;

    @Override
    public Type type() {
        return type;
    }
}
