package me.baislsl.tiger;

import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeClassGenTest {

    @Test
    void produce() {
        String name = "List";
        Map<String, Type> member = new HashMap<>();
        member.put("element", Type.INT);
        member.put("next", new ObjectType("List"));
        TypeClassGen.generateClass(name, member);
    }
}