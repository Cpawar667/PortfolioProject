package workoutplanner;

import components.map.Map;
import components.map.Map1L;

/**
 * Concrete test suite for WorkoutPlanner1. * By extending WorkoutPlannerTest
 * (instead of just WorkoutPlannerKernelTest), this class will run ALL tests: 1.
 * The Kernel tests (inherited from WorkoutPlannerKernelTest) 2. The Secondary
 * tests (inherited from WorkoutPlannerTest)
 */
public class WorkoutPlanner1Test extends WorkoutPlannerTest {

    @Override
    protected WorkoutPlanner constructorRef() {
        // Return the reference implementation
        return new WorkoutPlannerReference();
    }

    @Override
    protected WorkoutPlanner constructorTest() {
        // Return the actual component you implemented
        return new WorkoutPlanner1();
    }

    /**
     * Simple reference implementation wrapping a Map1L to serve as an oracle.
     * Note: This must implement WorkoutPlanner (the full interface), not just
     * WorkoutPlannerKernel.
     */
    private static class WorkoutPlannerReference implements WorkoutPlanner {
        private Map<MuscleGroup, SorenessLevel> map = new Map1L<>();

        // --- Kernel Methods (Delegated to Map) ---
        @Override
        public void add(MuscleGroup m, SorenessLevel s) {
            this.map.add(m, s);
        }

        @Override
        public SorenessLevel remove(MuscleGroup m) {
            return this.map.remove(m).value();
        }

        @Override
        public Map.Pair<MuscleGroup, SorenessLevel> removeAny() {
            return this.map.removeAny();
        }

        @Override
        public SorenessLevel value(MuscleGroup m) {
            return this.map.value(m);
        }

        @Override
        public boolean hasKey(MuscleGroup m) {
            return this.map.hasKey(m);
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public WorkoutPlanner newInstance() {
            return new WorkoutPlanner1();
        }

        @Override
        public void transferFrom(WorkoutPlanner source) {
            // In a strict reference implementation, you might implement this,
            // but for secondary method testing, the oracle just needs to hold state.
            // If necessary, we can cast and move map contents.
        }

        // --- Secondary Methods ---
        // We generally DO NOT implement secondary methods in the reference
        // if we are only testing the kernel. However, since WorkoutPlannerTest
        // compares pTest results against pRef results, pRef needs to be a valid object.
        //
        // Ideally, WorkoutPlannerTest creates the reference object and populates it
        // using KERNEL methods (add), so we don't actually need to implement
        // logic for replaceValue/etc inside this Reference class *unless* the test
        // calls them on pRef.
        //
        // Checking WorkoutPlannerTest: It calls createPlanner -> uses add().
        // It does NOT call secondary methods on pRef. It only uses pRef to compare state.
        // So we can leave these empty or throw exceptions if called.

        @Override
        public SorenessLevel replaceValue(MuscleGroup m, SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference should not call secondary methods");
        }

        @Override
        public components.sequence.Sequence<MuscleGroup> generateTrainingPlan(
                SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference should not call secondary methods");
        }

        @Override
        public components.map.Map<MuscleGroup, SorenessLevel> generateRestMap(
                SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference should not call secondary methods");
        }
    }
}
