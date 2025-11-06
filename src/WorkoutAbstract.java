import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * Layered implementations of secondary methods for {@code WorkoutPlanner}.
 *
 * <p>
 * Assuming execution-time performance of kernel methods is O(1), the
 * performance of the methods in this class is as follows:
 * </p>
 * <ul>
 * <li>replaceValue: O(1)</li>
 * <li>generateTrainingPlan: O(n) where n = |this|</li>
 * <li>generateRestMap: O(n) where n = |this|</li>
 * </ul>
 *
 * @author Christian Pawar
 */
public abstract class WorkoutPlannerSecondary implements WorkoutPlanner {
    /**
     * MuscleGroup enumeration used to identify muscle groups.
     */
    public static enum MuscleGroup {
        CHEST, BACK, LEGS, ARMS, SHOULDERS, CORE
    }

    public static enum SorenessLevel {
        NONE(0), LOW(1), MEDIUM(2), HIGH(3);

        private final int level;


        SorenessLevel(int level) {
            this.level = level;
        }
    @Override
    public final SorenessLevel replaceValue(MuscleGroup muscleGroup,
            SorenessLevel newSorenessLevel) {

        // Uses kernel methods: remove() and add()
        assert this.hasKey(
                muscleGroup) : "Violation of: muscleGroup is in DOMAIN(this)";

        // Remove the old entry and get the old soreness level
        SorenessLevel oldLevel = this.remove(muscleGroup);

        // Add the new entry with the updated soreness level
        this.add(muscleGroup, newSorenessLevel);

        return oldLevel;
    }

    @Override
    public final Sequence<MuscleGroup> generateTrainingPlan(
            SorenessLevel mandatoryRestLevel) {

        // Uses kernel methods: size(), removeAny(), add()
        Sequence<MuscleGroup> trainingPlan = new Sequence1L<>();

        // Temporary storage to restore this after checking all entries
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Check each muscle group
        while (this.size() > 0) {
            Map.Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            MuscleGroup group = entry.key();
            SorenessLevel level = entry.value();

            // If soreness level is below mandatory rest level, add to training plan
            if (level.getLevel() < mandatoryRestLevel.getLevel()) {
                trainingPlan.add(trainingPlan.length(), group);
            }

            // Store in temp to restore later
            temp.add(group, level);
        }

        // Restore all entries back to this
        this.transferFrom(temp);

        return trainingPlan;
    }

    @Override
    public final Map<MuscleGroup, SorenessLevel> generateRestMap(
            SorenessLevel mandatoryRestLevel) {

        // Uses kernel methods: size(), removeAny(), add()
        Map<MuscleGroup, SorenessLevel> restMap = new Map1L<>();

        // Temporary storage to restore this after checking all entries
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Check each muscle group
        while (this.size() > 0) {
            Map.Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            MuscleGroup group = entry.key();
            SorenessLevel level = entry.value();

            // If soreness level is at or above mandatory rest level, add to rest map
            if (level.getLevel() >= mandatoryRestLevel.getLevel()) {
                restMap.add(group, level);
            }

            // Store in temp to restore later
            temp.add(group, level);
        }
    /**
     * MuscleGroup enumeration used to identify muscle groups.
     */
    public static enum MuscleGroup {
        CHEST, BACK, LEGS, ARMS, SHOULDERS, CORE
    }

    /**
     * SorenessLevel enumeration used to indicate soreness severity.
     */

        // Restore all entries back to this
        this.transferFrom(temp);

        return restMap;
    }
}