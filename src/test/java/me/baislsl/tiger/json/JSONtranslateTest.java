package me.baislsl.tiger.json;

import me.baislsl.tiger.structure.Program;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

class JSONtranslateTest {
    private final static Logger logger = LoggerFactory.getLogger(JSONtranslateTest.class);

    @Test
    void load() throws Exception {
        Program p = JSONtranslate.load(getClass().getResourceAsStream("/json_out/test18.tig.json"));
        assertNotNull(p.exp);

    }


    @Test
    void allTest() throws Exception {
        File dir  = new File("./src/test/resources/json_out");
        for (File f : dir.listFiles()) {
            logger.info("Parse json " + f.getName());
            Program p = JSONtranslate.load(new FileInputStream(f));
            assertNotNull(p.exp);
        }

    }
}