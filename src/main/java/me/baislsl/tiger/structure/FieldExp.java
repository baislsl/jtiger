package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class FieldExp extends Lvalue {
    // true for lv -> id
    // false for lv -> subscript | fieldExp
    public boolean lvOnlyId;

    // lv -> subsript | fieldExp
    public Lvalue lvalue;

    // lv -> id
    public Token lvId;

    public Token point;
    public Token id;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
