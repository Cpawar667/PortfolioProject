package workoutplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map.Pair;

/**
 * JUnit test fixture for {@code WorkoutPlannerKernel}'s kernel methods.
 *
 * This abstract class provides test cases for {@code add}, {@code remove},
 * {@code removeAny}, {@code value}, {@code hasKey}, {@code size}, and the
 * required {@code Standard} methods.
 *
 * @author Christian Pawar
 */
public abstract class WorkoutPlannerKernelTest {

        /**
         * Constants for MuscleGroup and SorenessLevel for easier reference.
         */
        protected static final WorkoutPlannerKernel.MuscleGroup CHEST = WorkoutPlannerKernel.MuscleGroup.CHEST;
        protected static final WorkoutPlannerKernel.MuscleGroup BACK = WorkoutPlannerKernel.MuscleGroup.BACK;
        protected static final WorkoutPlannerKernel.MuscleGroup LEGS = WorkoutPlannerKernel.MuscleGroup.LEGS;
        protected static final WorkoutPlannerKernel.MuscleGroup ARMS = WorkoutPlannerKernel.MuscleGroup.ARMS;
        protected static final WorkoutPlannerKernel.MuscleGroup SHOULDERS = WorkoutPlannerKernel.MuscleGroup.SHOULDERS;
        protected static final WorkoutPlannerKernel.MuscleGroup CORE = WorkoutPlannerKernel.MuscleGroup.CORE;

        protected static final WorkoutPlannerKernel.SorenessLevel NONE = WorkoutPlannerKernel.SorenessLevel.NONE;
        protected static final WorkoutPlannerKernel.SorenessLevel LOW = WorkoutPlannerKernel.SorenessLevel.LOW;
        protected static final WorkoutPlannerKernel.SorenessLevel MEDIUM = WorkoutPlannerKernel.SorenessLevel.MEDIUM;
        protected static final WorkoutPlannerKernel.SorenessLevel HIGH = WorkoutPlannerKernel.SorenessLevel.HIGH;

        /**
         * Invokes the appropriate {@code WorkoutPlannerKernel} constructor for
         * the reference implementation and returns the result.
         *
         * @return the reference implementation
         */
        protected abstract WorkoutPlannerKernel constructorRef();

        /**
         * Invokes the appropriate {@code WorkoutPlannerKernel} constructor for
         * the implementation under test and returns the result.
         *
         * @return the implementation under test
         */
        protected abstract WorkoutPlannerKernel constructorTest();

        /**
         * Creates a {@code WorkoutPlannerKernel} object with the given muscle
         * group states.
         *
         * @param groups
         *                array of muscle groups
         * @param levels
         *                array of soreness levels
         * @return a reference WorkoutPlannerKernel object
         * @requires groups.length = levels.length
         */
        private WorkoutPlannerKernel createPlanner(
                        WorkoutPlannerKernel.MuscleGroup[] groups,
                        WorkoutPlannerKernel.SorenessLevel[] levels) {
                assert groups.length == levels.length : "Violation of: groups.length = levels.length";
                WorkoutPlannerKernel planner = this.constructorRef();
                for (int i = 0; i < groups.length; i++) {
                        // Use the kernel method add
                        planner.add(groups[i], levels[i]);
                }
                return planner;
        }

        /*
         * Test cases for constructor/initial state
         * --------------------------------
         */

        /**
         * Test constructor creates an empty planner (size 0).
         */
        @Test
        public final void testConstructor() {
                // Setup expected state (empty)
                WorkoutPlannerKernel pExpected = this.constructorRef();

                // Call method
                WorkoutPlannerKernel pTest = this.constructorTest();

                // Evaluation
                assertEquals(pExpected, pTest);
        }

        /*
         * Test cases for add
         * ------------------------------------------------------
         */

        /**
         * Test add to an empty planner.
         */
        @Test
        public final void testAddEmpty() {
                // Setup initial state (empty)
                WorkoutPlannerKernel pRef = this.constructorRef();
                WorkoutPlannerKernel pTest = this.constructorTest();

                // Setup expected state: {LEGS -> HIGH}
                WorkoutPlannerKernel pExpected = this.constructorRef();
                pExpected.add(LEGS, HIGH);

                // Call method
                pTest.add(LEGS, HIGH);

                // Evaluation
                assertEquals(pExpected, pTest);
                assertEquals(pRef.size() + 1, pTest.size());
        }

