package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

public class IntegerProperty extends PrimitiveTypeProperty<Integer> {

    public IntegerProperty(String name, Integer defaultValue, boolean required) {
        super(name, defaultValue, required, Integer::parseInt, currentValue -> "<number>");
    }
}
