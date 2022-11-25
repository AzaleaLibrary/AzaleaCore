package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;

public class BooleanProperty extends PrimitiveTypeProperty<Boolean> {

    public BooleanProperty(String name, Boolean defaultValue, boolean required) {
        this(name, defaultValue, required, AssignmentPolicy.anything());
    }

    public BooleanProperty(String name, Boolean defaultValue, boolean required, AssignmentPolicy<Boolean> policy) {
        super(name, defaultValue, required, policy, Boolean::parseBoolean, v -> v != null ? String.valueOf(!v) : "<value>");
    }
}
