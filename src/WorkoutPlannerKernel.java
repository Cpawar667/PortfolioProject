import components.standard.Standard;

/**
 * WorkoutPlanner kernel interface. * @author Christian Pawar
 * 
 * @param <M>
 *            type of the domain (MuscleGroup) entries
 * @param <S>
 *            type of the range (SorenessLevel) entries * @mathmodel <pre>
 * type WorkoutPlanner is (
 * (M, S) is a partial function
 * )
 * </pre>
 */
public interface WorkoutPlannerKernel<M, S>
        extends Standard<WorkoutPlanner<M, S>> {

    /**
     * Nested interface for a single key-value pair in the WorkoutPlanner.
     */
    public interface Pair<M, S> {
        /**
         * Reports the key of the pair. * @return the key
         * 
         * @ensures key = this.key
         */
        M key();

        /**
         * Reports the value of the pair. * @return the value
         * 
         * @ensures value = this.value
         */
        S value();
    }

    /**
     * Adds a new muscle status pair to {@code this}. * @param muscleGroup the
     * muscle group (key) to be added
     * 
     * @param sorenessLevel
     *            the soreness level (value) associated with the key
     * @updates this
     * @requires muscleGroup is not in DOMAIN(this)
     * @ensures this = #this union {(muscleGroup, sorenessLevel)}
     */
    void add(M muscleGroup, S sorenessLevel);

    /**
     * Removes a muscle status pair with the given key from {@code this} and
     * returns it. * @param muscleGroup the muscle group (key) to be removed
     * 
     * @return the removed pair
     * @updates this
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures <pre>
     * this = #this \ {remove.key, remove.value}  and
     * remove.key = muscleGroup  and
     * remove.value = #this.value(muscleGroup)
     * </pre>
     */
    Pair<M, S> remove(M muscleGroup);

    /**
     * Removes an arbitrary muscle status pair from {@code this} and returns it.
     * * @return the removed pair
     * 
     * @updates this
     * @requires |this| > 0
     * @ensures <pre>
     * this = #this \ {remove.key, remove.value}  and
     * remove is in #this
     * </pre>
     */
    Pair<M, S> removeAny();

    /**
     * Reports the soreness level associated with the given muscle group key in
     * {@code this}. * @param muscleGroup the muscle group (key) to query
     * 
     * @return the soreness level associated with the muscle group
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures value = this.value(muscleGroup)
     */
    S value(M muscleGroup);

    /**
     * Reports whether or not {@code muscleGroup} is in the domain of
     * {@code this}. * @param muscleGroup the muscle group (key) to check
     * 
     * @return true if muscleGroup is in DOMAIN(this), false otherwise
     * @ensures hasKey = (muscleGroup is in DOMAIN(this))
     */
    boolean hasKey(M muscleGroup);

    /**
     * Reports the number of muscle status pairs in {@code this}. * @return the
     * size of {@code this}
     * 
     * @ensures size = |this|
     */
    int size();
}
