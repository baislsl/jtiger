package me.baislsl.tiger.structure;

public class BreakExp extends Exp {
    @Override
    public void accept(TigerVisitor visitor) {
        visitor.visit(this);
    }
}
