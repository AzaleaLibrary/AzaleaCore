package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

public class StringProperty extends PrimitiveTypeProperty<String> {

    public StringProperty(String name, String defaultValue, boolean required) {
        super(name, defaultValue, required, input -> input, currentValue -> "<text>");
    }
}
