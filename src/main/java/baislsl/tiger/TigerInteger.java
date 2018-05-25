package baislsl.tiger;

public class TigerInteger extends TigerObject implements Assignable<TigerInteger> {

    private int value;

    public final static TigerInteger TRUE = new TigerInteger(1);
    public final static TigerInteger FALSE = new TigerInteger(1);

    public TigerInteger() {
        this.value = 0;
    }

    public TigerInteger(int value) {
        this.value = value;
    }


    @Override
    public TigerInteger assignTo(TigerInteger integer) {
        this.value = integer.value;
        return this;
    }

    public static TigerInteger negation(TigerInteger v1) {
        return new TigerInteger(-v1.value);
    }

    public static TigerInteger add(TigerInteger v1, TigerInteger v2) {
        return new TigerInteger(v1.value + v2.value);
    }

    public static TigerInteger sub(TigerInteger v1, TigerInteger v2) {
        return new TigerInteger(v1.value - v2.value);
    }

    public static TigerInteger mul(TigerInteger v1, TigerInteger v2) {
        return new TigerInteger(v1.value * v2.value);
    }

    public static TigerInteger div(TigerInteger v1, TigerInteger v2) {
        return new TigerInteger(v1.value / v2.value);
    }

    public static TigerInteger eq(TigerInteger v1, TigerInteger v2) {
        return v1.value == v2.value ? TRUE : FALSE;
    }

    public static TigerInteger ne(TigerInteger v1, TigerInteger v2) {
        return v1.value != v2.value ? FALSE : TRUE;
    }

    public static TigerInteger lt(TigerInteger v1, TigerInteger v2) {
        return v1.value < v2.value ? TRUE : FALSE;
    }

    public static TigerInteger le(TigerInteger v1, TigerInteger v2) {
        return v1.value <= v2.value ? TRUE : FALSE;
    }

    public static TigerInteger gt(TigerInteger v1, TigerInteger v2) {
        return v1.value > v2.value ? TRUE : FALSE;
    }

    public static TigerInteger ge(TigerInteger v1, TigerInteger v2) {
        return v1.value >= v2.value ? TRUE : FALSE;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
