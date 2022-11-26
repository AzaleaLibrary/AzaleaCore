package com.azalealibrary.azaleacore.foundation.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class Invocation {

    private final Method handler;
    private final Object targetObject;

    public Invocation(Method handler, Object targetObject) {
        this.handler = handler;
        this.targetObject = targetObject;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Invocation that = (Invocation) object;
        return Objects.equals(handler, that.handler) && Objects.equals(targetObject, that.targetObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handler, targetObject);
    }

    public void invoke(Object object) throws InvocationTargetException, IllegalAccessException {
        handler.invoke(targetObject, object);
    }
}