package me.baislsl.tiger;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class TypeClassGen {
    private ClassGen cg;
    private ConstantPoolGen cp;
    private InstructionFactory factory;
    private Map<String, String> fields;

    private TypeClassGen(String name, Map<String, String> fields) {
        this.fields = fields;
        cg = new ClassGen(name,
               // "java.lang.Object",
                TigerObject.class.getName(),
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


    /**
     * generate *.class file of the target object class
     *
     * @param name   new type name
     * @param fields field-name -> type
     *               type can be int, string  and new type declared
     * @param path   where the *.class file will be saved
     */

    public static void produce(String name, Map<String, String> fields, String path) {
        TypeClassGen factory = new TypeClassGen(name, fields);
        factory.addFieldDeclare();
        factory.addInit();
        JavaClass javaClass = factory.cg.getJavaClass();
        try {
            javaClass.dump(new BufferedOutputStream(new FileOutputStream(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
