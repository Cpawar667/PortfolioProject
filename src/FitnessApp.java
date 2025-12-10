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
     * Constructs a new FitnessTracker with an empty workout planner state.
     */
    public FitnessTracker() {
        // Assume DemoImplementation is the concrete type available.
        this.sorenessMap = new DemoImplementation();
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
        // Uses kernel method 'add' or 'replaceValue' (indirectly)
        if (this.sorenessMap.hasKey(group)) {
            this.sorenessMap.replaceValue(group, level);
        } else {
            this.sorenessMap.add(group, level);
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
        System.out.println("Chest Soreness: "
                + (this.sorenessMap.hasKey(WorkoutPlanner.MuscleGroup.CHEST)
                        ? this.sorenessMap
                                .value(WorkoutPlanner.MuscleGroup.CHEST)
                        : "N/A"));
        System.out.println("Legs Soreness: "
                + (this.sorenessMap.hasKey(WorkoutPlanner.MuscleGroup.LEGS)
                        ? this.sorenessMap
                                .value(WorkoutPlanner.MuscleGroup.LEGS)
                        : "N/A"));
        System.out.println("---------------------------------");
    }
}
