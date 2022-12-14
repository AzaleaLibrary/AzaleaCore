package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.foundation.AzaleaException;

import javax.annotation.Nullable;
import java.util.List;

public interface ProtectedAssignment<T> {

    List<AssignmentPolicy<T>> getAssignmentPolicies();

    default T verify(@Nullable T value) throws AzaleaException {
        AssignmentPolicy<T> failedCheck = getAssignmentPolicies().stream()
                .filter(validator -> !validator.canAssign(value))
                .findAny().orElse(null);

        if (failedCheck != null) {
            throw new AzaleaException(failedCheck.getMessage(value));
        }
        return value;
    }
}
