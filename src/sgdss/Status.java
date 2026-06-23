package sgdss;

public enum Status {
    NEW, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED, REJECTED;

    public String displayName() {
        return switch (this) {
            case NEW -> "New";
            case ASSIGNED -> "Assigned";
            case IN_PROGRESS -> "In Progress";
            case RESOLVED -> "Resolved";
            case CLOSED -> "Closed";
            case REJECTED -> "Rejected";
        };
    }
}
