package com.gudratli.nsbtodoapi.annotation.validator.impls;

import com.gudratli.nsbtodoapi.annotation.validator.StartsWith;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartsWithValidator implements ConstraintValidator<StartsWith, String>
{
    private StartsWith startsWith;

    @Override
    public void initialize (StartsWith constraintAnnotation)
    {
        startsWith = constraintAnnotation;
    }

    @Override
    public boolean isValid (String s, ConstraintValidatorContext constraintValidatorContext)
    {
        if (s == null)
            return false;

        return s.startsWith(startsWith.value());
    }
}
