package me.baislsl.tiger;

import me.baislsl.tiger.structure.*;
import me.baislsl.tiger.symbol.SystemFunSymbol;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class TypeVisitor implements TigerVisitor {
    private final static Logger logger = LoggerFactory.getLogger(TypeVisitor.class);

    private HashMap<String, Type> typeTable = new HashMap<>();
    private HashMap<String, Type> funcRetTable = new HashMap<>();
    private HashMap<String, HashMap<String, Type>> typeDecTable = new HashMap<>();
    private HashMap<String, Type> varTypeTable = new HashMap<>();


    public TypeVisitor() {
        typeTable.put("int", Type.INT);
        typeTable.put("string", Type.STRING);
        for (SystemFunSymbol s : SystemFunSymbol.symbols) {
            funcRetTable.put(s.name(), s.retType());
        }
    }

    private Type accept(Exp e) {
        e.accept(this);
        return e.type();
    }

    @Override
    public void visit(ArrCreate e) {
        accept(e.exp1);
        accept(e.exp2);
        e.type = typeTable.get(e.tyId.name);
        Objects.requireNonNull(e.type);
    }

    @Override
    public void visit(ArrTy e) {
        throw new CompileException("ArrTy should only appear in TyDec");
    }

    @Override
    public void visit(Assignment e) {
        accept(e.lvalue);
        accept(e.exp);
        if (e.exp.type == null) {
            e.exp.type = e.lvalue.type;
        }
        e.type = Type.VOID;
    }

    @Override
    public void visit(BreakExp e) {
        e.type = Type.VOID;
    }

    @Override
    public void visit(Call e) {
        for (Exp exp : e.exps) {
            accept(exp);
        }
        e.type = funcRetTable.get(e.id.name);
    }

    @Override
    public void visit(FieldCreate e) {
        accept(e.exp);
        typeTable.put(e.id.name, e.exp.type);
    }

    @Override
    public void visit(FieldDec e) {
        varTypeTable.put(e.id.name, typeTable.get(e.tyId.name));
        e.type = typeTable.get(e.tyId.name);
    }

    @Override
    public void visit(FieldExp e) {
        accept(e.lvalue);
        ObjectType ot = (ObjectType) e.lvalue.type;
        e.type = typeDecTable.get(ot.getClassName()).get(e.id.name);
        Objects.requireNonNull(e.type, e.id.name);
    }

    @Override
    public void visit(ForExp e) {
        accept(e.doExp);
        accept(e.fromExp);
        accept(e.toExp);
        e.type = Type.VOID;
    }

    @Override
    public void visit(FunDec e) {
        if(e.tyid != null ){
            funcRetTable.put(e.id.name, typeTable.get(e.tyid.name));
            e.setRetType(typeTable.get(e.tyid.name));
        } else {
            funcRetTable.put(e.id.name, Type.VOID);
            e.setRetType(Type.VOID);
        }

        for (FieldDec f : e.decs) {
            f.accept(this);
        }
        e.exp.accept(this);
    }

    @Override
    public void visit(IdOnlyLvalue e) {
        e.type = varTypeTable.get(e.id.name);
    }

    @Override
    public void visit(IdOnlyTy e) {
        e.type = typeTable.get(e.id.name);
    }

    @Override
    public void visit(IfThen e) {
        e.type = Type.VOID;
        accept(e.ifExp);
        accept(e.thenExp);
    }

    @Override
    public void visit(IfThenElse e) {
        accept(e.ifExp);
        accept(e.elseExp);
        accept(e.thenExp);
        // for nil
        if(e.elseExp.type == null) e.elseExp.type = e.thenExp.type;
        if(e.thenExp.type == null) e.thenExp.type = e.elseExp.type;

        if(e.elseExp.type == null) {
            throw new CompileException("If Else then type can not be nil both");
        }
        e.type = e.elseExp.type();


    }

    @Override
    public void visit(InfixExp e) {
        accept(e.exp1);
        accept(e.exp2);
        // for nil compare
        if (e.exp2.type == null) e.exp2.type = e.exp1.type;
        if (e.exp1.type == null) e.exp1.type = e.exp2.type;

        if (e.exp1.type == null) {   // both nil
            e.exp1.type = e.exp2.type = Type.INT;
        }
        e.type = Type.INT;
    }

    @Override
    public void visit(IntLit e) {
        e.type = Type.INT;
    }

    @Override
    public void visit(LetExp e) {
        e.type = Type.VOID;

        for (Dec d : e.decs) {
            d.accept(this);
        }

        for (Exp exp : e.exps) {
            exp.accept(this);
        }
    }

    @Override
    public void visit(Negation e) {
        accept(e.exp);
        e.type = Type.INT;
    }

    @Override
    public void visit(Nil e) {
        // nil type do not depend on itself
        e.type = null;
    }

    @Override
    public void visit(Program e) {
        accept(e.exp);
    }

    @Override
    public void visit(RecCreate e) {
        e.type = typeTable.get(e.tyId.name);
        for (FieldCreate f : e.fieldCreates) {
            f.accept(this);
        }
    }

    @Override
    public void visit(RecTy e) {
        throw new CompileException("Should appear in TyDec");
    }

    @Override
    public void visit(SeqExp e) {
        for (Exp exp : e.exps) {
            accept(exp);
        }
        if (e.exps.isEmpty()) {
            e.type = Type.VOID;
        } else {
            e.type = e.exps.get(e.exps.size() - 1).type();
        }
    }

    @Override
    public void visit(StringLit e) {
        e.type = Type.STRING;
    }

    @Override
    public void visit(Subscript e) {
        accept(e.lvalue);
        accept(e.exp);
        ArrayType t = (ArrayType) e.lvalue.type();
        e.type = t.getElementType();
    }

    @Override
    public void visit(TyDec e) {
        if (e.ty instanceof IdOnlyTy) {
            e.ty.accept(this);
            Type t = typeTable.get(((IdOnlyTy) e.ty).id.name);
            typeTable.put(e.tyId.name, t);
        } else if (e.ty instanceof RecTy) {
            typeTable.put(e.tyId.name, new ObjectType(e.tyId.name));
            RecTy rt = (RecTy) e.ty;
            HashMap<String, Type> value = new HashMap<>();
            for (FieldDec fe : rt.decs) {
                value.put(fe.id.name, typeTable.get(fe.tyId.name));
            }
            e.ty.type = new ObjectType(e.tyId.name);
            typeDecTable.put(e.tyId.name, value);
        } else {
            ArrTy arrTy = (ArrTy)e.ty;
            typeTable.put(e.tyId.name, new ArrayType(typeTable.get(arrTy.tyId.name), 1));
        }
    }

    @Override
    public void visit(VarDec e) {
        accept(e.exp);
        Type t = e.type();
        if (e.tyId != null) {
            t = typeTable.get(e.tyId.name);
        }
        varTypeTable.put(e.id.name, t);
    }

    @Override
    public void visit(While e) {
        e.type = Type.VOID;
        accept(e.whileExp);
        accept(e.doExp);
    }


}
