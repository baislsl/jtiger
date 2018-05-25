package me.baislsl.tiger.symbol;

import me.baislsl.tiger.structure.LetExp;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public class GenerateTypeSymbol implements TypeSymbol {

    private LetExp letExp;

    public GenerateTypeSymbol(LetExp letExp) {
        this.letExp = letExp;
    }

    @Override
    public String name() {
        return letExp.className;
    }

    @Override
    public Type type() {
        return new ObjectType(letExp.className);
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }
}
