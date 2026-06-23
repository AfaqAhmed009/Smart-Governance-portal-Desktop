package sgdss;

public enum Priority {
    LOW, MEDIUM, HIGH, URGENT;

    public String displayName() {
        return switch (this) {
            case LOW -> "Low";
            case MEDIUM -> "Medium";
            case HIGH -> "High";
            case URGENT -> "Urgent";
        };
    }
}
