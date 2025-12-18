package components.workoutplanner;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * Layered abstract implementation of the {@code WorkoutPlanner} interface.
 *
 * @author Christian Pawar
 */
public abstract class WorkoutPlannerSecondary implements WorkoutPlanner {

    /*
     * Secondary Methods
     */

    @Override
    public final SorenessLevel replaceValue(MuscleGroup muscleGroup,
            SorenessLevel newSorenessLevel) {
        assert this.hasKey(
                muscleGroup) : "Violation of: muscleGroup is in DOMAIN(this)";
        SorenessLevel oldLevel = this.remove(muscleGroup);
        this.add(muscleGroup, newSorenessLevel);
        return oldLevel;
    }

    @Override
    public final Sequence<MuscleGroup> generateTrainingPlan(
            SorenessLevel mandatoryRestLevel) {
        Sequence<MuscleGroup> trainingPlan = new Sequence1L<>();
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Move all entries to temp, checking criteria along the way
        while (this.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            if (entry.value().getLevel() < mandatoryRestLevel.getLevel()) {
                trainingPlan.add(trainingPlan.length(), entry.key());
            }
            temp.add(entry.key(), entry.value());
        }

        // Restore all entries from temp back to this
        while (temp.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = temp.removeAny();
            this.add(entry.key(), entry.value());
        }

        return trainingPlan;
    }

    @Override
    public final Map<MuscleGroup, SorenessLevel> generateRestMap(
            SorenessLevel mandatoryRestLevel) {
        Map<MuscleGroup, SorenessLevel> restMap = new Map1L<>();
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();

        // Move all entries to temp, checking criteria along the way
        while (this.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = this.removeAny();
            if (entry.value().getLevel() >= mandatoryRestLevel.getLevel()) {
                restMap.add(entry.key(), entry.value());
            }
            temp.add(entry.key(), entry.value());
        }

        // Restore all entries from temp back to this
        while (temp.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> entry = temp.removeAny();
            this.add(entry.key(), entry.value());
        }
        return restMap;
    }

    /*
     * Standard Methods Overrides (equals, hashCode, toString)
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof WorkoutPlanner)) {
            return false;
        }
        WorkoutPlanner k = (WorkoutPlanner) obj;
        if (this.size() != k.size()) {
            return false;
        }
        return this.toString().equals(k.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Map<MuscleGroup, SorenessLevel> temp = new Map1L<>();
        boolean first = true;
        while (this.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> p = this.removeAny();
            if (!first) {
                sb.append(", ");
            }
            sb.append(p.key() + "=" + p.value());
            first = false;
            temp.add(p.key(), p.value());
        }
        sb.append("}");

        // Restore
        while (temp.size() > 0) {
            Pair<MuscleGroup, SorenessLevel> p = temp.removeAny();
            this.add(p.key(), p.value());
        }
        return sb.toString();
    }
}
