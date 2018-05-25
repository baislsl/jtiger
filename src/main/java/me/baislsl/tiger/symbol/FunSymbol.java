package me.baislsl.tiger.symbol;

import org.apache.bcel.generic.Type;

import java.util.List;

public interface FunSymbol extends Symbol {
    Type retType();

    List<Type> paramsType();

    boolean isSystemFunc();
}
