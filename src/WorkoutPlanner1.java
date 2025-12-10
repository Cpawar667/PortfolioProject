import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;

public final class WorkoutPlanner1 extends WorkoutPlannerSecondary {

    /**
     * The internal representation of the WorkoutPlanner, holding muscle groups
     * and their corresponding soreness levels.
     */
    private Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> map;

    /**
     * Constructs and returns a default instance of the component.
     */
    public WorkoutPlanner1() {
        // Establishes the convention: map is never null
        this.map = new Map1L<>();
    }

    /*
     * Kernel methods
     * -----------------------------------------------------------
     */

    @Override
    public void add(WorkoutPlanner.MuscleGroup muscleGroup,
            WorkoutPlanner.SorenessLevel sorenessLevel) {
        // Delegate to the Map component's kernel add method (O(1) amortized)
        this.map.add(muscleGroup, sorenessLevel);
    }

    @Override
    public WorkoutPlanner.SorenessLevel remove(
            WorkoutPlanner.MuscleGroup muscleGroup) {
        // Delegate to the Map component's kernel remove method
        // We remove the pair and return just the value (SorenessLevel)
        return this.map.remove(muscleGroup).value();
    }

    @Override
    public Pair<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> removeAny() {
        // Delegate to the Map component's kernel removeAny method (O(1) amortized)
        return this.map.removeAny();
    }

    @Override
    public WorkoutPlanner.SorenessLevel value(
            WorkoutPlanner.MuscleGroup muscleGroup) {
        // Delegate to the Map component's value method (O(1) amortized)
        return this.map.value(muscleGroup);
    }

    @Override
    public boolean hasKey(WorkoutPlanner.MuscleGroup muscleGroup) {
        // Delegate to the Map component's hasKey method (O(1) amortized)
        return this.map.hasKey(muscleGroup);
    }

    @Override
    public int size() {
        // Delegate to the Map component's size method (O(1))
        return this.map.size();
    }

    /*
     * Standard methods
     * ---------------------------------------------------------
     */

    @Override
    public WorkoutPlanner newInstance() {
        // Creates a new, empty instance of the same concrete type
        return new WorkoutPlanner1();
    }

    @Override
    public void clear() {
        // Delegate to the Map component's clear method (O(1))
        this.map.clear();
    }

    @Override
    public void transferFrom(WorkoutPlanner source) {
        // Cast the abstract source to the known concrete type to access its internal map
        WorkoutPlanner1 localSource = (WorkoutPlanner1) source;

        // Delegate to the Map component's transferFrom method (O(1))
        this.map.transferFrom(localSource.map);
    }

}
