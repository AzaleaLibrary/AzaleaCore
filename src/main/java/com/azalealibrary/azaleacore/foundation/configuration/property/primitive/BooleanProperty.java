package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

public class BooleanProperty extends PrimitiveTypeProperty<Boolean> {

    public BooleanProperty(String name, Boolean defaultValue, boolean required) {
        super(name, defaultValue, required, Boolean::parseBoolean, v -> v != null ? String.valueOf(!v) : "<value>");
    }
}
