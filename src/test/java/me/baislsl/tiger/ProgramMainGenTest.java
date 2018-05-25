package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgramMainGenTest {

    @Test
    void gen() {
        Program p = new Program();
        p.exp = new LetExp();
        ProgramMainGen.gen(p);
    }


    @Test
    void helloWorld() {
        Program p = new Program();
        Call c = new Call();
        c.id = new Token("print");
        c.exps.add(new StringLit("Hello World"));
        p.exp = c;
        ProgramMainGen.gen(p, "HelloWorld");
    }
}