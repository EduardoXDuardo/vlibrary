package com.eduardoxduardo.vlibrary.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

    @Override
    public void initialize(PasswordStrength constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 8) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password must be at least 8 characters long")
                    .addConstraintViolation();
            return false;
        }

        boolean hasUppercase = UPPERCASE_PATTERN.matcher(password).matches();
        boolean hasLowercase = LOWERCASE_PATTERN.matcher(password).matches();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).matches();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(password).matches();

        if (!hasUppercase || !hasLowercase || !hasDigit || !hasSpecialChar) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}