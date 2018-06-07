package me.baislsl.tiger.all;

import me.baislsl.tiger.TigerCompiler;
import org.junit.jupiter.api.Test;

class TigerCompilerTest {

    @Test
    void compile() {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/1.tig.json"),
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

    @Test
    void compileQuickSort(){
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/qsort.tig.json"),
                "QuickSort");
    }

    @Test
    void compileBinSearch(){
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/bsearch.tig.json"),
                "Bsearch");
    }




}