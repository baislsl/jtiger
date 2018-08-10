# Compiler

A naive tiger compiler which compiles tiger code to JVM executable jar.

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

按照Modern Compiler Implementation in C（虎书）附录中tiger的语法标准翻译，
最终会生成JVM平台可执行的class文件，
可以直接使用java命令执行，保证输出结果和退出状态正确。

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

### Nested function和Let翻译

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







