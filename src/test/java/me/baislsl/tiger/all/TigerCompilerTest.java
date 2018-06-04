package me.baislsl.tiger.all;

import me.baislsl.tiger.TigerCompiler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TigerCompilerTest {

    @Test
    void compile() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/1.json"),
                "CompileTest");
    }

    @Test
    void compileMerge(){
        TigerCompiler.compile(this.getClass().getResourceAsStream("/json_out/merge.tig.json")
            , "Merge");

    }



}