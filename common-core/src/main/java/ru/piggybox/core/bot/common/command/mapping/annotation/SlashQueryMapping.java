package ru.piggybox.core.bot.common.command.mapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlashQueryMapping {
    // value is required field
    String value();

    // description is required field
    String description();

    String extendedDescription() default "";

    int sequencePriority() default 0;

    boolean visibility() default true;
}
