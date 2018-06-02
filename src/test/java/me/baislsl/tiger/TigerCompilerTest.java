package me.baislsl.tiger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TigerCompilerTest {

    @Test
    void compile() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/1.json"), "CompileTest");
    }
}