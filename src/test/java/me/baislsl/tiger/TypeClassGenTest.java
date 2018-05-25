package me.baislsl.tiger;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeClassGenTest {

    @Test
    void produce() {
        String name = "List";
        Map<String, String> member = new HashMap<>();
        member.put("element", "int");
        member.put("next", "List");
        TypeClassGen.produce(name, member, "./target/classes/List.class");
    }
}