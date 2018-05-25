package baislsl.tiger;

public class TigerString extends TigerObject implements Assignable<TigerString> {
    private String value;

    public TigerString() {
        this.value = "";
    }

    public TigerString(String value) {
        this.value = value;
    }

    @Override
    public TigerString assignTo(TigerString v) {
        this.value = v.value;
        return this;
    }

    public static TigerInteger eq(TigerString v1, TigerString v2) {
        return v1.value.equals(v2.value) ? TigerInteger.TRUE : TigerInteger.FALSE;
    }

    public static TigerInteger ne(TigerString v1, TigerString v2) {
        return v1.value.equals(v2.value) ? TigerInteger.FALSE : TigerInteger.TRUE;
    }

    public static TigerInteger lt(TigerString v1, TigerString v2) {
        return v1.value.compareTo(v2.value) < 0 ? TigerInteger.TRUE : TigerInteger.FALSE;
    }

    public static TigerInteger le(TigerString v1, TigerString v2) {
        return v1.value.compareTo(v2.value) <= 0 ? TigerInteger.TRUE : TigerInteger.FALSE;
    }

    public static TigerInteger gt(TigerString v1, TigerString v2) {
        return v1.value.compareTo(v2.value) > 0 ? TigerInteger.TRUE : TigerInteger.FALSE;
    }

    public static TigerInteger ge(TigerString v1, TigerString v2) {
        return v1.value.compareTo(v2.value) >= 0 ? TigerInteger.TRUE : TigerInteger.FALSE;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
