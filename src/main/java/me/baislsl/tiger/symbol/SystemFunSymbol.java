package me.baislsl.tiger.symbol;

import me.baislsl.tiger.TigerFuncLink;
import org.apache.bcel.generic.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SystemFunSymbol implements FunSymbol {
    private String name;
    private Type retType;
    private List<Type> paramsType;

    private SystemFunSymbol() {
    }

    private SystemFunSymbol(String name, Type retType, List<Type> paramsType) {
        this.name = name;
        this.retType = retType;
        this.paramsType = paramsType;
    }

    public static List<SystemFunSymbol> symbols = new ArrayList<>();

    static {
        for (Method m : TigerFuncLink.class.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && !m.getName().equals("<clinit>")) {
                String name = m.getName();
                Type ret = convert(m.getReturnType());
                List<Type> params = new ArrayList<>();
                for (Class<?> c : m.getParameterTypes()) {
                    params.add(convert(c));
                }
                symbols.add(new SystemFunSymbol(name, ret, params));
            }
        }
    }

    private static Type convert(Class<?> clazz) {
        if (clazz.equals(void.class)) {
            return Type.VOID;
        } else if (clazz.equals(String.class)) {
            return Type.STRING;
        } else if (clazz.equals(int.class)) {
            return Type.INT;
        } else {
            throw new RuntimeException("No type as " + clazz);
        }
    }

    @Override
    public boolean isSystemFunc() {
        return true;
    }

    @Override
    public Type retType() {
        return retType;
    }

    @Override
    public List<Type> paramsType() {
        return paramsType;
    }

    @Override
    public String name() {
        return name;
    }
}
