package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;

public class StringProperty extends PrimitiveTypeProperty<String> {

    public StringProperty(String name, String defaultValue, boolean required) {
        this(name, defaultValue, required, AssignmentPolicy.anything());
    }

    public StringProperty(String name, String defaultValue, boolean required, AssignmentPolicy<String> policy) {
        super(name, defaultValue, required, policy, input -> input, currentValue -> "<text>");
    }
}
