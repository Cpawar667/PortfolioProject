package components.workoutplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;

/**
 * JUnit test fixture for {@code WorkoutPlanner}'s secondary methods.
 *
 * @author Christian Pawar
 */
public abstract class WorkoutPlannerTest extends WorkoutPlannerKernelTest {

    /**
     * Constants for MuscleGroup and SorenessLevel for easier reference.
     */
    protected static final MuscleGroup CHEST = MuscleGroup.CHEST;
    protected static final MuscleGroup BACK = MuscleGroup.BACK;
    protected static final MuscleGroup LEGS = MuscleGroup.LEGS;
    protected static final MuscleGroup ARMS = MuscleGroup.ARMS;
    protected static final MuscleGroup SHOULDERS = MuscleGroup.SHOULDERS;
    protected static final MuscleGroup CORE = MuscleGroup.CORE;

    protected static final SorenessLevel NONE = SorenessLevel.NONE;
    protected static final SorenessLevel LOW = SorenessLevel.LOW;
    protected static final SorenessLevel MEDIUM = SorenessLevel.MEDIUM;
    protected static final SorenessLevel HIGH = SorenessLevel.HIGH;

    /**
     * Helper to check if a Sequence contains a specific MuscleGroup. Rewritten
     * to use index-based loop for maximum compatibility.
     */
    private boolean sequenceContains(Sequence<MuscleGroup> seq,
            MuscleGroup item) {
        for (int i = 0; i < seq.length(); i++) {
            if (seq.entry(i) == item) { // Enums can be compared with ==
                return true;
            }
        }
        return false;
    }

    @Override
    protected abstract WorkoutPlanner constructorRef();

    @Override
    protected abstract WorkoutPlanner constructorTest();

    /**
     * Creates a REFERENCE WorkoutPlanner object.
     */
    private WorkoutPlanner createRef(MuscleGroup[] groups,
            SorenessLevel[] levels) {
        assert groups.length == levels.length : "Violation of: groups.length = levels.length";
        WorkoutPlanner planner = this.constructorRef();
        for (int i = 0; i < groups.length; i++) {
            planner.add(groups[i], levels[i]);
        }
        return planner;
    }

    /**
     * Creates a TEST WorkoutPlanner object.
     */
    private WorkoutPlanner createTest(MuscleGroup[] groups,
            SorenessLevel[] levels) {
        assert groups.length == levels.length : "Violation of: groups.length = levels.length";
        WorkoutPlanner planner = this.constructorTest();
        for (int i = 0; i < groups.length; i++) {
            planner.add(groups[i], levels[i]);
        }
        return planner;
    }

    /**
     * Creates a Map object for comparison.
     */
    private Map<MuscleGroup, SorenessLevel> createMap(MuscleGroup[] groups,
            SorenessLevel[] levels) {
        assert groups.length == levels.length : "Violation of: groups.length = levels.length";
        Map<MuscleGroup, SorenessLevel> map = new Map1L<>();
        for (int i = 0; i < groups.length; i++) {
            map.add(groups[i], levels[i]);
        }
        return map;
    }

    /*
     * Test cases for replaceValue (O(1)) -------------------------------------
     */

    @Test
    public final void testReplaceValueLowToHigh() {
        WorkoutPlanner pRef = this.createRef(new MuscleGroup[] { CHEST },
                new SorenessLevel[] { LOW });
        WorkoutPlanner pTest = this.createTest(new MuscleGroup[] { CHEST },
                new SorenessLevel[] { LOW });

        WorkoutPlanner pExpected = this.createRef(new MuscleGroup[] { CHEST },
                new SorenessLevel[] { HIGH });

        SorenessLevel oldLevel = pTest.replaceValue(CHEST, HIGH);

        assertEquals(pExpected, pTest);
        assertEquals(LOW, oldLevel);
        assertEquals(pRef.size(), pTest.size());
    }

    @Test
    public final void testReplaceValueMultiEntry() {
        MuscleGroup[] groups = { CHEST, BACK };
        SorenessLevel[] levels = { LOW, MEDIUM };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        SorenessLevel[] expectedLevels = { LOW, HIGH };
        WorkoutPlanner pExpected = this.createRef(groups, expectedLevels);

        SorenessLevel oldLevel = pTest.replaceValue(BACK, HIGH);

        assertEquals(pExpected, pTest);
        assertEquals(MEDIUM, oldLevel);
    }

