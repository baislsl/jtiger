package me.baislsl.tiger;

import me.baislsl.tiger.structure.LetExp;
import me.baislsl.tiger.structure.Program;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgramMainGenTest {

    @Test
    void gen() {
        Program p = new Program();
        p.exp = new LetExp();
        ProgramMainGen.gen(p);
    }
}