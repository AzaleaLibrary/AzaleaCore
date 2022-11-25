package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;

public class IntegerProperty extends PrimitiveTypeProperty<Integer> {

    @SafeVarargs
    public IntegerProperty(String name, Integer defaultValue, boolean required, AssignmentPolicy<Integer>... policies) {
        super(name, defaultValue, required, Integer::parseInt, currentValue -> "<number>", policies);
    }
}
