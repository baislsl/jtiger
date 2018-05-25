package me.baislsl.tiger;

import me.baislsl.tiger.structure.Program;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.io.IOException;

// entry for program

/**
 * generate entry:
 *
 * public class Tiger{
 * public static void main(String[] args) {
 * // exp
 * }
 * }
 */


public class ProgramMainGen {

    private static String DEFAULT_CLASSNAME = "Tiger";

    public static void gen(Program p ){
        gen(p, DEFAULT_CLASSNAME);
    }

    public static void gen(Program p, String className) {
        ClassGen cg = new ClassGen(className, "java.lang.Object",
                "<generated>", Const.ACC_PUBLIC |
                Const.ACC_SUPER,
                null);
        ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Const.ACC_STATIC |
                Const.ACC_PUBLIC,// access flags
                Type.VOID,              // return type
                new Type[]{            // argument types
                        new ArrayType(Type.STRING, 1)
                },
                new String[]{"argv"}, // arg names
                "main", className,    // method, class
                il, cp);
        TigerEnv env = new TigerEnv();
        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);

        p.exp.accept(visitor);
        if (p.type() != null) {
            il.append(InstructionConst.POP);
        }
        il.append(InstructionConst.RETURN);
        mg.setMaxStack();   // TODO:
        cg.addMethod(mg.getMethod());
        il.dispose();
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
        try {
            cg.getJavaClass().dump(Util.classPath + className + ".class");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
