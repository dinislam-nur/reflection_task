package ru.innopolis.student.dinislam.fields_cleaner.impl;

import ru.innopolis.student.dinislam.fields_cleaner.api.FieldsCleaner;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Реализация интерфейса FieldCleaner.
 */
public class FieldsCleanerImpl implements FieldsCleaner {

    /**
     * Метод очистки и вывода состояния объекта.
     * Также в случае, когда в качестве объекта передается Map<String, ?>, очищаются и выводятся
     * соответствующие записи и значения по ключам из входных Set-ов.
     *
     * @param object          - объект (либо Map) который будет обрабатываться по полям.
     * @param fieldsToCleanup - Set полей (либо ключей), состояния которых необходимо обнулить.
     * @param fieldsToOutput  - Set полей (либо ключей), состояние которых необходимо вывести в консоль.
     * @throws IllegalArgumentException - выбрасывается в случае, если в объекте не
     *                                  имеется поля соответсвуещему полю из Set-ов. Либо в случае, ключи Map
     *                                  не типа String.
     */
    @Override
    public void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalArgumentException {
        if (object instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) object;
            checkTypeOfKeys(map);
            processMap(map, fieldsToCleanup, fieldsToOutput);
        } else {
            processFields(object, fieldsToCleanup, fieldsToOutput);
        }
    }

    /**
     * Проверка типа ключей из входного Map<?, ?>.
     *
     * @param map - входной Map.
     * @throws IllegalArgumentException - выбрасывается, если тип ключа не соответствует String.
     */
    private void checkTypeOfKeys(Map<?, ?> map) throws IllegalArgumentException {
        final Set<?> keys = map.keySet();
        for (Object key : keys) {
            if (!(key instanceof String)) {
                throw new IllegalArgumentException("Тип ключей должен быть String!");
            }
        }
    }

    /**
     * Обработка записей Map, по входным Set-ам ключей.
     *
     * @param map                      - входная Map на обработку.
     * @param keysToRemove             - Set ключей, записи которых будут удалены из Map.
     * @param keysWhoseValuesWillBeOut - Set ключей, значения которыех будут выведены на экран.
     */
    private void processMap(Map<?, ?> map, Set<String> keysToRemove, Set<String> keysWhoseValuesWillBeOut) {
        for (Object key : keysWhoseValuesWillBeOut) {
            final Object value = map.get(key);
            if (value != null) {
                System.out.println(value);
            }
        }

        for (Object key : keysToRemove) {
            map.remove(key);
        }
    }

    /**
     * Обработка полей объекта object.
     *
     * @param object          - объект, поля которого будут обрабатываться.
     * @param fieldsToCleanup - Set полей, состояние которых обнулится.
     * @param fieldsToOutput  - Set полей, состояние которых выведется на экран.
     * @throws IllegalArgumentException - выбрасывается в случае, если в объекте не
     *                                  имеется поля соответсвуещему полю из Set-ов.
     */
    private void processFields(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput)
            throws IllegalArgumentException {
        List<Field> cleanup = new ArrayList<>(fieldsToCleanup.size());
        List<Field> output = new ArrayList<>(fieldsToOutput.size());

        try {
            final Class<?> aClass = object.getClass();
            for (String fieldNameToCleanup : fieldsToCleanup) {
                cleanup.add(aClass.getDeclaredField(fieldNameToCleanup));
            }
            for (String fieldNameToOutput : fieldsToOutput) {
                output.add(aClass.getDeclaredField(fieldNameToOutput));
            }

            for (Field fieldToOutput : output) {
                fieldToOutput.setAccessible(true);
                System.out.println(fieldToOutput.getName() + ": " + fieldToOutput.get(object));
            }

            for (Field fieldToCleanup : cleanup) {
                fieldToCleanup.setAccessible(true);

                final Class<?> type = fieldToCleanup.getType();
                if (type.isPrimitive()) {
                    processPrimitiveFields(object, fieldToCleanup, type);
                } else {
                    fieldToCleanup.set(object, null);
                }
            }
        } catch (NoSuchFieldException e) {
            System.out.println("Неверное поле в множестве обрабатываемых полей");
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            System.out.println("Ошибка в доступе к объекту через рефлексию");
            e.printStackTrace();

        }
    }

    /**
     * Обработка примитивного поля объекта.
     *
     * @param object - входной объект.
     * @param field  - примитивное поле.
     * @param type   - тип примитава.
     */
    private void processPrimitiveFields(Object object, Field field, Type type) {
        try {
            if (type == int.class) {
                field.setInt(object, 0);
            } else if (type == long.class) {
                field.setLong(object, 0);
            } else if (type == double.class) {
                field.setDouble(object, 0);
            } else if (type == float.class) {
                field.setFloat(object, 0);
            } else if (type == short.class) {
                field.setShort(object, (short) 0);
            } else if (type == byte.class) {
                field.setByte(object, (byte) 0);
            } else if (type == char.class) {
                field.setChar(object, (char) 0);
            } else if (type == boolean.class) {
                field.setBoolean(object, false);
            }
        } catch (IllegalAccessException e) {
            System.out.println("Ошибка доступа");
            e.printStackTrace();
        }
    }
}
