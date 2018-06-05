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
    private Map<String, Type> fields;

    private TypeClassGen(String name, Map<String, Type> fields) {
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
        for (Map.Entry<String, Type> e : fields.entrySet()) {
            FieldGen fg = new FieldGen(Const.ACC_PUBLIC,
                    e.getValue(),
                    e.getKey(), cp);
            cg.addField(fg.getField());
        }
    }


    private void addInit() {
        cg.addEmptyConstructor(Const.ACC_PUBLIC);
    }


    public static void generateClass(TigerEnv env, TyDec tyDec) {
        Map<String, Type> fields = new HashMap<>();
        if(!(tyDec.ty instanceof RecTy)) {
            throw new CompileException("Can not generate class for non rec type");
        }
        RecTy recTy = (RecTy) tyDec.ty;
        for (FieldDec f : recTy.decs) {
            fields.put(f.id.name, env.getTypeTable().query(f.tyId.name).symbol.type());
        }
        generateClass(tyDec.tyId.name, fields);
    }

    /**
     * generate *.class file of the target object class
     *
     * @param name   new type name
     * @param fields field-name -> type
     *               type can be int, string  and new type declared
     */

    static void generateClass(String name, Map<String, Type> fields) {
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
