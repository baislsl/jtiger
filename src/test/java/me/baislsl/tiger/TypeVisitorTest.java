package me.baislsl.tiger;

import me.baislsl.tiger.json.JSONtranslate;
import me.baislsl.tiger.structure.Program;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeVisitorTest {


    @Test
    void test1() {
        Program p = JSONtranslate.load(getClass().getResourceAsStream("/1.json"));

        TypeVisitor typeVisitor = new TypeVisitor();
        p.accept(typeVisitor);


        assertNotNull(p.exp.type());
    }

}