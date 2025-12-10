
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * JUnit test fixture for {@code WorkoutPlanner}'s secondary methods
 * (implemented in WorkoutPlannerSecondary).
 *
 * This abstract class provides test cases for {@code replaceValue},
 * {@code generateTrainingPlan}, and {@code generateRestMap}.
 *
 * @author Christian Pawar
 */
public abstract class WorkoutPlannerTest {

    /**
     * Constants for MuscleGroup and SorenessLevel for easier reference.
     */
    protected static final WorkoutPlanner.MuscleGroup CHEST = WorkoutPlanner.MuscleGroup.CHEST;
    protected static final WorkoutPlanner.MuscleGroup BACK = WorkoutPlanner.MuscleGroup.BACK;
    protected static final WorkoutPlanner.MuscleGroup LEGS = WorkoutPlanner.MuscleGroup.LEGS;
    protected static final WorkoutPlanner.MuscleGroup ARMS = WorkoutPlanner.MuscleGroup.ARMS;
    protected static final WorkoutPlanner.MuscleGroup SHOULDERS = WorkoutPlanner.MuscleGroup.SHOULDERS;
    protected static final WorkoutPlanner.MuscleGroup CORE = WorkoutPlanner.MuscleGroup.CORE;

    protected static final WorkoutPlanner.SorenessLevel NONE = WorkoutPlanner.SorenessLevel.NONE;
    protected static final WorkoutPlanner.SorenessLevel LOW = WorkoutPlanner.SorenessLevel.LOW;
    protected static final WorkoutPlanner.SorenessLevel MEDIUM = WorkoutPlanner.SorenessLevel.MEDIUM;
    protected static final WorkoutPlanner.SorenessLevel HIGH = WorkoutPlanner.SorenessLevel.HIGH;

    /**
     * Invokes the appropriate {@code WorkoutPlanner} constructor for the
     * reference implementation and returns the result.
     *
     * @return the reference implementation
     */
    protected abstract WorkoutPlanner constructorRef();

    /**
     * Invokes the appropriate {@code WorkoutPlanner} constructor for the
     * implementation under test and returns the result.
     *
     * @return the implementation under test
     */
    protected abstract WorkoutPlanner constructorTest();

    /**
     * Creates a {@code WorkoutPlanner} object with the given muscle group states.
     *
     * @param groups array of muscle groups
     * @param levels array of soreness levels
     * @return a reference WorkoutPlanner object
     * @requires groups.length = levels.length
     */
    private WorkoutPlanner createPlanner(WorkoutPlanner.MuscleGroup[] groups,
            WorkoutPlanner.SorenessLevel[] levels) {
        assert groups.length == levels.length : "Violation of: groups.length = levels.length";
        WorkoutPlanner planner = this.constructorRef();
        for (int i = 0; i < groups.length; i++) {
            // Note: add is a kernel method and must be implemented by the concrete test fixture
            planner.add(groups[i], levels[i]);
        }
        return planner;
    }

    /**
     * Creates a Map object from the given muscle group states for comparison.
     *
     * @param groups array of muscle groups
     * @param levels array of soreness levels
     * @return a Map object
     * @requires groups.length = levels.length
     */
    private Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> createMap(
            WorkoutPlanner.MuscleGroup[] groups,
            WorkoutPlanner.SorenessLevel[] levels) {
        assert groups.length == levels.length : "Violation of: groups.length = levels.length";
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> map = new Map1L<>();
        for (int i = 0; i < groups.length; i++) {
            map.add(groups[i], levels[i]);
        }
        return map;
    }

    /*
     * Test cases for replaceValue (O(1)) -------------------------------------
     */

