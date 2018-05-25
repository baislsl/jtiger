package me.baislsl.tiger.structure;

public class Token {
    public  String name;

    public Token(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
