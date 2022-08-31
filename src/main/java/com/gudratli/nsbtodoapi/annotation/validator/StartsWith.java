package com.gudratli.nsbtodoapi.annotation.validator;

import com.gudratli.nsbtodoapi.annotation.validator.impls.StartsWithValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartsWithValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartsWith
{
    String message () default "";

    String value () default "";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}
