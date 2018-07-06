package me.baislsl.tiger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JarBuilder.class);
    private static final String DEFAULT_PATH = "tiger.jar";
    private static List<File> files = new ArrayList<>();
    private static String mainClass;

    private JarBuilder() {
    }

    public static void setMainClass(String mainClass) {
        JarBuilder.mainClass = mainClass;
    }

    public static void clear() {
        files.clear();
    }

    public static void add(File f) {
        files.add(f);
    }

    public static void dump() throws IOException {
        dump(DEFAULT_PATH);
    }

    public static void dump(String jarName) throws IOException {
        logger.info("Generating executable jar file");
        FileOutputStream fos = new FileOutputStream(jarName);

        JarOutputStream jarOutputStream = new JarOutputStream(fos);
        BufferedOutputStream out = new BufferedOutputStream(jarOutputStream);
        jarOutputStream.putNextEntry(new JarEntry("META-INF/MANIFEST.MF"));
        jarOutputStream.write(("Main-Class: " + mainClass + "\n").getBytes("UTF8"));

        jarOutputStream.putNextEntry(new JarEntry("me/baislsl/tiger/TigerFuncLink.class"));
        int i;
        InputStream fo = new BufferedInputStream(new FileInputStream("./target/classes/me/baislsl/tiger/TigerFuncLink.class"));
        while ((i = fo.read()) != -1) {
            jarOutputStream.write(i);
        }
        fo.close();

        for (File f : files) {
            logger.info("Write {} into jar", f.getName());
            JarEntry jarAdd = new JarEntry(f.getName());
            jarAdd.setTime(f.lastModified());
            jarOutputStream.putNextEntry(jarAdd);
            InputStream in = new BufferedInputStream(new FileInputStream(f.getAbsoluteFile()));
            int cc;
            while ((cc = in.read()) != -1) {
                jarOutputStream.write(cc);
            }
            in.close();
        }
        out.close();
        jarOutputStream.close();
        logger.info("Generate executable jar file {} success", jarName);
    }

}
