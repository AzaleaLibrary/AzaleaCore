package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

public interface PrimitiveTypeCompleter<T> {
    String suggest(T currentValue);
}
