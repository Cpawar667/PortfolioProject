import components.map.Map;
import workoutplanner.WorkoutPlanner;
import workoutplanner.WorkoutPlanner1;

/**
 * Utility class demonstrating direct use of the WorkoutPlanner component.
 */
public final class DailyPlanGenerator {

        /**
         * Private constructor to prevent instantiation of this utility class.
         */
        private DailyPlanGenerator() {
        }

        /**
         * Returns a map of muscle groups that must rest based on the provided
         * threshold.
         *
         * @param statusPlanner
         *                the WorkoutPlanner providing current soreness status
         * @param threshold
         *                the soreness level threshold above which rest is
         *                required
         * @return a Map from MuscleGroup to SorenessLevel containing groups
         *         requiring rest
         */
        public static Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> getMandatoryRestGroups(
                        WorkoutPlanner statusPlanner,
                        WorkoutPlanner.SorenessLevel threshold) {
                return statusPlanner.generateRestMap(threshold);
        }

        /**
         * Main entry point demonstrating usage of WorkoutPlanner and printing
         * mandatory rest groups.
         *
         * @param args
         *                command-line arguments (not used)
         */
        public static void main(String[] args) {
                // Use the concrete implementation from the workoutplanner package
                WorkoutPlanner userStatus = new WorkoutPlanner1();

                userStatus.add(WorkoutPlanner.MuscleGroup.BACK,
                                WorkoutPlanner.SorenessLevel.HIGH);
                userStatus.add(WorkoutPlanner.MuscleGroup.CHEST,
                                WorkoutPlanner.SorenessLevel.LOW);
                userStatus.add(WorkoutPlanner.MuscleGroup.LEGS,
                                WorkoutPlanner.SorenessLevel.MEDIUM);

                final WorkoutPlanner.SorenessLevel THRESHOLD = WorkoutPlanner.SorenessLevel.HIGH;

                Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restRequired = getMandatoryRestGroups(
                                userStatus, THRESHOLD);

                System.out.println("--- Daily Plan Generator Utility Demo ---");
                System.out.println("Rest Threshold: " + THRESHOLD);

                if (restRequired.size() > 0) {
                        while (restRequired.size() > 0) {
                                Map.Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> entry = restRequired
                                                .removeAny();
                                System.out.println("- " + entry.key()
                                                + " (Soreness: " + entry.value()
                                                + ")");
                        }
                } else {
                        System.out.println(
                                        "- No groups require rest based on threshold.");
                }
        }
}