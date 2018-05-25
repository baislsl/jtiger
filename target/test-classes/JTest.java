
public class JTest {

    private static void convert(Integer i) {
        i = 123;
    }

    public static void main(String[] args) {
        Integer i = 100;

        convert(i);

        System.out.println(i);


    }
    int jj;

    class SubClass{
        int a, b;

        class SubSubclass{
            int h, g = jj;
        }
    }

}