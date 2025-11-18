import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * WorkoutPlannerSecondary: A partial implementation of the WorkoutPlanner
 * interface that provides secondary methods using the kernel methods.
 *
 * @author Christian Pawar
 *
 * @mathmodel <pre>
 * type WorkoutPlanner is modeled as a partial function from MuscleGroup to SorenessLevel
 * </pre>
 * @initially <pre>
 * default:
 *  ensures
 *   this = {}
 * </pre>
 * @iterator <pre>
 * ~this.seen * ~this.unseen = this  and
 * |~this.seen * ~this.unseen| = |this|
 * </pre>
 */

public abstract class WorkoutPlannerSecondary implements WorkoutPlanner {

    @Override
    public final SorenessLevel replaceValue(MuscleGroup muscleGroup,
            SorenessLevel newSorenessLevel) {
        // Uses kernel methods: hasKey(), remove(), and add()
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
        /*
         * Uses kernel methods: size(), removeAny(), add() (on temp map),
         * transferFrom(), and the getLevel() method on the SorenessLevel enum.
         */
        Sequence<MuscleGroup> trainingPlan = new Sequence1L<>();
        // Temporary storage to hold and restore the content of 'this'
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Iterate through all entries using the kernel methods
        while (this.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            MuscleGroup group = entry.key();
            SorenessLevel level = entry.value();

            // If soreness level is below mandatory rest level, add to training plan
            if (level.getLevel() < mandatoryRestLevel.getLevel()) {
                trainingPlan.add(trainingPlan.length(), group);
            }

            // Store in temp to restore later (transferFrom expects a Map)
            temp.add(group, level);
        }

        // Restore all entries back to 'this' using the kernel transfer method
        this.transferFrom(temp);

        return trainingPlan;
    }

    @Override
    public final Map<MuscleGroup, SorenessLevel> generateRestMap(
            SorenessLevel mandatoryRestLevel) {
        /*
         * Uses kernel methods: size(), removeAny(), add() (on rest map and temp
         * map), transferFrom(), and the getLevel() method on the SorenessLevel
         * enum.
         */
        Map<MuscleGroup, SorenessLevel> restMap = new Map1L<>();
        // Temporary storage to hold and restore the content of 'this'
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Iterate through all entries using the kernel methods
        while (this.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            MuscleGroup group = entry.key();
            SorenessLevel level = entry.value();

            // If soreness level is at or above mandatory rest level, add to rest map
            if (level.getLevel() >= mandatoryRestLevel.getLevel()) {
                restMap.add(group, level);
            }

            // Store in temp to restore later
            temp.add(group, level);
        }

        // Restore all entries back to 'this' using the kernel transfer method
        this.transferFrom(temp);

        return restMap;
    }
}