package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;

public class StringProperty extends PrimitiveTypeProperty<String> {

    @SafeVarargs
    public StringProperty(String name, String defaultValue, boolean required, AssignmentPolicy<String>... policies) {
        super(name, defaultValue, required, input -> input, currentValue -> "<text>", policies);
    }
}
