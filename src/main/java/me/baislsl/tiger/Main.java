package me.baislsl.tiger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static me.baislsl.tiger.TigerCompiler.DEFAULT_PATH;

public class Main {
    public final static Logger logger = LoggerFactory.getLogger(Main.class);

    private static String getJSONOutputPath(String path) {
        return path + ".json";
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            logger.info("Please input source code path");
        }
        for (String codePath : args) {
            logger.info("Compile {} ...", codePath);
            String jsonPath = getJSONOutputPath(codePath);
            logger.info("Parsing {} ...", codePath);
            try {   // use c++ main executable file to compile code into json
                new ProcessBuilder("./main")
                        .directory(new File("absyn"))
                        .redirectInput(new File(codePath))
                        .redirectOutput(new File(jsonPath))
                        .start();
            } catch (IOException e) {
                logger.error("Error parsing code");
            }
            logger.info("Generating json parse result in " + new File(jsonPath).getAbsolutePath());
            String path;
            try {
                path = TigerCompiler.compile(new FileInputStream(jsonPath));
                logger.info("Compile Success!");
            } catch (FileNotFoundException e) {
                logger.error("Can not find file", e);
                return;
            }

            logger.info("程序现在默认被直接执行，请输入（如果有），你也可以在{}中手动执行java Tiger运行程序",
                    new File(path).getParent());
            try {
                Process process = new ProcessBuilder("java", DEFAULT_PATH)
                        .directory(new File("target/classes"))
                        .inheritIO()
                        .start();
                // wait for the program to finish
                process.waitFor();
            } catch (IOException|InterruptedException e) {
                logger.error("Error run program", e);
            }

        }
    }
}
