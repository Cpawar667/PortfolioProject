package components.workoutplanner;

/**
 * SorenessLevel enumeration used to indicate soreness severity.
 */
public enum SorenessLevel {
    /**
     * Takes values from least to most sore from MuscleGroup.NONE to
     * MuscleGroup.HIGH.
     */
    NONE(0), LOW(1), MEDIUM(2), HIGH(3);

    /**
     * Level to change
     */
    private final int level;

    /**
     * Constructs a SorenessLevel with the given numeric level.
     *
     * @param level
     *            numeric severity level (higher means more sore)
     */
    SorenessLevel(int level) {
        this.level = level;
    }

    /**
     * Returns the numeric severity level of this SorenessLevel.
     *
     * @return the numeric severity level
     */
    public int getLevel() {
        return this.level;
    }
}
