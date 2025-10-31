package com.tui.transport.utils;

import java.lang.reflect.Field;

public class Converter<R, I> {
    private final Class<R> targetClass;

    public Converter(Class<R> targetClass) {
        this.targetClass = targetClass;
    }

    public R convert(I input) {
        try {
            R returnValue = targetClass.getDeclaredConstructor().newInstance();
            Field[] fields = input.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Field targetField = targetClass.getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(returnValue, field.get(input));
                }
                catch (NoSuchFieldException ignored) {} //pass if field is missing
            }
            return returnValue;
        } catch (Exception e) {
            throw new RuntimeException("Can't convert input: " +  input.getClass() + " to " + targetClass.getName(), e);
        }
    }
}
