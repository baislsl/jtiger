package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeClassGen {
    private ClassGen cg;
    private ConstantPoolGen cp;
    private InstructionFactory factory;
    private Map<String, String> fields;

    private TypeClassGen(String name, Map<String, String> fields) {
        this.fields = fields;
        cg = new ClassGen(name,
                "java.lang.Object",
                name + ".class",
                Const.ACC_PUBLIC,
                new String[]{}
        );
        cp = cg.getConstantPool();
        factory = new InstructionFactory(cp);
    }

    private void addFieldDeclare() {
        for (Map.Entry<String, String> e : fields.entrySet()) {
            FieldGen fg = new FieldGen(Const.ACC_PUBLIC,
                    new ObjectType(e.getValue()),
                    e.getKey(), cp);
            cg.addField(fg.getField());
        }
    }


    private void addInit() {
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
    }


    public static void generateClass(TigerEnv env, TyDec tyDec) {
        Map<String, String> fields = new HashMap<>();
        if (tyDec.ty instanceof IdOnlyTy) {
            // TODO: test  for type a = b
            IdOnlyTy idty = (IdOnlyTy)tyDec.ty;
            env.getTypeTable().put(tyDec.tyId.name, env.getTypeTable().query(idty.id.name).symbol);
        } else {
            if (tyDec.ty instanceof RecTy) {
                RecTy recTy = (RecTy) tyDec.ty;
                for (FieldDec f : recTy.decs) {
                    fields.put(f.id.name, f.tyId.name);
                }
                generateClass(tyDec.tyId.name, fields);
            } else {
                throw new CompileException("Unsupported to compile arrTy");
            }
        }
    }

    /**
     * generate *.class file of the target object class
     *
     * @param name   new type name
     * @param fields field-name -> type
     *               type can be int, string  and new type declared
     */

    public static void generateClass(String name, Map<String, String> fields) {
        TypeClassGen factory = new TypeClassGen(name, fields);
        factory.addFieldDeclare();
        factory.addInit();
        JavaClass javaClass = factory.cg.getJavaClass();
        try {
            javaClass.dump(JVMSpec.classPath + name + ".class");
        } catch (IOException e) {
            throw new CompileException(e);
        }
    }


}
