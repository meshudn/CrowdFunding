package Model;

/**
 * Created by meshu on 5/8/2017.
 */

public class ValidateString {
    private String value;
    private Boolean isIgnore;

    public ValidateString(String value, Boolean isIgnore) {
        this.value = value;
        this.isIgnore = isIgnore;
    }

    public ValidateString() {
    }

    public ValidateString(String value) {
        this.value = value;
        this.isIgnore = false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getIgnore() {
        return isIgnore;
    }

    public void setIgnore(Boolean ignore) {
        isIgnore = ignore;
    }

    @Override
    public String toString() {
        return "ValidateString{" +
                "value='" + value + '\'' +
                ", isIgnore=" + isIgnore +
                '}';
    }
}
