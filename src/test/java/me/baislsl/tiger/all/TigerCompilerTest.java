package me.baislsl.tiger.all;

import me.baislsl.tiger.TigerCompiler;
import org.apache.bcel.verifier.VerifyDialog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TigerCompilerTest {

    @Test
    void compile() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/1.tiger.json"),
                "CompileTest");
    }

    @Test
    void compileMerge() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/merge.tig.json"),
                "Merge");
    }

    @Test
    void compileQueens() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/queens.tig.json"),
                "Queens");
    }


}