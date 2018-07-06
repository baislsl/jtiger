# Compiler

A naive tiger compiler that compiles tiger code to JVM executable class file.

## 使用说明

java部分提供可执行的jar包（target/tiger-1.0-SNAPSHOT-jar-with-dependencies.jar), C++代码需要重新编译

```
git clone git@github.com:baislsl/jtiger.git
cd jtiger
cd absyn
make
cd ..
mvn clean compile assembly:single # 可跳过，使用提供的jar包
java -jar target/tiger-1.0-SNAPSHOT-jar-with-dependencies.jar path_to_tiger_source_code
java -jar tiger.jar # 执行编译出来的jar文件
```
需要注意使用tiger-1.0-SNAPSHOT-jar-with-dependencies.jar时的目录，因为这是根据写死的路径来使用absyn/main，所以一定要在项目根目录
上执行

## 环境和依赖

环境：

- c++11
- python3
- jdk8+
- maven

主要三方库：

- flex
- bison
- rapidjson
- com.googlecode.json-simple
- org.apache.bcel

## 目标代码生成

本编译器按照Modern Compiler Implementation in C（虎书）附录中tiger的语法标准翻译，
最终会生成JVM平台可执行的class文件，
可以直接使用java命令执行，保证输出结果和退出状态正确。

### 语法支持

- 不支持:

    * for 语句定义的id不支持嵌套引用（如果for中定义let，那么let语句块里面无法访问到该id）
    * 不同作用域的函数重名或者结构类型重名会有问题

- 支持：***其他所有特性***, 包括嵌套函数，数组，结构体等

### 实现

#### 语法树

JVM是基于栈运行的虚拟机，在翻译时直接基于语法树进行翻译。

词法分析和语法分析均使用c++和python编写，经过这两个步骤后得出语法树，将语法树存在json格式文件，再在java中读取json文件重现语法树。
json数据一个例子如下：

```json
{
  "class" : "Program",
  "exp": {
    "class" : "LetExp",
    "decs" :[

    ],
    "exps" : [
      {
        "class" : "ForExp",
        "id" : "i",
        "fromExp" : {
          "class": "IntLit",
          "value" : "1234"
        },
        "toExp" : {
          "class" : "IntLit",
          "value" : "1235"
        },
        "doExp": {
          "class" : "Call",
          "id" : "print",
          "exps" : [
            {
              "class" : "StringLit",
              "value" : "Hello World"
            }
          ]
        }

      }
    ]
  }
}
```

这里因为java有反射，只要将名字对上反射的名称，能够比较简单地通过反射读取json文件，所以这个格式是根据me.baislsl.tiger.structure
包下的数据定义而定下的，和C++代码定义的结构里的稍有不同。

### 关于JVM字节码和执行环境

JVM是基于栈执行的虚拟机，所以翻译时字节在语法树的基础上翻译出最终的字节码，并没有经过中间代码的步骤。

执行环境中与本编译器比较相关的除了方法栈外还有局部变量表（local variable table)，局部变量表主要存放方法参数（包括this引用）和临时定义的变量。虽然JVM提供了局部变量表这种类似与寄存器的结构，但非常不推荐编译器在翻译时无中生有生成额外的局部变量，而是建议一切操作基于栈执行。

本编译器翻译时尽最大程度尊重这个设计原则，比如在for循环中，只有因为代码中显式定义了局部变量，所以才会在局部变量表中生成一个局部变量。唯一一个生成额外局部变量的地方是数组初始化，会额外生成一个```$$$i$$$```的局部变量，这主要是考虑到tiger语言和java的不同，JVM字节码集合中创建新的数组只有newarray指令，new出来的数组初始值默认为该类型的初始值，并没有指定初始值的字节码指令，为了实现tiger中数组整个初始化赋值，需要一个额外的for循环，如果不引入一个新的局部变量，很难利用JVM有限的dup，swap栈顶操作实现for循环赋值。

### 字节码生成

写入二进制文件非常繁琐，JVM8的class文件格式定义可见
[The Java® Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se8/html/index.html)
第4章,字节码集定义则见第6章。

本项目依赖bcel（ Byte Code Engineering Library）写入文件，bcel是一个专注与读写、编辑、分析java类文件的三方库，本项目主要
通过分析语法树得出字节码的基础上使用bcel生成类文件。

