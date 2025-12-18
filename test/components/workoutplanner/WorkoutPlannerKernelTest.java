package components.workoutplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map.Pair;

public abstract class WorkoutPlannerKernelTest {

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

        protected abstract WorkoutPlannerKernel constructorRef();

        protected abstract WorkoutPlannerKernel constructorTest();

        /**
         * Helper to create a Reference implementation populated with data.
         */
        private WorkoutPlannerKernel createRef(MuscleGroup[] groups,
                        SorenessLevel[] levels) {
                WorkoutPlannerKernel planner = this.constructorRef();
                for (int i = 0; i < groups.length; i++) {
                        planner.add(groups[i], levels[i]);
                }
                return planner;
        }

        /**
         * Helper to create a Test implementation populated with data.
         */
        private WorkoutPlannerKernel createTest(MuscleGroup[] groups,
                        SorenessLevel[] levels) {
                WorkoutPlannerKernel planner = this.constructorTest();
                for (int i = 0; i < groups.length; i++) {
                        planner.add(groups[i], levels[i]);
                }
                return planner;
        }

        @Test
        public final void testConstructor() {
                WorkoutPlannerKernel pExpected = this.constructorRef();
                WorkoutPlannerKernel pTest = this.constructorTest();
                assertEquals(pExpected, pTest);
        }

        @Test
        public final void testAddEmpty() {
                WorkoutPlannerKernel pRef = this.constructorRef();
                WorkoutPlannerKernel pTest = this.constructorTest();

                WorkoutPlannerKernel pExpected = this.constructorRef();
                pExpected.add(LEGS, HIGH);

                pTest.add(LEGS, HIGH);

                assertEquals(pExpected, pTest);
                assertEquals(pRef.size() + 1, pTest.size());
        }

        @Test
        public final void testAddNonEmpty() {
                WorkoutPlannerKernel pRef = this.createRef(
                                new MuscleGroup[] { CHEST },
                                new SorenessLevel[] { LOW });
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { CHEST },
                                new SorenessLevel[] { LOW });

                WorkoutPlannerKernel pExpected = this.createRef(
                                new MuscleGroup[] { CHEST, BACK },
                                new SorenessLevel[] { LOW, MEDIUM });

                pTest.add(BACK, MEDIUM);

                assertEquals(pExpected, pTest);
                assertEquals(pRef.size() + 1, pTest.size());
        }

        @Test
        public final void testRemoveSingleEntry() {
                WorkoutPlannerKernel pRef = this.createRef(
                                new MuscleGroup[] { CORE },
                                new SorenessLevel[] { NONE });
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { CORE },
                                new SorenessLevel[] { NONE });

                WorkoutPlannerKernel pExpected = this.constructorRef();

                SorenessLevel removed = pTest.remove(CORE);

                assertEquals(pExpected, pTest);
                assertEquals(NONE, removed);
                assertEquals(pRef.size() - 1, pTest.size());
        }

        @Test
        public final void testRemoveMultiEntry() {
                MuscleGroup[] groups = { ARMS, SHOULDERS };
                SorenessLevel[] levels = { HIGH, MEDIUM };
                WorkoutPlannerKernel pRef = this.createRef(groups, levels);
                WorkoutPlannerKernel pTest = this.createTest(groups, levels);

                WorkoutPlannerKernel pExpected = this.createRef(
                                new MuscleGroup[] { ARMS },
                                new SorenessLevel[] { HIGH });

                SorenessLevel removed = pTest.remove(SHOULDERS);

                assertEquals(pExpected, pTest);
                assertEquals(MEDIUM, removed);
                assertEquals(pRef.size() - 1, pTest.size());
        }

        @Test
        public final void testRemoveAnySingleEntry() {
                WorkoutPlannerKernel pRef = this.createRef(
                                new MuscleGroup[] { ARMS },
                                new SorenessLevel[] { HIGH });
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { ARMS },
                                new SorenessLevel[] { HIGH });

                WorkoutPlannerKernel pExpected = this.constructorRef();

                Pair<MuscleGroup, SorenessLevel> removed = pTest.removeAny();

                assertEquals(pExpected, pTest);
                assertEquals(ARMS, removed.key());
                assertEquals(HIGH, removed.value());
                assertEquals(pRef.size() - 1, pTest.size());
        }

        @Test
        public final void testRemoveAnyMultiEntry() {
                MuscleGroup[] groups = { CHEST, BACK };
                SorenessLevel[] levels = { LOW, MEDIUM };
                WorkoutPlannerKernel pRef = this.createRef(groups, levels);
                WorkoutPlannerKernel pTest = this.createTest(groups, levels);

                Pair<MuscleGroup, SorenessLevel> removed = pTest.removeAny();

                boolean wasRemoved = (removed.key() == CHEST
                                && removed.value() == LOW)
                                || (removed.key() == BACK
                                                && removed.value() == MEDIUM);
                assertTrue(wasRemoved);
                assertEquals(pRef.size() - 1, pTest.size());
                assertTrue(pTest.size() == 1);
        }

        @Test
        public final void testValue() {
                MuscleGroup[] groups = { LEGS, ARMS };
                SorenessLevel[] levels = { HIGH, NONE };
                WorkoutPlannerKernel pRef = this.createRef(groups, levels);
                WorkoutPlannerKernel pTest = this.createTest(groups, levels);

                SorenessLevel soreness = pTest.value(ARMS);

                assertEquals(pRef, pTest);
                assertEquals(NONE, soreness);
        }

        @Test
        public final void testHasKeyTrue() {
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { LEGS },
                                new SorenessLevel[] { HIGH });
                assertTrue(pTest.hasKey(LEGS));
        }

        @Test
        public final void testHasKeyFalse() {
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { LEGS },
                                new SorenessLevel[] { HIGH });
                assertFalse(pTest.hasKey(CHEST));
        }

        @Test
        public final void testSizeEmpty() {
                WorkoutPlannerKernel pTest = this.constructorTest();
                assertEquals(0, pTest.size());
        }

        @Test
        public final void testSizeMultiEntry() {
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { CHEST, BACK, LEGS, CORE },
                                new SorenessLevel[] { LOW, MEDIUM, HIGH,
                                                NONE });
                assertEquals(4, pTest.size());
        }

        @Test
        public final void testNewInstance() {
                WorkoutPlannerKernel pRef = this.createRef(
                                new MuscleGroup[] { CHEST },
                                new SorenessLevel[] { LOW });
                WorkoutPlannerKernel pExpected = this.constructorRef();
                WorkoutPlannerKernel pNew = pRef.newInstance();
                assertEquals(pExpected, pNew);
                assertTrue(pNew.size() == 0);
        }

        @Test
        public final void testClearNonEmpty() {
                WorkoutPlannerKernel pTest = this.createTest(
                                new MuscleGroup[] { CHEST, BACK },
                                new SorenessLevel[] { LOW, MEDIUM });
                WorkoutPlannerKernel pExpected = this.constructorRef();
                pTest.clear();
                assertEquals(pExpected, pTest);
                assertTrue(pTest.size() == 0);
        }
}
