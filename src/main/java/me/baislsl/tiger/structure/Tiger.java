package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public interface Tiger {
    Type type();

    void accept(TigerVisitor visitor);
}
