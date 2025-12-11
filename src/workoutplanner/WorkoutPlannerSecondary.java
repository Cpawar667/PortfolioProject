package workoutplanner;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * Layered abstract implementation of the {@code WorkoutPlanner} interface.
 *
 * <p>
 * This class implements all secondary methods by calling only the abstract
 * kernel methods inherited from {@code WorkoutPlannerKernel}. The kernel
 * methods themselves remain abstract in this class.
 * </p>
 *
 * <p>
 * Assuming execution-time performance of kernel methods is O(1), the
 * performance of the implemented methods is as follows:
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

    @Override
    public final WorkoutPlanner.SorenessLevel replaceValue(
            WorkoutPlanner.MuscleGroup muscleGroup,
            WorkoutPlanner.SorenessLevel newSorenessLevel) {
        // Uses kernel methods: hasKey(), remove(), and add()
        assert this.hasKey(
                muscleGroup) : "Violation of: muscleGroup is in DOMAIN(this)";

        // Remove the old entry and get the old soreness level
        WorkoutPlanner.SorenessLevel oldLevel = this.remove(muscleGroup);

        // Add the new entry with the updated soreness level
        this.add(muscleGroup, newSorenessLevel);

        return oldLevel;
    }

    @Override
    public final Sequence<WorkoutPlanner.MuscleGroup> generateTrainingPlan(
            WorkoutPlanner.SorenessLevel mandatoryRestLevel) {
        /*
         * Uses kernel methods: size(), removeAny(), add() (on temp map),
         * transferFrom(), and the getLevel() method on the SorenessLevel enum.
         */
        Sequence<WorkoutPlanner.MuscleGroup> trainingPlan = new Sequence1L<>();
        // Temporary storage to hold and restore the content of 'this'
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> temp = new Map1L<>();

        // Iterate through all entries using the kernel methods
        while (this.size() > 0) {
            Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = this
                    .removeAny();
            WorkoutPlanner.MuscleGroup group = entry.key();
            WorkoutPlanner.SorenessLevel level = entry.value();

            // If soreness level is below mandatory rest level, add to training plan
            if (level.getLevel() < mandatoryRestLevel.getLevel()) {
                trainingPlan.add(trainingPlan.length(), group);
            }

            // Store in temp to restore later (transferFrom expects a Map)
            temp.add(group, level);
        }

        // Restore all entries back to 'this'
        // We cannot use transferFrom because temp is a Map, not a WorkoutPlanner.
        while (temp.size() > 0) {
            Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = temp
                    .removeAny();
            this.add(entry.key(), entry.value());
        }

        return trainingPlan;
    }

    @Override
    public final Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> generateRestMap(
            WorkoutPlanner.SorenessLevel mandatoryRestLevel) {
        /*
         * Uses kernel methods: size(), removeAny(), add() (on rest map and temp
         * map), transferFrom(), and the getLevel() method on the SorenessLevel
         * enum.
         */
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restMap = new Map1L<>();
        // Temporary storage to hold and restore the content of 'this'
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> temp = new Map1L<>();

        // Iterate through all entries using the kernel methods
        while (this.size() > 0) {
            Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = this
                    .removeAny();
            WorkoutPlanner.MuscleGroup group = entry.key();
            WorkoutPlanner.SorenessLevel level = entry.value();

            // If soreness level is at or above mandatory rest level, add to rest map
            if (level.getLevel() >= mandatoryRestLevel.getLevel()) {
                restMap.add(group, level);
            }

            // Store in temp to restore later
            temp.add(group, level);
        }

        // Restore all entries back to 'this'
        // We cannot use transferFrom because temp is a Map, not a WorkoutPlanner.
        while (temp.size() > 0) {
            Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = temp
                    .removeAny();
            this.add(entry.key(), entry.value());
        }

        return restMap;
    }

}
