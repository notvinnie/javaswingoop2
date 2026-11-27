public enum Role {
    PET_OWNER("Pet Owner"),
    VETERINARIAN("Veterinarian"),
    FRONT_DESK("Front Desk Staff");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}