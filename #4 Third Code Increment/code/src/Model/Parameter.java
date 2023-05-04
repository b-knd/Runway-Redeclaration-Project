package Model;

public class Parameter {
    private String name;
    private String originalValue;
    private String newValue;

    public Parameter(String name, String originalValue, String newValue) {
        this.name = name;
        this.originalValue = originalValue;
        this.newValue = newValue;
    }

    public String getName() {
        return name;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
