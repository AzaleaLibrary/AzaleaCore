package com.azalealibrary.azaleacore.foundation.bus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private final String name;
    private final Map<Class<?>, Set<Invocation>> methods = new ConcurrentHashMap<>();

    public EventBus(String name) {
        this.name = name;
    }

    public void post(Object object) {
        Class<?> clazz = object.getClass();

        if (methods.containsKey(clazz)) {
            for (Invocation method : methods.get(clazz)) {
                try {
                    method.invoke(object);
                } catch (Exception exception) {
                    String message = String.format("An error occurred while posting event '%s' in bus %s.", clazz, name);
                    throw new RuntimeException(message, exception);
                }
            }
        }
    }

    public void register(Class<?> clazz) {
        try {
            register(clazz.getConstructor().newInstance());
        } catch (Exception exception) {
            throw new IllegalArgumentException("Could not register class '" + clazz.getName() + "' to event bus.");
        }
    }

    public void register(Object object) {
        Class<?> clazz = object.getClass();

        while (clazz != null) {
            for (Method method : findSubscriptionMethods(clazz)) {
                Class<?> type = method.getParameterTypes()[0];
                Set<Invocation> invocations = methods.getOrDefault(type, new HashSet<>());
                invocations.add(new Invocation(method, object));
                methods.put(type, invocations);
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static List<Method> findSubscriptionMethods(Class<?> clazz) {
        List<Method> subscribeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventSubscriber.class))
                .toList();

        Optional<Method> invalidMethod = subscribeMethods.stream().filter(method -> method.getParameterCount() != 1).findAny();
        if (invalidMethod.isPresent()) {
            throw new IllegalArgumentException("Malformed event subscriber method '" + invalidMethod.get().getName() + "'.");
        }
        return subscribeMethods;
    }
}