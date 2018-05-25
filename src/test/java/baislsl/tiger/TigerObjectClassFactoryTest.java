package baislsl.tiger;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TigerObjectClassFactoryTest {

    @Test
    void produce() throws Exception {
        String path = "/home/baislsl/java/tiger/intlist.class";
        Map<String, String> map = new HashMap<>();
        map.put("hd", "int");
        map.put("t1", "intlist");
        TigerObjectClassFactory.produce("intlist", map, "intlist.class");
//        File file = new File(path);
//        URL localJar = file.toURI().toURL();
//        URLClassLoader cl = new URLClassLoader(new URL[]{  localJar });
//         Class<?> clazz = cl.loadClass("baislsl.tiger.intlist");
    }

    @Test
    void produceLoop() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key", "int");
        map.put("t1", "treelist");
        TigerObjectClassFactory.produce("tree", map, "tree.class");
        map.clear();
        map.put("hd", "tree");
        map.put("t1", "treelist");
        TigerObjectClassFactory.produce("treelist", map, "treelist.class");
    }
}