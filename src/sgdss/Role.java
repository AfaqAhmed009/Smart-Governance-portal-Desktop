package sgdss;

public enum Role {
    CITIZEN,
    OFFICER,
    DEPT_ADMIN,
    CITY_ADMIN,
    GLOBAL_ADMIN;

    public String displayName() {
        return switch (this) {
            case CITIZEN -> "Citizen";
            case OFFICER -> "Officer";
            case DEPT_ADMIN -> "Department Admin";
            case CITY_ADMIN -> "City Admin";
            case GLOBAL_ADMIN -> "Global Admin";
        };
    }

    public boolean atLeast(Role other) {
        return this.ordinal() >= other.ordinal();
    }
}
