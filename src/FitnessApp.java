import components.sequence.Sequence;
import workoutplanner.WorkoutPlanner;
import workoutplanner.WorkoutPlannerKernel;

/**
 * Proof-of-concept component that uses the WorkoutPlanner as its internal
 * representation to manage a user's current fitness state and plan readiness.
 */
public class FitnessApp {

    /**
     * Internal map storing the user's soreness data. Modeled by the
     * WorkoutPlanner component.
     */
    private WorkoutPlanner sorenessMap;

    /**
     * Constructs a new FitnessApp with the provided workout planner
     * implementation.
     *
     * @param planner
     *            the WorkoutPlanner implementation to use for tracking soreness
     */
    public FitnessApp(WorkoutPlanner planner) {
        this.sorenessMap = planner;
    }

    /**
     * Records the user's current feeling for a muscle group.
     *
     * @param group
     *            the muscle group
     * @param level
     *            the soreness level
     */
    public void recordDailyStatus(WorkoutPlanner.MuscleGroup group,
            WorkoutPlanner.SorenessLevel level) {
        // Convert public-facing enums to kernel enums before calling kernel methods.
        WorkoutPlannerKernel.MuscleGroup kGroup = WorkoutPlannerKernel.MuscleGroup
                .valueOf(group.name());
        WorkoutPlannerKernel.SorenessLevel kLevel = WorkoutPlannerKernel.SorenessLevel
                .valueOf(level.name());
        // Uses kernel method 'add' or 'replaceValue' (indirectly)
        if (this.sorenessMap.hasKey(kGroup)) {
            this.sorenessMap.replaceValue(kGroup, kLevel);
        } else {
            this.sorenessMap.add(kGroup, kLevel);
        }
    }

    /**
     * Generates a recommended workout plan based on a dynamic threshold.
     *
     * @param threshold
     *            the soreness level that mandates rest
     * @return a Sequence of recommended muscle groups to train
     */
    public Sequence<WorkoutPlanner.MuscleGroup> generatePlan(
            WorkoutPlanner.SorenessLevel threshold) {
        // Uses secondary method 'generateTrainingPlan'
        return this.sorenessMap.generateTrainingPlan(threshold);
    }

    /**
     * Prints the current state of the soreness tracker.
     */
    public void printCurrentStatus() {
        // Uses kernel method to retrieve current status (value)
        System.out.println("--- Tracker Status (Snapshot) ---");
        // NOTE: We cannot easily iterate without removeAny, so we check specific keys.
        String chestStatus;
        if (this.sorenessMap.hasKey(WorkoutPlanner.MuscleGroup.CHEST)) {
            chestStatus = String.valueOf(
                    this.sorenessMap.value(WorkoutPlanner.MuscleGroup.CHEST));
        } else {
            chestStatus = "N/A";
        }
        System.out.println("Chest Soreness: " + chestStatus);

        String legsStatus;
        if (this.sorenessMap.hasKey(WorkoutPlanner.MuscleGroup.LEGS)) {
            legsStatus = String.valueOf(
                    this.sorenessMap.value(WorkoutPlanner.MuscleGroup.LEGS));
        } else {
            legsStatus = "N/A";
        }
        System.out.println("Legs Soreness: " + legsStatus);

        System.out.println("---------------------------------");
    }
}
