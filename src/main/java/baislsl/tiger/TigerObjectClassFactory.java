package baislsl.tiger;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class TigerObjectClassFactory {
   // private final static String packageHeader = "baislsl.tiger.";
    private final static String packageHeader = "";

    private ClassGen cg;
    private ConstantPoolGen cp;
    private InstructionFactory factory;
    private Map<String, String> fields;

    private TigerObjectClassFactory(String name, Map<String, String> fields) {
        this.fields = fields;
        cg = new ClassGen(packageHeader + name,
                TigerObject.class.getName(),
                name + ".class",
                Const.ACC_PUBLIC,
                new String[]{}
        );
        cp = cg.getConstantPool();
        factory = new InstructionFactory(cp);
    }

    private String typeWrapper(String type) {
        return type.equals("int") ? "TigerInteger"
                : type.equals("string") ? "TigerString"
                : type;
    }

    private void addFieldDeclare() {
        for (Map.Entry<String, String> e : fields.entrySet()) {
            FieldGen fg = new FieldGen(Const.ACC_PUBLIC,
                    new ObjectType(packageHeader + typeWrapper(e.getValue())),
                    e.getKey(), cp);
            cg.addField(fg.getField());
        }
    }

//    private void addStaticClint() {
//        // static {}
//        InstructionList il = new InstructionList();
//        MethodGen staticMg = new MethodGen(Const.ACC_STATIC, Type.VOID, Type.NO_ARGS, null,
//                "<clinit>", cg.getClassName(), il, cp);
//        il.append(factory.createNew(cg.getClassName()));
//        il.append(InstructionConst.DUP);
//        il.append(factory.createInvoke(cg.getClassName(), "<init>", Type.VOID, Type.NO_ARGS,
//                Const.INVOKESPECIAL));
//        il.append(factory.createPutStatic(cg.getClassName(), "NULL", new ObjectType(cg.getClassName())));
//        il.append(InstructionConst.RETURN);
//        cg.addMethod(staticMg.getMethod());
//    }

    private void addInit() {
        final InstructionList il = new InstructionList();

        // call super.<init>
        il.append(InstructionConst.THIS);
        il.append(new INVOKESPECIAL(cp.addMethodref(cg.getSuperclassName(), "<init>", "()V")));

        for (Map.Entry<String, String> e : fields.entrySet()) {
            // initialize if it is not record type
            // avoid loop initialize
            String name = packageHeader + typeWrapper(e.getValue());
            if (name.equals(packageHeader + "TigerInteger")
                    || name.equals(packageHeader + "TigerString")) {
                il.append(InstructionConst.THIS);
                il.append(factory.createNew(name));
                il.append(InstructionConst.DUP);
                il.append(factory.createInvoke(name, "<init>", Type.VOID, Type.NO_ARGS,
                        Const.INVOKESPECIAL));
                il.append(factory.createPutField(cg.getClassName(), e.getKey(), new ObjectType(name)));
            }
        }

        il.append(InstructionConst.RETURN);
        MethodGen mg = new MethodGen(Const.ACC_PUBLIC, Type.VOID, Type.NO_ARGS,
                null, "<init>", cg.getClassName(), il, cp);
        mg.setMaxStack(10 + fields.size() * 3);
        cg.addMethod(mg.getMethod());
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
        TigerObjectClassFactory factory = new TigerObjectClassFactory(name, fields);
        factory.addFieldDeclare();
//        factory.addStaticClint();
        factory.addInit();
        JavaClass javaClass = factory.cg.getJavaClass();
        try {
            javaClass.dump(new BufferedOutputStream(new FileOutputStream(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