    /**
     * Test replaceValue replacing LOW with HIGH soreness.
     */
    @Test
    public final void testReplaceValueLowToHigh() {
        // Setup initial state: CHEST -> LOW
        WorkoutPlanner pRef = this.createPlanner(
                new WorkoutPlanner.MuscleGroup[] { CHEST },
                new WorkoutPlanner.SorenessLevel[] { LOW });
        WorkoutPlanner pTest = this.createPlanner(
                new WorkoutPlanner.MuscleGroup[] { CHEST },
                new WorkoutPlanner.SorenessLevel[] { LOW });

        // Setup expected state: CHEST -> HIGH
        WorkoutPlanner pExpected = this.createPlanner(
                new WorkoutPlanner.MuscleGroup[] { CHEST },
                new WorkoutPlanner.SorenessLevel[] { HIGH });

        // Call method
        WorkoutPlanner.SorenessLevel oldLevel = pTest.replaceValue(CHEST, HIGH);

        // Evaluation: Check post-condition and returned value
        assertEquals(pExpected, pTest);
        assertEquals(LOW, oldLevel);
        assertEquals(pRef.size(), pTest.size());
    }

    /**
     * Test replaceValue replacing the value in a multi-entry map.
     */
    @Test
    public final void testReplaceValueMultiEntry() {
        // Initial state: {CHEST -> LOW, BACK -> MEDIUM}
        WorkoutPlanner.MuscleGroup[] groups = { CHEST, BACK };
        WorkoutPlanner.SorenessLevel[] levels = { LOW, MEDIUM };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Expected state: {CHEST -> LOW, BACK -> HIGH}
        WorkoutPlanner.SorenessLevel[] expectedLevels = { LOW, HIGH };
        WorkoutPlanner pExpected = this.createPlanner(groups, expectedLevels);

        // Call method on BACK
        WorkoutPlanner.SorenessLevel oldLevel = pTest.replaceValue(BACK, HIGH);

        // Evaluation
        assertEquals(pExpected, pTest);
        assertEquals(MEDIUM, oldLevel);
    }

    /*
     * Test cases for generateTrainingPlan (O(n)) -----------------------------
     */

    /**
     * Test generateTrainingPlan when all muscle groups are suitable (NONE/LOW).
     */
    @Test
    public final void testGenerateTrainingPlanAllSuitable() {
        // Initial state: {CHEST->NONE, BACK->LOW, CORE->LOW}
        WorkoutPlanner.MuscleGroup[] groups = { CHEST, BACK, CORE };
        WorkoutPlanner.SorenessLevel[] levels = { NONE, LOW, LOW };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: MEDIUM (level 2). All entries are < 2.
        WorkoutPlanner.SorenessLevel mandatoryRest = MEDIUM;

        // Call method
        Sequence<WorkoutPlanner.MuscleGroup> trainingPlan = pTest
                .generateTrainingPlan(mandatoryRest);

        // Evaluation: Check that the original planner state is unchanged
        assertEquals(pRef, pTest);

        // Check that all suitable groups are present in the plan
        for (WorkoutPlanner.MuscleGroup group : groups) {
            assertTrue(trainingPlan.contains(group));
        }
        assertEquals(groups.length, trainingPlan.length());
    }

    /**
     * Test generateTrainingPlan when no muscle groups are suitable (all HIGH).
     */
    @Test
    public final void testGenerateTrainingPlanNoneSuitable() {
        // Initial state: {LEGS->HIGH, SHOULDERS->HIGH}
        WorkoutPlanner.MuscleGroup[] groups = { LEGS, SHOULDERS };
        WorkoutPlanner.SorenessLevel[] levels = { HIGH, HIGH };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: LOW (level 1). All entries are >= 1.
        WorkoutPlanner.SorenessLevel mandatoryRest = LOW;

        // Call method
        Sequence<WorkoutPlanner.MuscleGroup> trainingPlan = pTest
                .generateTrainingPlan(mandatoryRest);

        // Evaluation
        assertEquals(pRef, pTest);
        assertEquals(0, trainingPlan.length());
    }

