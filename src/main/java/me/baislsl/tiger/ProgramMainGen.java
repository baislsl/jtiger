package me.baislsl.tiger;

import me.baislsl.tiger.structure.Program;
import me.baislsl.tiger.symbol.GenerateTypeSymbol;
import org.apache.bcel.Const;
import org.apache.bcel.generic.*;

import java.io.IOException;

// entry for program

/**
 * generate entry:
 *
 * public class Tiger{
 * public static void main(String[] args) {
 *   new Tiger().invoke();
 * }
 *
 * public void invoke(){
 *
 * }
 *
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
        cg.addEmptyConstructor(Const.ACC_PUBLIC);

        // main()
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
        InstructionFactory factory = new InstructionFactory(cp);
        il.append(factory.createNew(className));
        il.append(InstructionConst.DUP);
        il.append(factory.createInvoke(className, "<init>", Type.VOID, Type.NO_ARGS,
                Const.INVOKESPECIAL));
        il.append(factory.createInvoke(className, JVMSpec.invokeFuncName, Type.VOID, Type.NO_ARGS,
                Const.INVOKEVIRTUAL));
        il.append(InstructionConst.RETURN);
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());

        // invoke()
        il = new InstructionList();
        mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[]{},
                JVMSpec.invokeFuncName, className, il, cp);
        TigerEnv env = new TigerEnv();
        env.getTypeTable().put(className, new GenerateTypeSymbol(className));
        TigerVisitorImpl visitor = new TigerVisitorImpl(env, cg, mg, il);
        p.exp.accept(visitor);
        if (p.exp.type() != Type.VOID) {
            il.append(InstructionConst.POP);
        }
        il.append(InstructionConst.RETURN);
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
        il.dispose();
        try {
            cg.getJavaClass().dump(JVMSpec.classPath + className + ".class");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
