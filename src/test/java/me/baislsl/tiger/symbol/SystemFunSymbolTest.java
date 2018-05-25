package me.baislsl.tiger.symbol;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class SystemFunSymbolTest {
    private final static Logger logger = LoggerFactory.getLogger(SystemFunSymbolTest.class);
    @Test
    void all() throws Exception {
        for(FunSymbol s : SystemFunSymbol.symbols) {
            logger.info(s.name());
        }
        assertEquals(10, SystemFunSymbol.symbols.size());
    }

}