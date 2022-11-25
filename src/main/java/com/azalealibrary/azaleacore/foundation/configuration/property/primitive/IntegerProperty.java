package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;

public class IntegerProperty extends PrimitiveTypeProperty<Integer> {

    public IntegerProperty(String name, Integer defaultValue, boolean required) {
        this(name, defaultValue, required, AssignmentPolicy.anything());
    }

    public IntegerProperty(String name, Integer defaultValue, boolean required, AssignmentPolicy<Integer> policy) {
        super(name, defaultValue, required, policy, Integer::parseInt, currentValue -> "<number>");
    }
}
