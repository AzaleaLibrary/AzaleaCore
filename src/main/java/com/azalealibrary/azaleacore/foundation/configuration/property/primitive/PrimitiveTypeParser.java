package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

public interface PrimitiveTypeParser<T> {
    T parseValue(String input);
}
