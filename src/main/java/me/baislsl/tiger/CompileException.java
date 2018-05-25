package me.baislsl.tiger;

public class CompileException extends RuntimeException {
    public CompileException() {
        super();
    }

    public CompileException(String message) {
        super(message);
    }

    public CompileException(Exception e) {
        super(e);
    }

    public CompileException(String message, Exception e) {
        super(message, e);
    }
}
