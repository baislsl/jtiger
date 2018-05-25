


public class Main {
    static class gg{
        public void print() {
            System.out.println(Main.class.getName());
        }
    }

    public static void main(String[] args) {
        new gg().print();

    }

}
