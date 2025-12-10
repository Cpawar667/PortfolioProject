import components.map.Map;
import components.sequence.Sequence;

/**
 * WorkoutPlanner enhanced interface. This interface extends the
 * WorkoutPlannerKernel by adding secondary methods necessary for practical use,
 * such as updating soreness levels and generating recommended training and rest
 * plans.
 *
 * @author Christian Pawar
 */
public interface WorkoutPlanner extends WorkoutPlannerKernel {

    /**
     * Replaces the soreness level associated with the given muscle group key
     * with {@code newSorenessLevel} and returns the old soreness level.
     *
     * @param muscleGroup
     *            the muscle group (key) whose value is to be replaced
     * @param newSorenessLevel
     *            the new soreness level (value)
     * @return the old soreness level
     * @updates this
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures <pre>
     * this = (#this \ {(muscleGroup, oldSorenessLevel)}) union {(muscleGroup, newSorenessLevel)} and
     * replaceValue = #this.value(muscleGroup)
     * </pre>
     */
    SorenessLevel replaceValue(MuscleGroup muscleGroup,
            SorenessLevel newSorenessLevel);

    /**
     * Generates a workout plan consisting of a Sequence of muscle groups that
     * are suitable for training (where soreness is less than the specified
     * mandatory rest level).
     *
     * @param mandatoryRestLevel
     *            the SorenessLevel that indicates a mandatory rest/avoid status
     * @return a Sequence of muscle groups recommended for today's training
     * @ensures <pre>
     * generateTrainingPlan = [sequence of muscle groups m where
     *                         m is in DOMAIN(this) and
     *                         this.value(m).getLevel() < mandatoryRestLevel.getLevel()]
     * and this = #this
     * </pre>
     */
    Sequence<MuscleGroup> generateTrainingPlan(
            SorenessLevel mandatoryRestLevel);

    /**
     * Generates a Map of muscle groups that are too sore to train, along with
     * their soreness levels.
     *
     * @param mandatoryRestLevel
     *            the SorenessLevel that indicates a mandatory rest/avoid status
     * @return a Map containing muscle groups that require rest
     * @ensures <pre>
     * generateRestMap = [map of (m, s) where
     *                    (m, s) is in this and
     *                    s.getLevel() >= mandatoryRestLevel.getLevel()]
     * and this = #this
     * </pre>
     */
    Map<MuscleGroup, SorenessLevel> generateRestMap(
            SorenessLevel mandatoryRestLevel);

    /**
     * MuscleGroup enumeration used to identify muscle groups.
     */
    public static enum MuscleGroup {
        CHEST, BACK, LEGS, ARMS, SHOULDERS, CORE
    }

    /**
     * SorenessLevel enumeration used to indicate soreness severity.
     */
    public static enum SorenessLevel {
        NONE(0), LOW(1), MEDIUM(2), HIGH(3);

        private final int level;

        SorenessLevel(int level) {
            this.level = level;
        }

        /**
         * Returns the numeric level of soreness.
         * 
         * @return integer level (higher means more sore)
         */
        public int getLevel() {
            return this.level;
        }
    }
}