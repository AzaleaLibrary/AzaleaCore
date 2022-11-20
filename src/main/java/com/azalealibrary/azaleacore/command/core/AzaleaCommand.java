package com.azalealibrary.azaleacore.command.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AzaleaCommand {
    String name();

    String description() default "";

    String[] aliases() default {};

    String permission() default "";

    String permissionMessage() default "";

    String usage() default "";
}
