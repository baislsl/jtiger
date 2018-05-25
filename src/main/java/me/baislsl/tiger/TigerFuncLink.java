package me.baislsl.tiger;

import java.util.Scanner;

public class TigerFuncLink {
    private static Scanner scanner;
    private static final int MAX_ASCII_INDEX = 255;

    static {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("");   // read in every char as string
    }

    private TigerFuncLink() {
    }

    public static void print(String s) {
        System.out.println(s);
    }


    public static void flush() {
        System.out.flush();
    }

    public static String getChar() {
        return scanner.next();
    }

    public static int ord(String s) {
        return s.isEmpty() ? -1 : s.charAt(0);
    }


    public static String chr(int i) {
        if (i > MAX_ASCII_INDEX) {
            throw new RuntimeException("Tiger chr : i out of range");
        }
        return String.valueOf((char) (i));
    }

    public static int size(String s) {
        return s.length();
    }

    public static String subString(String s, int first, int n) {
        return s.substring(first, first + n);
    }

    public static String concat(String s1, String s2) {
        return s1 + s2;
    }

    public static int not(int integer) {
        return integer == 0 ? 1 : 0;
    }

    public static void exit(int i) {
        System.exit(i);
    }
}