    /**
     * Test generateTrainingPlan with mixed suitable and resting groups.
     */
    @Test
    public final void testGenerateTrainingPlanMixed() {
        // Initial state: {CHEST->LOW, BACK->MEDIUM, LEGS->HIGH, ARMS->NONE}
        WorkoutPlanner.MuscleGroup[] groups = { CHEST, BACK, LEGS, ARMS };
        WorkoutPlanner.SorenessLevel[] levels = { LOW, MEDIUM, HIGH, NONE };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: MEDIUM (level 2).
        // Suitable (level < 2): CHEST (1), ARMS (0)
        // Resting (level >= 2): BACK (2), LEGS (3)

        // Call method
        Sequence<WorkoutPlanner.MuscleGroup> trainingPlan = pTest
                .generateTrainingPlan(MEDIUM);

        // Evaluation: Check that the original planner state is unchanged
        assertEquals(pRef, pTest);

        // Check plan contents
        assertEquals(2, trainingPlan.length());
        assertTrue(trainingPlan.contains(CHEST));
        assertTrue(trainingPlan.contains(ARMS));
        assertTrue(!trainingPlan.contains(BACK));
        assertTrue(!trainingPlan.contains(LEGS));
    }

    /*
     * Test cases for generateRestMap (O(n)) ----------------------------------
     */

    /**
     * Test generateRestMap when all muscle groups require rest (HIGH/MEDIUM).
     */
    @Test
    public final void testGenerateRestMapAllResting() {
        // Initial state: {LEGS->HIGH, SHOULDERS->MEDIUM}
        WorkoutPlanner.MuscleGroup[] groups = { LEGS, SHOULDERS };
        WorkoutPlanner.SorenessLevel[] levels = { HIGH, MEDIUM };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: MEDIUM (level 2). All entries are >= 2.
        WorkoutPlanner.SorenessLevel mandatoryRest = MEDIUM;

        // Expected map: {LEGS->HIGH, SHOULDERS->MEDIUM}
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> expectedRestMap = this
                .createMap(groups, levels);

        // Call method
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restMap = pTest
                .generateRestMap(mandatoryRest);

        // Evaluation
        assertEquals(pRef, pTest);
        assertEquals(expectedRestMap, restMap);
    }

    /**
     * Test generateRestMap when no muscle groups require rest (all NONE/LOW).
     */
    @Test
    public final void testGenerateRestMapNoneResting() {
        // Initial state: {CHEST->NONE, BACK->LOW}
        WorkoutPlanner.MuscleGroup[] groups = { CHEST, BACK };
        WorkoutPlanner.SorenessLevel[] levels = { NONE, LOW };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: HIGH (level 3). None of the entries are >= 3.
        WorkoutPlanner.SorenessLevel mandatoryRest = HIGH;

        // Call method
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restMap = pTest
                .generateRestMap(mandatoryRest);

        // Evaluation
        assertEquals(pRef, pTest);
        assertTrue(restMap.size() == 0);
    }

    /**
     * Test generateRestMap with mixed suitable and resting groups.
     */
    @Test
    public final void testGenerateRestMapMixed() {
        // Initial state: {CHEST->LOW, BACK->MEDIUM, LEGS->HIGH, ARMS->NONE}
        WorkoutPlanner.MuscleGroup[] groups = { CHEST, BACK, LEGS, ARMS };
        WorkoutPlanner.SorenessLevel[] levels = { LOW, MEDIUM, HIGH, NONE };
        WorkoutPlanner pRef = this.createPlanner(groups, levels);
        WorkoutPlanner pTest = this.createPlanner(groups, levels);

        // Mandatory rest level: MEDIUM (level 2).
        // Resting (level >= 2): BACK (2), LEGS (3)

        // Expected map: {BACK->MEDIUM, LEGS->HIGH}
        WorkoutPlanner.MuscleGroup[] expectedGroups = { BACK, LEGS };
        WorkoutPlanner.SorenessLevel[] expectedLevels = { MEDIUM, HIGH };
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> expectedRestMap = this
                .createMap(expectedGroups, expectedLevels);

        // Call method
        Map<WorkoutPlanner.MuscleGroup, WorkoutPlanner.SorenessLevel> restMap = pTest
                .generateRestMap(MEDIUM);

        // Evaluation
        assertEquals(pRef, pTest);
        assertEquals(expectedRestMap, restMap);
    }
}}
