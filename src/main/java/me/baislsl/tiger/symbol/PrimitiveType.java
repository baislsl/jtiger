package me.baislsl.tiger.symbol;

import org.apache.bcel.generic.Type;

public class PrimitiveType implements TypeSymbol {


    private Type type;

    private PrimitiveType(Type type) {
        this.type = type;
    }

    public static PrimitiveType INT = new PrimitiveType(Type.INT);
    public static PrimitiveType STRING = new PrimitiveType(Type.STRING);

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String name() {
        return type.equals(Type.INT) ? "int" : "string";
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }
}
