package baislsl.tiger;

/**
 * just a sample, not use in true environment
 *
 * not support nil ?!
 */
public class TigerObjectSample extends TigerObject {
    public TigerInteger integer;
    public TigerString string;
    public TigerObjectSample sample;

    public TigerObjectSample() {
        integer = new TigerInteger();
        string = new TigerString();

        // sample do not need to initialize because it is not a record type
    }
}
