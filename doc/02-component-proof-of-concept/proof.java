import java.util.Iterator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map2;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;


 
public class WorkoutPlanClient {

    /**
     * Checks if a specific muscle group is safe to train.
     *
     * @param muscleStatus
     *            the map of muscle groups to soreness levels
     * @param group
     *            the muscle group to check
     * @return true if the group can be trained, false otherwise
     */
    private static boolean canTrainMuscleGroup(
            Map<MuscleGroup, SorenessLevel> muscleStatus, MuscleGroup group) {

        if (muscleStatus.hasKey(group)) {
            SorenessLevel status = muscleStatus.value(group);
            return status.getLevel() < SorenessLevel.DEAD_SORE.getLevel();
        }
        return false;
    }

    /**
     * Finds the muscle group with the lowest soreness level (most recovered).
     *
     * @param muscleStatus
     *            the map of muscle groups to soreness levels
     * @return the most recovered muscle group, or null if map is empty
     */
    private static MuscleGroup getMostRecoveredGroup(
            Map<MuscleGroup, SorenessLevel> muscleStatus) {

        if (muscleStatus.size() == 0) {
            return null;
        }

        Iterator<Pair<MuscleGroup, SorenessLevel>> it = muscleStatus
                .iterator();
        Pair<MuscleGroup, SorenessLevel> best = it.next();

        while (it.hasNext()) {
            Pair<MuscleGroup, SorenessLevel> current = it.next();
            if (current.value().getLevel() < best.value().getLevel()) {
                best = current;
            }
        }
        return best.key();
    }

    /**
     * Updates the soreness level for a muscle group after training.
     *
     * @param muscleStatus
     *            the map to update
     * @param group
     *            the muscle group that was trained
     * @param newLevel
     *            the new soreness level
     * @updates muscleStatus
     */
    private static void updateSoreness(
            Map<MuscleGroup, SorenessLevel> muscleStatus, MuscleGroup group,
            SorenessLevel newLevel) {

        if (muscleStatus.hasKey(group)) {
            muscleStatus.remove(group);
        }
        muscleStatus.add(group, newLevel);
    }

    /**
     * Main method to demonstrate the concept validation. Uses kernel methods:
     * add, value, hasKey, remove, size. Uses secondary method: iterator.
     */
    public static void main(String[] args) {

        SimpleWriter out = new SimpleWriter1L();

        Map<MuscleGroup, SorenessLevel> muscleStatusMap = new Map2<MuscleGroup, SorenessLevel>();

        out.println("--- Setting Status (Using 'add' kernel method) ---");

        muscleStatusMap.add(MuscleGroup.CHEST, SorenessLevel.FRESH);
        muscleStatusMap.add(MuscleGroup.BACK, SorenessLevel.MILD_SORENESS);
        muscleStatusMap.add(MuscleGroup.LEGS, SorenessLevel.DEAD_SORE);
        muscleStatusMap.add(MuscleGroup.ARMS, SorenessLevel.MODERATE_SORENESS);

        out.println("Map size: " + muscleStatusMap.size());

        MuscleGroup[] groupsToCheck = { MuscleGroup.CHEST, MuscleGroup.BACK,
                MuscleGroup.LEGS, MuscleGroup.ARMS, MuscleGroup.SHOULDERS };

        for (MuscleGroup group : groupsToCheck) {
            if (muscleStatusMap.hasKey(group)) {
                SorenessLevel status = muscleStatusMap.value(group);
                out.println("- " + group + " is PRESENT. Status: " + status);
            } else {
                out.println("- " + group + " is ABSENT.");
            }
        }

        out.println(
                "-----------------------------------------------------------------");

        out.println(
                "\n--- Today's Adaptive Workout Plan (Via 'iterator' secondary method) ---");

        boolean trainingPossible = false;
        Iterator<Pair<MuscleGroup, SorenessLevel>> it = muscleStatusMap
                .iterator();

        while (it.hasNext()) {
            Pair<MuscleGroup, SorenessLevel> entry = it.next();
            MuscleGroup group = entry.key();
            SorenessLevel status = entry.value();

            out.print("- " + group + ": " + status + " ");

            if (status.getLevel() < SorenessLevel.DEAD_SORE.getLevel()) {
                out.println("[TRAIN]: " + status.getRecommendation());
                trainingPossible = true;
            } else {
                out.println("[REST]: " + status.getRecommendation());
            }
        }

        if (!trainingPossible) {
            out.println("\nOverall Recommendation: Total Rest Day!");
        } else {
            out.println("\nOverall Recommendation: Focus on safe groups.");
        }

        out.println(
                "-----------------------------------------------------------------");

        out.println("\n--- Testing Custom Methods ---");

        out.println("\nTesting canTrainMuscleGroup():");
        MuscleGroup testGroup = MuscleGroup.CHEST;
        if (canTrainMuscleGroup(muscleStatusMap, testGroup)) {
            out.println("- " + testGroup + " is SAFE to train");
        } else {
            out.println("- " + testGroup + " needs REST");
        }

        testGroup = MuscleGroup.LEGS;
        if (canTrainMuscleGroup(muscleStatusMap, testGroup)) {
            out.println("- " + testGroup + " is SAFE to train");
        } else {
            out.println("- " + testGroup + " needs REST");
        }

        out.println("\nTesting getMostRecoveredGroup():");
        MuscleGroup mostRecovered = getMostRecoveredGroup(muscleStatusMap);
        if (mostRecovered != null) {
            out.println("- Most recovered muscle group: " + mostRecovered);
            out.println("  Status: " + muscleStatusMap.value(mostRecovered));
        }

        out.println("\nTesting updateSoreness():");
        out.println("- Before update: CHEST status = "
                + muscleStatusMap.value(MuscleGroup.CHEST));

        updateSoreness(muscleStatusMap, MuscleGroup.CHEST,
                SorenessLevel.MODERATE_SORENESS);

        out.println("- After training CHEST: CHEST status = "
                + muscleStatusMap.value(MuscleGroup.CHEST));

        out.println(
                "-----------------------------------------------------------------");

        out.close();
    }
}
