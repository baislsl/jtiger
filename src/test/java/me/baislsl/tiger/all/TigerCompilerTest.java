package me.baislsl.tiger.all;

import me.baislsl.tiger.TigerCompiler;
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
        TigerCompiler.compile(this.getClass().getResourceAsStream("/json_out/merge.tig.json")
                , "Merge");
    }

    @Test
    void compileTest6() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/json_out/test6.tig.json"));
    }


}