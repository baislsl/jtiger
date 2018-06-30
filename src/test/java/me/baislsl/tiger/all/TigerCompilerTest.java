package me.baislsl.tiger.all;

import me.baislsl.tiger.TigerCompiler;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TigerCompilerTest {
    private final static Logger logger = LoggerFactory.getLogger(TigerCompilerTest.class);
    private final static String REDIRECT_PATH = "target/input.txt";

    /**
     * run the generate class, test the result
     *
     * @param mainClassName class name
     * @param input         input string
     * @param output        output
     * @return line number of output
     */
    private int testOutput(String mainClassName, String input, @Nullable String output) throws Exception {
        FileWriter fw = new FileWriter(REDIRECT_PATH);
        fw.write(input);
        fw.close();
        Process proc = new ProcessBuilder(new String[]{"java", mainClassName})
                .directory(new File("target/classes"))
                .redirectInput(new File(REDIRECT_PATH))
                .start();
        logger.info("Exec \"java {}\"", mainClassName);
        InputStream stdin = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        int lineNumber = 0;
        while ((line = br.readLine()) != null) {
            ++lineNumber;
            sb.append(line);
            sb.append("\n");
        }
        logger.info("Output of {}:\n{}", mainClassName, sb.toString());
        if (output != null) assertEquals(output.trim(), sb.toString().trim());
        int exitVal = proc.waitFor();
        logger.info("Process exitValue: {}", exitVal);
        logger.info("Output {} lines", lineNumber);
        return lineNumber;
    }

    @Test
    void compile() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/break.tig.json"),
                "TigerBreak");
        testOutput("TigerBreak", "", "0\n-1\n-2\n-3\n-4\n");
    }

    @Test
    void compileMerge() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/merge.tig.json"),
                "TigerMerge");
        testOutput("TigerMerge", "4 78 100 347 | 33 99 346 =\n",
                "4 33 78 99 100 346 347");
    }

    @Test
    void compileQueens() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/queens.tig.json"),
                "TigerQueens");
        int lineNumber = testOutput("TigerQueens", "", null);
        assertEquals(828, lineNumber);
    }

    @Test
    void compileQuickSort() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/qsort.tig.json"),
                "TigerQuickSort");
        testOutput("TigerQuickSort", "", "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16");
    }

    @Test
    void compileBinSearch() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/bsearch.tig.json"),
                "TigerBsearch");
        testOutput("TigerBsearch", "", "3");
    }

    @Test
    void comiplePrime() throws Exception {
        TigerCompiler.compile(this.getClass().getResourceAsStream("/tiger/prime.tig.json"),
                "TigerPrime");
        testOutput("TigerPrime", "", "0\n1\n1\n0\n1\n1\n1\n1\n0\n1\n0\n");
    }


}