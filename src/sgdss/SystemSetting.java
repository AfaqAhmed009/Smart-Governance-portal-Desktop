package sgdss;

import java.io.Serial;
import java.io.Serializable;

/**
 * SystemSetting stores key-value configuration pairs for the application.
 * Used for theme, notification preferences, and other settings.
 */
public class SystemSetting implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private String key;
    private String value;
    private String description;

    public SystemSetting(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
    public String getDescription() { return description; }

    public void setValue(String value) { this.value = value; }
    public void setDescription(String description) { this.description = description; }
}
