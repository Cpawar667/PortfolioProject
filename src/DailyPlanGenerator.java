import components.map.Map;
import components.workoutplanner.MuscleGroup;
import components.workoutplanner.SorenessLevel;
import components.workoutplanner.WorkoutPlanner;
import components.workoutplanner.WorkoutPlanner1;

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
        public static Map<MuscleGroup, SorenessLevel> getMandatoryRestGroups(
                        WorkoutPlanner statusPlanner, SorenessLevel threshold) {
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

                userStatus.add(MuscleGroup.BACK, SorenessLevel.HIGH);
                userStatus.add(MuscleGroup.CHEST, SorenessLevel.LOW);
                userStatus.add(MuscleGroup.LEGS, SorenessLevel.MEDIUM);

                final SorenessLevel THRESHOLD = SorenessLevel.HIGH;

                Map<MuscleGroup, SorenessLevel> restRequired = getMandatoryRestGroups(
                                userStatus, THRESHOLD);

                System.out.println("--- Daily Plan Generator Utility Demo ---");
                System.out.println("Rest Threshold: " + THRESHOLD);

                if (restRequired.size() > 0) {
                        while (restRequired.size() > 0) {
                                Map.Pair<MuscleGroup, SorenessLevel> entry = restRequired
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