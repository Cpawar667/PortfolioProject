package components.workoutplanner;

import components.map.Map;
import components.standard.Standard;

/**
 * WorkoutPlanner kernel interface.
 *
 * @author Christian Pawar
 */
public interface WorkoutPlannerKernel extends Standard<WorkoutPlanner> {

    /**
     * Adds a new muscle status pair to {@code this}.
     *
     * @param muscleGroup
     *            the muscle group (key) to be added
     * @param sorenessLevel
     *            the soreness level (value) associated with the key
     * @updates this
     * @requires muscleGroup is not in DOMAIN(this)
     * @ensures this = #this union {(muscleGroup, sorenessLevel)}
     */
    void add(MuscleGroup muscleGroup, SorenessLevel sorenessLevel);

    /**
     * Removes the muscle status pair with the given key from {@code this} and
     * returns the associated soreness level.
     *
     * @param muscleGroup
     *            the muscle group (key) to be removed
     * @return the soreness level that was associated with muscleGroup
     * @updates this
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures <pre>
     * remove = #this.value(muscleGroup) and
     * this = #this \ {(muscleGroup, remove)}
     * </pre>
     */
    SorenessLevel remove(MuscleGroup muscleGroup);

    /**
     * Removes an arbitrary muscle status pair from {@code this} and returns it.
     *
     * @return the removed pair (using Map.Pair from OSU Components)
     * @updates this
     * @requires |this| > 0
     * @ensures <pre>
     * removeAny is in #this and
     * this = #this \ {removeAny}
     * </pre>
     */
    Map.Pair<MuscleGroup, SorenessLevel> removeAny();

    /**
     * Reports the soreness level associated with the given muscle group key in
     * {@code this}.
     *
     * @param muscleGroup
     *            the muscle group (key) to query
     * @return the soreness level associated with the muscle group
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures value = this.value(muscleGroup)
     */
    SorenessLevel value(MuscleGroup muscleGroup);

    /**
     * Reports whether or not {@code muscleGroup} is in the domain of
     * {@code this}.
     *
     * @param muscleGroup
     *            the muscle group (key) to check
     * @return true if muscleGroup is in DOMAIN(this), false otherwise
     * @ensures hasKey = (muscleGroup is in DOMAIN(this))
     */
    boolean hasKey(MuscleGroup muscleGroup);

    /**
     * Reports the number of muscle status pairs in {@code this}.
     *
     * @return the size of {@code this}
     * @ensures size = |this|
     */
    int size();
}