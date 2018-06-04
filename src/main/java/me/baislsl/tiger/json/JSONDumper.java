package me.baislsl.tiger.json;

import me.baislsl.tiger.CompileException;
import me.baislsl.tiger.structure.Program;
import me.baislsl.tiger.structure.Tiger;
import me.baislsl.tiger.structure.Token;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class JSONDumper {
    private final static Logger logger = LoggerFactory.getLogger(JSONDumper.class);

    public static void dump(Program p, Writer writer) throws IOException {
        logger.info("Generating json information");
        JSONObject jo = parseJSONResult(p, "null");
        jo.writeJSONString(writer);
        logger.info("Generating success");
    }

    private JSONDumper() { }


    @SuppressWarnings("unchecked")
    private static JSONObject parseJSONResult(Tiger t, String parent) {
        JSONObject ans = new JSONObject();
        ans.put("parent", parent);
        String name = t.getClass().getSimpleName();
        ans.put("name", name);

        JSONArray children = new JSONArray();

        for (Field f : t.getClass().getFields()) {
//            if (!f.isAccessible()) {
//                continue;
//            }

            try {
                Object target = f.get(t);
                if(target == null) continue;
                if (Tiger.class.isAssignableFrom(target.getClass()) ) {
                    Tiger child = (Tiger) target;
                    children.add(parseJSONResult(child, name));
                } else if (Token.class.isAssignableFrom(target.getClass())) {
                    String value = ((Token) target).name;
                    JSONObject o = new JSONObject();
                    o.put("name", f.getName() + ": " + value);
                    o.put("parent", name);
                    children.add(o);
                } else if(List.class.isAssignableFrom(target.getClass())) {
                    List list = (List)target;
                    for(Object c : list) {
                        children.add(parseJSONResult((Tiger)c, name));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new CompileException(e);
            }

        }
        if (!children.isEmpty()) {
            ans.put("children", children);
        }
        return ans;
    }


}
