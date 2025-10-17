import java.util.Iterator;
import java.util.NoSuchElementException;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map2;

/**
 * WorkoutPlanClient: A simple client/tester class to validate the workout
 * planning concept using the OSU CSE Map component model (Map<K, V>).
 */
public class WorkoutPlanClient {

    /**
     * Main method to demonstrate the concept validation. Uses kernel methods:
     * add, value, hasKey, size. Methods like getLevel and getRecommendation are
     * used from SorenessLevel enum. Uses secondary method: iterator.
     */
    public static void main(String[] args) {

        // Instantiate the Map component
        Map<MuscleGroup, SorenessLevel> muscleStatusMap = new Map2<MuscleGroup, SorenessLevel>();

        // 1. Initial State Setup using 'add' kernel method
        out.println("--- Setting Status (Using 'add' kernel method) ---");

        muscleStatusMap.add(MuscleGroup.CHEST, SorenessLevel.FRESH);
        muscleStatusMap.add(MuscleGroup.BACK, SorenessLevel.MILD_SORENESS);
        muscleStatusMap.add(MuscleGroup.LEGS, SorenessLevel.DEAD_SORE);
        muscleStatusMap.add(MuscleGroup.ARMS, SorenessLevel.MODERATE_SORENESS);

        // Validation check using kernel methods
        out.println("Map size: " + muscleStatusMap.size());

        // Iterate through all possible muscle groups to check which ones were added
        // This array iterates over ALL groups, but the logic inside only runs
        // for keys present in the map, demonstrating the kernel methods.
        MuscleGroup[] groupsToCheck = { MuscleGroup.CHEST, MuscleGroup.BACK,
                MuscleGroup.LEGS, MuscleGroup.ARMS, MuscleGroup.SHOULDERS };

        for (MuscleGroup group : groupsToCheck) {
            // Kernel method: hasKey(K)
            if (muscleStatusMap.hasKey(group)) {
                // Kernel method: value(K)
                SorenessLevel status = muscleStatusMap.value(group);
                out.printf("- %-10s is PRESENT. Status: %s\n", group, status);
            } else {
                out.printf("- %-10s is ABSENT.\n", group);
            }
        }

        out.println(
                "-----------------------------------------------------------------");

        // 2. Plan Generation using iterator (secondary method)
        out.println(
                "\n--- Today's Adaptive Workout Plan (Via 'iterator' secondary method) ---");

        boolean trainingPossible = false;
        Iterator<Pair<MuscleGroup, SorenessLevel>> it = muscleStatusMap
                .iterator();

        while (it.hasNext()) {
            Pair<MuscleGroup, SorenessLevel> entry = it.next();
            MuscleGroup group = entry.key();
            SorenessLevel status = entry.value();

            // Print status and recommendation in one line
            out.printf("- %-10s: %s ", group, status);

            // Determine recommendation
            if (status.getLevel() < SorenessLevel.DEAD_SORE.getLevel()) {
                out.printf("[TRAIN]: %s\n", status.getRecommendation());
                trainingPossible = true;
            } else {
                out.printf("[REST]: %s\n", status.getRecommendation());
            }
        }

        if (!trainingPossible) {
            out.println("\nOverall Recommendation: Total Rest Day!");
        } else {
            out.println("\nOverall Recommendation: Focus on safe groups.");
        }
    }
}
