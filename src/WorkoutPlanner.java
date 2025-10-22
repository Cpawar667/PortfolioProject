import components.map.Map;
import components.sequence.Sequence;

/**
 * WorkoutPlanner enhanced interface. * This interface extends the
 * WorkoutPlannerKernel by adding secondary methods necessary for practical use,
 * such as updating soreness levels and generating recommended training and rest
 * plans. * @author Christian Pawar
 * 
 * @param <M>
 *            type of the domain (MuscleGroup) entries
 * @param <S>
 *            type of the range (SorenessLevel) entries
 */
public interface WorkoutPlanner<M, S> extends WorkoutPlannerKernel<M, S> {

    /**
     * Replaces the soreness level associated with the given muscle group key
     * with {@code newSorenessLevel} and returns the old soreness level.
     * * @param muscleGroup the muscle group (key) whose value is to be replaced
     * 
     * @param newSorenessLevel
     *            the new soreness level (value)
     * @return the old soreness level
     * @updates this
     * @requires muscleGroup is in DOMAIN(this)
     * @ensures <pre>
     * this = (#this \ {(muscleGroup, oldSorenessLevel)}) union {(muscleGroup, newSorenessLevel)}  and
     * oldSorenessLevel = #this.value(muscleGroup)
     * </pre>
     */
    S replaceValue(M muscleGroup, S newSorenessLevel);

    /**
     * Generates a workout plan consisting of a Sequence of muscle groups that
     * are suitable for training (where soreness is less than the specified
     * mandatory rest level). * @param mandatoryRestLevel the SorenessLevel that
     * indicates a mandatory rest/avoid status
     * 
     * @return a Sequence of muscle groups recommended for today's training
     * @ensures <pre>
     * generateTrainingPlan = muscleGroups that satisfy the training condition
     * this = #this
     * </pre>
     */
    Sequence<M> generateTrainingPlan(S mandatoryRestLevel);

    /**
     * Generates a Map of muscle groups that are too sore to train, along with
     * their recommended rest messages. * @param mandatoryRestLevel the
     * SorenessLevel that indicates a mandatory rest/avoid status
     * 
     * @return a Map containing muscle groups that require rest
     * @ensures <pre>
     * generateRestMap = muscleGroups that satisfy the rest condition
     * this = #this
     * </pre>
     */
    Map<M, S> generateRestMap(S mandatoryRestLevel);
}
