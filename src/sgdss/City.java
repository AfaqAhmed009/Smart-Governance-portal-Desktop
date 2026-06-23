package sgdss;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class City implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private final String id;
    private String name;
    private String code;

    public City(String name, String code) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.code = code;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    @Override public String toString() { return name + " [" + code + "]"; }
}
