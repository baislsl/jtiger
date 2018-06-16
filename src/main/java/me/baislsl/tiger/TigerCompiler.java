package me.baislsl.tiger;

import me.baislsl.tiger.json.JSONDumper;
import me.baislsl.tiger.json.JSONtranslate;
import me.baislsl.tiger.structure.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;

public class TigerCompiler {
    public static final String DEFAULT_PATH = "Tiger";
    private static final Logger logger = LoggerFactory.getLogger(TigerCompiler.class);

    public static String compile(InputStream in) {
        logger.info("Load json source");
        return compile(JSONtranslate.load(in), DEFAULT_PATH);
    }

    public static String compile(InputStream in, String path) {
        return compile(JSONtranslate.load(in), path);
    }

    private static String compile(Program program, String target) {
        logger.info("Generating infovis ...");
        try {
            Writer writer = new FileWriter("infovis/static/data.js");
            writer.write("var treeData = [");
            JSONDumper.dump(program, writer);
            writer.write("]\n");
            writer.close();
            logger.info("可视化结果见 {}",
                    new File("infovis/static/index.html").getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error generating infovis", e);
        }
        program.accept(new TypeVisitor());  // get type information
        String path = ProgramMainGen.gen(program, target);
        logger.info("Compile Success");
        logger.info("The source has been compile to " + new File(path).getAbsolutePath());
        return path;
    }

}
