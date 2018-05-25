package me.baislsl.tiger.symbol;

import org.apache.bcel.generic.Type;

public interface TypeSymbol extends Symbol {
    boolean isPrimitiveType();
    Type type();
}
