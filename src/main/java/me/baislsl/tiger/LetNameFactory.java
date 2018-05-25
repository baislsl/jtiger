package me.baislsl.tiger;

public class LetNameFactory {

    private static int id = 0;

    public synchronized static String newLetName() {
        return "LetClass" + id++;
    }
}