该部分生成字节码主要遍历两次语法树，第一次遍历进行类型分析，实现见[TypeVisitor](./src/main/java/me/baislsl//tigerTypeVisitor.java),第二次遍历在类型确定的基础上进行翻译，实现见[TigerVisitorImpl](./src/main/java/me/baislsl/tiger/TigerVisitorImpl.java).

### java和tiger数据结构的转换

|       | 整形 | 字符串 | 
| ----- | :----: | :----: | 
| java  | int | java.lang.String | 
| tiger | int | string | 

数组直接对上java上的数组，对于用户自定义的结构，会为该结构生成一个类，类成员对应结构成员

比如类型定义

```tiger
type any = {any : int}
```

编译出来的any.class文件经过反编译后对应java代码应该为

```java
public class any {
    public int any;
    public any() {
    }
}
```

### Tiger标准库实现

这部分直接用写java代码,为每个标准库接口编写一个静态的方法，编译好后在执行tiger程序时放在正确的classpath上即可，
标准库java实现代码可见[TigerFuncLink.java](./src/main/java/me/baislsl/tiger/TigerFuncLink.java).


这样翻译时，调用标准库接口就对应一个invokestatic指令。

### Nested function和let翻译

会为每个function和let生成一个他们对应的类，类初始化时会保留一个parent的引用

函数的参数为作为类成员，在构造函数中传入并初始化，比如函数

```tiger
function f(message string) = print(message)
```

被编译出来的f.class文件反编译后对应的java代码应为

```java
import me.baislsl.tiger.TigerFuncLink;

public class FunctionSampleClass {
    // Object should be the type of parent
    Object $$$parent$$$;
    public FunctionSampleClass(Object $$$parent$$$, String message) {
        this.$$$parent$$$ = $$$parent$$$;
        this.message = message;
    }
    public void $$$invoke$$$$() {
      TigerFuncLink.print(message);
    }
}
```

$$$parent$$$、$$$invoke$$$$的命名方式是为了避免和tiger代码中命名冲突。
这样，当调用```f("Hello World")```时，实际的java代码对应```new f(this, "Hello World").$$$invoke$$$$();```，$$$invoke$$$$方法的返回值就是该函数的返回值，这里f没有返回值，所以$$$invoke$$$$返回类型为void。

需要注意的是这里构造函数第一个参数并非一定简单的为this，这取决于调用函数f时函数f在符号表中相对于当前的深度，假设深度为d(d = 0, 1, 2...)，那么应该在this上调用d次getfield $$$parent$$$才能得到对应的引用，比如如果深度d为3，
会被翻译换成```this.$$$parent$$$.$$$parent$$$.$$$parent$$$```,对应字节码是执行连续3次的getfield.

对于Let语句```let dec+ in exp∗; end```，
dec中如果是函数类或者新类型定义，会按照上面的方法生成对应的类，如果为成员定义，该成员会作为生成类的成员，其初始化放在构造函数中进行。```exp*```全部放在```$$$invoke$$$$```方法中。let生成的类也需要传进parent的引用，

比如以下tiger代码

```tiger
let 
  var N := 123
in
  print("Hello World")
end
```

生成的类反编译后结果应该为

```java
import me.baislsl.tiger.TigerFuncLink;

public class LetDecTest {
    public StringTestRoot $$$parent$$$;
    public int N;

    public LetDecTest(StringTestRoot $$$parent$$$) {
        this.$$$parent$$$ = $$$parent$$$;
        this.N = 123;
    }

    public void $$$invoke$$$() {
        TigerFuncLink.print("Hello World");
    }
}
```

这样，let和function生成的类中都会包含调用其的父引用，当需要访问其上层作用域的参数或函数时，首先在符号表中寻找该符号，符号表将找到的符号和深度d一并返回，然后在this引用上调用d次getfield $$$parent$$$，从而获取对应的符号。

### 测试

已经通过虎书附录提供的八皇后问题和合并链表的例程和其他测试程序。

### TODO

- 命名可能会出问题，不同作用域的多个同名函数会出bug，因为现在函数生成的类都是放在同一个目录下，并且直接以函数名作为类名，
所以后面出现的会覆盖前面的文件

- for里面定义的变量只支持直接在for中访问，如果for语句里面又搞个let或者func，那么let和func里都不支持对for定义的循环变量访问







