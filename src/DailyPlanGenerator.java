import components.map.Map;

/**
 * Utility class demonstrating direct use of the WorkoutPlanner component in a
 * static method, simulating a plan generation library function.
 */
public final class DailyPlanGenerator {

    /**
     * Private constructor to prevent instantiation.
     */
    private DailyPlanGenerator() {
    }

    /**
     * Analyzes the current muscle status and reports the groups that require
     * rest.
     *
     * @param statusPlanner
     *            a populated WorkoutPlanner object (input)
     * @param threshold
     *            the soreness level that mandates rest
     * @return a Map of muscle groups and their high soreness levels
     */
    public static Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> getMandatoryRestGroups(
            WorkoutPlanner statusPlanner,
            WorkoutPlanner.SorenessLevel threshold) {

        // Uses the secondary method 'generateRestMap' directly on the input object
        return statusPlanner.generateRestMap(threshold);
    }

    public static void main(String[] args) {
        // --- Setup Input Data ---
        WorkoutPlanner userStatus = new DemoImplementation();
        userStatus.add(WorkoutPlanner.MuscleGroup.BACK,
                WorkoutPlanner.SorenessLevel.HIGH);
        userStatus.add(WorkoutPlanner.MuscleGroup.CHEST,
                WorkoutPlanner.SorenessLevel.LOW);
        userStatus.add(WorkoutPlanner.MuscleGroup.LEGS,
                WorkoutPlanner.SorenessLevel.MEDIUM);

        final WorkoutPlanner.SorenessLevel THRESHOLD = WorkoutPlanner.SorenessLevel.HIGH;

        // --- Execution ---
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restRequired = getMandatoryRestGroups(
                userStatus, THRESHOLD);

        // --- Output ---
        System.out.println("--- Daily Plan Generator Utility Demo ---");
        System.out.println("Rest Threshold: " + THRESHOLD);
        System.out.println("Rest Groups Found (Using generateRestMap):");

        if (restRequired.size() > 0) {
            // Note: restRequired state is consumed by removeAny() for printing.
            while (restRequired.size() > 0) {
                Map.Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = restRequired
                        .removeAny();
                System.out.println("- " + entry.key() + " (Soreness: "
                        + entry.value() + ")");
            }
        } else {
            System.out.println("- No groups require rest based on threshold.");
        }

        // IMPORTANT: The original userStatus object is UNCHANGED after the call
        // because generateRestMap restores the state of its receiver.
        System.out.println(
                "\nOriginal Planner size after call: " + userStatus.size());
    }
}
