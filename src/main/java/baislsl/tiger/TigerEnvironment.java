package baislsl.tiger;

import java.util.Scanner;

public final class TigerEnvironment {
    private static Scanner scanner;
    private static final int MAX_ASCII_INDEX = 255;

    static {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("");   // read in every char as string
    }

    private TigerEnvironment() { }

    public static void print(TigerString s) {
        System.out.println(s.getValue());
    }


    public static void flush() {
        System.out.flush();
    }

    public static TigerString getChar() {
        return new TigerString(scanner.next());
    }

    public static int ord(TigerString s) {
        return s.getValue().isEmpty() ? -1 : s.getValue().charAt(0);
    }


    public static TigerString chr(TigerInteger i) {
        if (i.getValue() > MAX_ASCII_INDEX) {
            throw new RuntimeException("Tiger chr : i out of range");
        }
        return new TigerString(String.valueOf((char) (i.getValue())));
    }

    public static TigerString subString(TigerString s, TigerInteger first, TigerInteger n) {
        return new TigerString(s.getValue().substring(first.getValue(), n.getValue()));
    }

    public static TigerString concat(TigerString s1, TigerString s2) {
        return new TigerString(s1.getValue() + s2.getValue());
    }

    public static TigerInteger not(TigerInteger integer) {
        return integer.getValue() == 0 ? new TigerInteger(1) : new TigerInteger(0);
    }

    public static void exit(int i) {
        System.exit(i);
    }


}
