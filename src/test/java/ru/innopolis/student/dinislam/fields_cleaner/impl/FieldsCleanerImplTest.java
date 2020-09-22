package ru.innopolis.student.dinislam.fields_cleaner.impl;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import ru.innopolis.student.dinislam.fields_cleaner.api.FieldsCleaner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FieldsCleanerImplTest {

    private static final double DELTA = 0.000001;

    @Getter
    private static class ClassWithPrimitiveFields {
        private final int one;
        private final long two;
        private final double three;
        private final float four;
        private final short five;
        private final byte six;
        private final char seven;
        private final boolean eight;

        public ClassWithPrimitiveFields() {
            one = 1;
            two = 2L;
            three = 3.1;
            four = 4.1f;
            five = 5;
            six = 6;
            seven = '7';
            eight = true;
        }

        @Override
        public String toString() {
            return "ClassWithPrimitiveFields{" +
                    "one=" + one +
                    ", two=" + two +
                    ", three=" + three +
                    ", four=" + four +
                    ", five=" + five +
                    ", six=" + six +
                    ", seven=" + seven +
                    ", eight=" + eight +
                    '}';
        }

        boolean getEight() {
            return eight;
        }
    }

    @Test
    void cleanupPrimitiveFields() {
        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>(Arrays.asList(
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight"
        ));
        final Set<String> output = new HashSet<>();
        final ClassWithPrimitiveFields object = new ClassWithPrimitiveFields();
        fieldsCleaner.cleanup(object, cleanup, output);

        assertEquals(0, object.getOne());
        assertEquals(0, object.getTwo());
        assertEquals(0, object.getThree(), DELTA);
        assertEquals(0, object.getFour(), DELTA);
        assertEquals(0, object.getFive());
        assertEquals(0, object.getSix());
        assertEquals(0, object.getSeven());
        assertFalse(object.getEight());
    }

    @Test
    void outputPrimitiveFieldsTest() {
        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>();
        final Set<String> output = new HashSet<>(Arrays.asList(
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight"
        ));
        final ClassWithPrimitiveFields object = new ClassWithPrimitiveFields();
        System.out.println("Вывод примитивных полей:");
        fieldsCleaner.cleanup(object, cleanup, output);
    }

    @Getter
    private static class ClassWithObjectFields {
        private final String string;
        private final ClassWithPrimitiveFields someObject;
        private final List<Integer> list;

        ClassWithObjectFields() {
            string = "string";
            someObject = new ClassWithPrimitiveFields();
            list = Arrays.asList(1, 2, 3);
        }

        @Override
        public String toString() {
            return "ClassWithObjectFields{" +
                    "string='" + string + '\'' +
                    ", someObject=" + someObject +
                    ", list=" + list +
                    '}';
        }
    }

    @Test
    void cleanupObjectFields() {
        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>(Arrays.asList(
                "string",
                "someObject",
                "list"
        ));
        final Set<String> output = new HashSet<>();
        final ClassWithObjectFields object = new ClassWithObjectFields();
        fieldsCleaner.cleanup(object, cleanup, output);

        assertNull(object.getString());
        assertNull(object.getSomeObject());
        assertNull(object.getList());
    }

    @Test
    void outputObjectFields() {
        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>();
        final Set<String> output = new HashSet<>(Arrays.asList(
                "string",
                "someObject",
                "list"
        ));
        final ClassWithObjectFields object = new ClassWithObjectFields();
        System.out.println("Вывод ссылочных полей:");
        fieldsCleaner.cleanup(object, cleanup, output);
    }

    @Test
    void throwIllegalArgumentExceptionTest() {
        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>(Collections.singletonList(
                "someIllegalField"
        ));
        final Set<String> output = new HashSet<>(Collections.singletonList(
                "someIllegalField"
        ));
        final ClassWithObjectFields object = new ClassWithObjectFields();

        assertThrows(IllegalArgumentException.class, () -> fieldsCleaner.cleanup(object, cleanup, output));
    }

    @Test
    void deleteKeysFromMapTest() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("first", 1);
        map.put("second", 2);
        map.put("third", 3);

        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>(Arrays.asList(
                "first",
                "second"
        ));
        final Set<String> output = new HashSet<>();
        fieldsCleaner.cleanup(map, cleanup, output);

        assertEquals(1, map.size());
        assertTrue(map.containsKey("third"));
        assertFalse(map.containsKey("first"));
        assertFalse(map.containsKey("second"));
    }

    @Test
    void outputValuesFromMapTest() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("first", 1);
        map.put("second", 2);
        map.put("third", 3);

        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>();
        final Set<String> output = new HashSet<>(Arrays.asList(
                "first",
                "second",
                "third"
        ));
        System.out.println("Вывод значений Map по ключам:");
        fieldsCleaner.cleanup(map, cleanup, output);
    }

    @Test
    void throwIllegalArgumentExceptionFromMapTest() {
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);

        final FieldsCleaner fieldsCleaner = new FieldsCleanerImpl();
        final Set<String> cleanup = new HashSet<>();
        final Set<String> output = new HashSet<>();

        assertThrows(IllegalArgumentException.class, () -> fieldsCleaner.cleanup(map, cleanup, output));
    }
}

