package me.baislsl.tiger;

import me.baislsl.tiger.json.JSONtranslate;
import me.baislsl.tiger.structure.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class TigerCompiler {
    private static final String DEFAULT_PATH = "Tiger";
    private static final Logger logger = LoggerFactory.getLogger(TigerCompiler.class);

    public static void compile(InputStream in) {
        logger.info("Load json source");
        compile(JSONtranslate.load(in), DEFAULT_PATH);
    }

    public static void compile(InputStream in, String path) {
        compile(JSONtranslate.load(in), path);
    }

    private static void compile(Program program, String target) {
        logger.info("Compiling");
        program.accept(new TypeVisitor());  // get type information
        ProgramMainGen.gen(program, target);
        logger.info("Compile Success");
        logger.info("The source has been compile to target/classes/" + target + ".class");
        logger.info("Use \"java " + target + "\" in the correct directory to run your tiger program under jvm platform");
    }


}
