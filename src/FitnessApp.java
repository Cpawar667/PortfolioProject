import java.util.Scanner;

import components.sequence.Sequence;
import components.workoutplanner.MuscleGroup;
import components.workoutplanner.SorenessLevel;
import components.workoutplanner.WorkoutPlanner;
import components.workoutplanner.WorkoutPlanner1;

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
    public void recordDailyStatus(MuscleGroup group, SorenessLevel level) {
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
    public Sequence<MuscleGroup> generatePlan(SorenessLevel threshold) {
        return this.sorenessMap.generateTrainingPlan(threshold);
    }

    /**
     * Main method to run the Interactive Fitness App.
     */
    public static void main(String[] args) {
        WorkoutPlanner planner = new WorkoutPlanner1();
        FitnessApp app = new FitnessApp(planner);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("      Welcome to the Fitness App!        ");
        System.out.println("=========================================");
        System.out.println("Please enter soreness for each muscle.");
        System.out.println("Options: NONE, LOW, MEDIUM, HIGH");
        System.out.println("-----------------------------------------");

        // Loop through all MuscleGroups and ask the user for input
        for (MuscleGroup group : MuscleGroup.values()) {
            while (true) {
                System.out.print("How sore is your " + group + "? ");
                String input = scanner.nextLine().trim().toUpperCase();

                try {
                    SorenessLevel level = SorenessLevel.valueOf(input);
                    app.recordDailyStatus(group, level);
                    break; // Move to next muscle if input is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Invalid input. Please type: NONE, LOW, MEDIUM, or HIGH.");
                }
            }
        }

        // Ask for the threshold (At what point should you rest?)
        System.out.println("-----------------------------------------");
        System.out.println("Let's generate your plan.");
        System.out.println(
                "What is your soreness limit? (e.g. MEDIUM means rest if Medium or High)");

        SorenessLevel threshold = SorenessLevel.MEDIUM; // Default
        while (true) {
            System.out.print("Enter threshold [default: MEDIUM]: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                break; // Use default
            }
            try {
                threshold = SorenessLevel.valueOf(input);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Try: LOW, MEDIUM, or HIGH.");
            }
        }

        // Generate and print the plan
        Sequence<MuscleGroup> plan = app.generatePlan(threshold);

        System.out.println("\n*** TODAY'S WORKOUT PLAN ***");
        if (plan.length() == 0) {
            System.out.println("Recommendation: REST DAY.");
            System.out.println("Everything is above your soreness threshold.");
        } else {
            System.out.print("Go train: ");
            for (int i = 0; i < plan.length(); i++) {
                System.out.print(plan.entry(i));
                if (i < plan.length() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        System.out.println("=========================================");

        scanner.close();
    }
}