        /**
         * Test add to a non-empty planner.
         */
        @Test
        public final void testAddNonEmpty() {
                // Setup initial state: {CHEST -> LOW}
                WorkoutPlannerKernel pRef = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] {
                                                CHEST },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                LOW });
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] {
                                                CHEST },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                LOW });

                // Setup expected state: {CHEST -> LOW, BACK -> MEDIUM}
                WorkoutPlannerKernel pExpected = this.constructorRef();
                pExpected.add(CHEST, LOW);
                pExpected.add(BACK, MEDIUM);

                // Call method
                pTest.add(BACK, MEDIUM);

                // Evaluation
                assertEquals(pExpected, pTest);
                assertEquals(pRef.size() + 1, pTest.size());
        }

        /*
         * Test cases for remove
         * ---------------------------------------------------
         */

        /**
         * Test remove when only one entry is present.
         */
        @Test
        public final void testRemoveSingleEntry() {
                // Setup initial state: {CORE -> NONE}
                WorkoutPlannerKernel pRef = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { CORE },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                NONE });
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { CORE },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                NONE });

                // Setup expected state (empty)
                WorkoutPlannerKernel pExpected = this.constructorRef();

                // Call method
                WorkoutPlannerKernel.SorenessLevel removed = pTest.remove(CORE);

                // Evaluation
                assertEquals(pExpected, pTest);
                assertEquals(NONE, removed);
                assertEquals(pRef.size() - 1, pTest.size());
        }

        /**
         * Test remove from a multi-entry planner.
         */
        @Test
        public final void testRemoveMultiEntry() {
                // Initial state: {ARMS -> HIGH, SHOULDERS -> MEDIUM}
                WorkoutPlannerKernel.MuscleGroup[] groups = { ARMS, SHOULDERS };
                WorkoutPlannerKernel.SorenessLevel[] levels = { HIGH, MEDIUM };
                WorkoutPlannerKernel pRef = this.createPlanner(groups, levels);
                WorkoutPlannerKernel pTest = this.createPlanner(groups, levels);

                // Expected state: {ARMS -> HIGH}
                WorkoutPlannerKernel pExpected = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { ARMS },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                HIGH });

                // Call method (remove SHOULDERS)
                WorkoutPlannerKernel.SorenessLevel removed = pTest
                                .remove(SHOULDERS);

                // Evaluation
                assertEquals(pExpected, pTest);
                assertEquals(MEDIUM, removed);
                assertEquals(pRef.size() - 1, pTest.size());
        }

        /*
         * Test cases for removeAny
         * ------------------------------------------------
         */

        /**
         * Test removeAny when only one entry is present.
         */
        @Test
        public final void testRemoveAnySingleEntry() {
                // Initial state: {ARMS -> HIGH}
                WorkoutPlannerKernel pRef = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { ARMS },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                HIGH });
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { ARMS },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                HIGH });

                // Expected state (empty)
                WorkoutPlannerKernel pExpected = this.constructorRef();

                // Call method
                Pair<WorkoutPlannerKernel.MuscleGroup, WorkoutPlannerKernel.SorenessLevel> removed = pTest
                                .removeAny();

                // Evaluation
                assertEquals(pExpected, pTest);
                assertEquals(ARMS, removed.key());
                assertEquals(HIGH, removed.value());
                assertEquals(pRef.size() - 1, pTest.size());
        }

        /**
         * Test removeAny from a multi-entry planner.
         */
        @Test
        public final void testRemoveAnyMultiEntry() {
                // Initial state: {CHEST -> LOW, BACK -> MEDIUM}
                WorkoutPlannerKernel.MuscleGroup[] groups = { CHEST, BACK };
                WorkoutPlannerKernel.SorenessLevel[] levels = { LOW, MEDIUM };
                WorkoutPlannerKernel pRef = this.createPlanner(groups, levels);
                WorkoutPlannerKernel pTest = this.createPlanner(groups, levels);

                // Call method
                Pair<WorkoutPlannerKernel.MuscleGroup, WorkoutPlannerKernel.SorenessLevel> removed = pTest
                                .removeAny();

                // Evaluation
                // Check that the removed element was in the original set
                boolean wasRemoved = (removed.key() == CHEST
                                && removed.value() == LOW)
                                || (removed.key() == BACK
                                                && removed.value() == MEDIUM);
                assertTrue(wasRemoved);

                // Check that the remaining planner contains the other element
                assertEquals(pRef.size() - 1, pTest.size());
                assertTrue(pTest.size() == 1);
        }

        /*
         * Test cases for value and hasKey
         * -----------------------------------------
         */

        /**
         * Test value retrieves the correct soreness level.
         */
        @Test
        public final void testValue() {
                // Initial state: {LEGS -> HIGH, ARMS -> NONE}
                WorkoutPlannerKernel.MuscleGroup[] groups = { LEGS, ARMS };
                WorkoutPlannerKernel.SorenessLevel[] levels = { HIGH, NONE };
                WorkoutPlannerKernel pRef = this.createPlanner(groups, levels);
                WorkoutPlannerKernel pTest = this.createPlanner(groups, levels);

                // Call method
                WorkoutPlannerKernel.SorenessLevel soreness = pTest.value(ARMS);

                // Evaluation (state must be preserved)
                assertEquals(pRef, pTest);
                assertEquals(NONE, soreness);
        }

        /**
         * Test hasKey returns true for an existing key.
         */
        @Test
        public final void testHasKeyTrue() {
                // Initial state: {LEGS -> HIGH}
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { LEGS },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                HIGH });

                // Evaluation
                assertTrue(pTest.hasKey(LEGS));
        }

        /**
         * Test hasKey returns false for a non-existing key.
         */
        @Test
        public final void testHasKeyFalse() {
                // Initial state: {LEGS -> HIGH}
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { LEGS },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                HIGH });

                // Evaluation
                assertFalse(pTest.hasKey(CHEST));
        }

        /*
         * Test cases for size
         * -----------------------------------------------------
         */

        /**
         * Test size on an empty planner.
         */
        @Test
        public final void testSizeEmpty() {
                WorkoutPlannerKernel pTest = this.constructorTest();
                assertEquals(0, pTest.size());
        }

        /**
         * Test size on a planner with multiple entries.
         */
        @Test
        public final void testSizeMultiEntry() {
                WorkoutPlannerKernel.MuscleGroup[] groups = { CHEST, BACK, LEGS,
                                CORE };
                WorkoutPlannerKernel.SorenessLevel[] levels = { LOW, MEDIUM,
                                HIGH, NONE };
                WorkoutPlannerKernel pTest = this.createPlanner(groups, levels);
                assertEquals(4, pTest.size());
        }

        /*
         * Test cases for Standard methods
         * -----------------------------------------
         */

        /**
         * Test newInstance creates a new, empty planner.
         */
        @Test
        public final void testNewInstance() {
                // Setup initial state (non-empty)
                WorkoutPlannerKernel pRef = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] {
                                                CHEST },
                                new WorkoutPlannerKernel.SorenessLevel[] {
                                                LOW });
                WorkoutPlannerKernel pExpected = this.constructorRef();

                // Call method
                WorkoutPlannerKernel pNew = pRef.newInstance();

                // Evaluation
                assertEquals(pExpected, pNew);
                assertTrue(pNew.size() == 0);
        }

        /**
         * Test clear on a non-empty planner.
         */
        @Test
        public final void testClearNonEmpty() {
                // Setup initial state (non-empty)
                WorkoutPlannerKernel pTest = this.createPlanner(
                                new WorkoutPlannerKernel.MuscleGroup[] { CHEST,
                                                BACK },
                                new WorkoutPlannerKernel.SorenessLevel[] { LOW,
                                                MEDIUM });
                WorkoutPlannerKernel pExpected = this.constructorRef();

                // Call method
                pTest.clear();

                // Evaluation
                assertEquals(pExpected, pTest);
                assertTrue(pTest.size() == 0);
        }

}
