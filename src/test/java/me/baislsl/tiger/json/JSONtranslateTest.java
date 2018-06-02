package me.baislsl.tiger.json;

import me.baislsl.tiger.structure.Program;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONtranslateTest {

    @Test
    void load() {
        Program p = JSONtranslate.load(getClass().getResourceAsStream("/1.json"));



        assertNotNull(p.exp);

    }
}