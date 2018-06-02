package me.baislsl.tiger.json;

import org.json.simple.parser.JSONParser;
import me.baislsl.tiger.CompileException;
import me.baislsl.tiger.structure.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


public class JSONtranslate {

    private final static Class<?>[] clazzes = new Class[]{
            ArrCreate.class, ArrTy.class, Assignment.class, BreakExp.class,
            Call.class, FieldCreate.class, FieldDec.class, FieldExp.class,
            ForExp.class, FunDec.class, IdOnlyLvalue.class, IdOnlyTy.class, IfThen.class,
            IfThenElse.class, InfixExp.class, IntLit.class, LetExp.class, Negation.class,
            Nil.class, Program.class, RecCreate.class, RecTy.class, SeqExp.class,
            StringLit.class, Subscript.class, Token.class, TyDec.class, VarDec.class,
            While.class
    };

    public static Program load(InputStream path) {
        return load(new BufferedReader(new InputStreamReader(path)));
    }

    public static Program load(Reader reader) {
        JSONObject jo;
        try {
            jo = (JSONObject) (new JSONParser().parse(reader));
        } catch (Exception e) {
            throw new CompileException(e);
        }
        return load(jo);
    }


    public static Program load(JSONObject object) {
        try {
            return loadJSONObject(object, Program.class);
        } catch (ReflectiveOperationException e) {
            throw new CompileException(e);
        }
    }

    private static Object loadJSONObject(JSONObject object)
            throws ReflectiveOperationException {
        String name = (String) object.get("class");
        for (Class<?> clazz : clazzes) {
            if (clazz.getSimpleName().equals(name)) {
                return loadJSONObject(object, clazz);
            }
        }
        throw new ReflectiveOperationException("Can not find class for " + name);

    }


    private static <T> T loadJSONObject(JSONObject object, Class<T> clazz)
            throws ReflectiveOperationException {
        T ans = clazz.getConstructor().newInstance();
        for (Object o : object.keySet()) {
            String key = (String) o;
            boolean flag = false;
            for (Field f : clazz.getFields()) {
                f.setAccessible(true);
                if (f.getName().equals(key)) {
                    Class<?> fc = f.getType();
                    if (fc.isAssignableFrom(List.class)) {
                        ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
                        Class<?> elementType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                        f.set(ans, loadJSONArray((JSONArray) object.get(key), elementType));
                    } else if (fc == Token.class) {
                        f.set(ans, new Token((String) object.get(key)));
                    } else {
                        f.set(ans, loadJSONObject((JSONObject) object.get(key)));
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag && !key.equals("class")) {
                throw new RuntimeException("Error parse for " + key);
            }
        }
        return ans;
    }


    private static <T> List<T> loadJSONArray(JSONArray array, Class<T> clazz)
            throws ReflectiveOperationException {
        List<T> ans = new ArrayList<>();
        for (Object o : array) {
            JSONObject jo = (JSONObject) o;
            ans.add(clazz.cast(loadJSONObject(jo)));
        }
        return ans;
    }


}
