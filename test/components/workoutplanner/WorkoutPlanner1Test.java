package components.workoutplanner;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;

/**
 * Concrete test suite for WorkoutPlanner1.
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
            return new WorkoutPlannerReference();
        }

        @Override
        public void transferFrom(WorkoutPlanner source) {
            // Needed if tests use transferFrom
            WorkoutPlannerReference localSource = (WorkoutPlannerReference) source;
            this.map.transferFrom(localSource.map);
        }

        // --- Secondary Methods (Not tested on Reference, but must exist) ---
        @Override
        public SorenessLevel replaceValue(MuscleGroup m, SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference does not implement secondary methods");
        }

        @Override
        public Sequence<MuscleGroup> generateTrainingPlan(SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference does not implement secondary methods");
        }

        @Override
        public Map<MuscleGroup, SorenessLevel> generateRestMap(
                SorenessLevel s) {
            throw new UnsupportedOperationException(
                    "Reference does not implement secondary methods");
        }

        // --- STANDARD METHODS (REQUIRED FOR TESTING) ---

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            // We allow comparison with ANY implementation of WorkoutPlanner
            if (!(obj instanceof WorkoutPlanner)) {
                return false;
            }
            WorkoutPlanner k = (WorkoutPlanner) obj;
            if (this.size() != k.size()) {
                return false;
            }
            // Use toString comparison for simplicity, provided toString is consistent
            return this.toString().equals(k.toString());
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public String toString() {
            // Build string strictly matching the format of WorkoutPlannerSecondary
            StringBuilder sb = new StringBuilder("{");
            Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();
            boolean first = true;

            // We need to iterate over the map. Since Map is iterable in OSU components?
            // Actually, Map1L is Iterable.
            for (Map.Pair<MuscleGroup, SorenessLevel> p : this.map) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(p.key() + "=" + p.value());
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
    }
}