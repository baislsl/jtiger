package me.baislsl.tiger.json;

import me.baislsl.tiger.structure.Program;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JSONDumperTest {

    @Test
    void dump() throws Exception {
        Writer writer = new FileWriter("infovis/static/data.js");
        writer.write("var treeData = [");
        Program p = JSONtranslate.load(getClass().getResourceAsStream("/json_out/merge.tig.json"));
        JSONDumper.dump(p, writer);
        writer.write("]\n");
        writer.close();

    }
}