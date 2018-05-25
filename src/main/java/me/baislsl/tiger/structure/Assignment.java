package me.baislsl.tiger.structure;

import org.apache.bcel.generic.Type;

public class Assignment extends Exp {
    // true for lv -> id
    // false for lv -> subscript | fieldExp
    public boolean lvOnlyId;


    // lv -> subscript | fieldExp
    public Lvalue lv;
    // lv -> id |
    public Token lvid;

    public Token assign;
    public Exp exp;

    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