    /*
     * Test cases for generateTrainingPlan (O(n)) -----------------------------
     */

    @Test
    public final void testGenerateTrainingPlanAllSuitable() {
        MuscleGroup[] groups = { CHEST, BACK, CORE };
        SorenessLevel[] levels = { NONE, LOW, LOW };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        SorenessLevel mandatoryRest = MEDIUM;

        Sequence<MuscleGroup> trainingPlan = pTest
                .generateTrainingPlan(mandatoryRest);

        assertEquals(pRef, pTest);
        for (MuscleGroup group : groups) {
            assertTrue("Plan should contain " + group,
                    this.sequenceContains(trainingPlan, group));
        }
        assertEquals(groups.length, trainingPlan.length());
    }

    @Test
    public final void testGenerateTrainingPlanNoneSuitable() {
        MuscleGroup[] groups = { LEGS, SHOULDERS };
        SorenessLevel[] levels = { HIGH, HIGH };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        SorenessLevel mandatoryRest = LOW;

        Sequence<MuscleGroup> trainingPlan = pTest
                .generateTrainingPlan(mandatoryRest);

        assertEquals(pRef, pTest);
        assertEquals(0, trainingPlan.length());
    }

    @Test
    public final void testGenerateTrainingPlanMixed() {
        MuscleGroup[] groups = { CHEST, BACK, LEGS, ARMS };
        SorenessLevel[] levels = { LOW, MEDIUM, HIGH, NONE };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        // Suitable (< MEDIUM): CHEST (LOW), ARMS (NONE)
        Sequence<MuscleGroup> trainingPlan = pTest.generateTrainingPlan(MEDIUM);

        assertEquals(pRef, pTest);
        assertEquals(2, trainingPlan.length());
        assertTrue(this.sequenceContains(trainingPlan, CHEST));
        assertTrue(this.sequenceContains(trainingPlan, ARMS));
        assertTrue(!this.sequenceContains(trainingPlan, BACK));
        assertTrue(!this.sequenceContains(trainingPlan, LEGS));
    }

    /*
     * Test cases for generateRestMap (O(n)) ----------------------------------
     */

    @Test
    public final void testGenerateRestMapAllResting() {
        MuscleGroup[] groups = { LEGS, SHOULDERS };
        SorenessLevel[] levels = { HIGH, MEDIUM };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        SorenessLevel mandatoryRest = MEDIUM;
        Map<MuscleGroup, SorenessLevel> expectedRestMap = this.createMap(groups,
                levels);

        Map<MuscleGroup, SorenessLevel> restMap = pTest
                .generateRestMap(mandatoryRest);

        assertEquals(pRef, pTest);
        assertEquals(expectedRestMap, restMap);
    }

    @Test
    public final void testGenerateRestMapNoneResting() {
        MuscleGroup[] groups = { CHEST, BACK };
        SorenessLevel[] levels = { NONE, LOW };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        SorenessLevel mandatoryRest = HIGH;

        Map<MuscleGroup, SorenessLevel> restMap = pTest
                .generateRestMap(mandatoryRest);

        assertEquals(pRef, pTest);
        assertTrue(restMap.size() == 0);
    }

    @Test
    public final void testGenerateRestMapMixed() {
        MuscleGroup[] groups = { CHEST, BACK, LEGS, ARMS };
        SorenessLevel[] levels = { LOW, MEDIUM, HIGH, NONE };
        WorkoutPlanner pRef = this.createRef(groups, levels);
        WorkoutPlanner pTest = this.createTest(groups, levels);

        // Resting (>= MEDIUM): BACK (MEDIUM), LEGS (HIGH)
        MuscleGroup[] expectedGroups = { BACK, LEGS };
        SorenessLevel[] expectedLevels = { MEDIUM, HIGH };
        Map<MuscleGroup, SorenessLevel> expectedRestMap = this
                .createMap(expectedGroups, expectedLevels);

        Map<MuscleGroup, SorenessLevel> restMap = pTest.generateRestMap(MEDIUM);

        assertEquals(pRef, pTest);
        assertEquals(expectedRestMap, restMap);
    }
}
