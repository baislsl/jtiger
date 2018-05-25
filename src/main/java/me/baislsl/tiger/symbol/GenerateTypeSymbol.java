package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.LetExp;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public class GenerateTypeSymbol implements TypeSymbol {

    private String name;

    public GenerateTypeSymbol(LetExp letExp) {
        this.name = letExp.className;
    }

    public GenerateTypeSymbol(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return new ObjectType(name);
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }
}
