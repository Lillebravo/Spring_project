package com.jerry.school_project.util;

import org.springframework.stereotype.Component;

@Component
public class Validation {

    public void validateString(String parameterValue, String parameterName, boolean required,
                               boolean allowNumbers, boolean allowSpecialChars, String customPattern) {
        // Check if required
        if (required && (parameterValue == null || parameterValue.trim().isEmpty())) {
            throw new IllegalArgumentException(parameterName + " cannot be empty.");
        }

        // If not required and empty, skip further validation
        if (!required && (parameterValue == null || parameterValue.trim().isEmpty())) {
            return;
        }

        // Use custom pattern if provided
        if (customPattern != null && !customPattern.isEmpty()) {
            if (!parameterValue.matches(customPattern)) {
                throw new IllegalArgumentException(parameterName + " format is invalid.");
            }
            return;
        }

        // Build regex pattern based on options
        StringBuilder regex = new StringBuilder("^[A-Za-zÅÄÖåäö");

        if (allowNumbers) {
            regex.append("0-9");
        }

        if (allowSpecialChars) {
            regex.append("\\s\\-'.,!?");
        } else {
            regex.append(" ");
        }

        regex.append("]+$");

        if (!parameterValue.matches(regex.toString())) {
            StringBuilder errorMsg = new StringBuilder(parameterName + " can only contain letters");
            if (allowNumbers) errorMsg.append(", numbers");
            if (allowSpecialChars) errorMsg.append(", and common punctuation");
            else errorMsg.append(" and spaces");
            errorMsg.append(". Please use valid characters only.");

            throw new IllegalArgumentException(errorMsg.toString());
        }
    }

    // Validate string with default settings (required, letters and spaces only)
    public void validateString(String value, String fieldName) {
        validateString(value, fieldName, true, false, false, null);
    }

    // Validate string with numbers allowed
    public void validateStringWithNumbers(String value, String fieldName, boolean required) {
        validateString(value, fieldName, required, true, false, null);
    }

    // Validate string with special characters allowed
    public void validateStringWithSpecialChars(String value, String fieldName, boolean required) {
        validateString(value, fieldName, required, false, true, null);
    }


    public void validateInteger(Integer parameterValue, String parameterName, boolean required,
                                Integer minValue, Integer maxValue, String customErrorMessage) {
        // Check if required
        if (required && parameterValue == null) {
            throw new IllegalArgumentException(parameterName + " is required");
        }

        // If not required and null, skip further validation
        if (!required && parameterValue == null) {
            return;
        }

        // Check minimum value
        if (minValue != null && parameterValue < minValue) {
            if (customErrorMessage != null) {
                throw new IllegalArgumentException(customErrorMessage);
            }
            throw new IllegalArgumentException(parameterName + " must be at least " + minValue);
        }

        // Check maximum value
        if (maxValue != null && parameterValue > maxValue) {
            if (customErrorMessage != null) {
                throw new IllegalArgumentException(customErrorMessage);
            }
            throw new IllegalArgumentException(parameterName + " must be at most " + maxValue);
        }
    }

    // Validate int with default settings (required, no min/max)
    public void validateInteger(Integer value, String fieldName) {
        validateInteger(value, fieldName, true, 0, null, null);
    }

    // Validate long
    public void validateLong(Long parameterValue, String parameterName,
                             Long minValue, String customErrorMessage) {
        if (parameterValue == null) {
            throw new IllegalArgumentException(parameterName + " is required");
        }

        if (minValue != null && parameterValue < minValue) {
            if (customErrorMessage != null) {
                throw new IllegalArgumentException(customErrorMessage);
            }
            throw new IllegalArgumentException(parameterName + " must be at least " + minValue);
        }
    }

    // Specific validation for birth year
    public void validateBirthYear(Integer birthYear, boolean required) {
        validateInteger(birthYear, "Birth year", required, 1900, 2015,
                "Birth year must be between 1900 and 2015");
    }

    // Specific validation for publication year
    public void validatePublicationYear(Integer publicationYear) {
        validateInteger(publicationYear, "Publication year", true, 1000, 2025,
                "Publication year must be between 1000 and 2025");
    }

    // Validate that one int doesnt exceed another
    public void validateIntegerRelationship(Integer value1, Integer value2,
                                            String field1Name, String field2Name,
                                            String relationshipType) {
        if (value1 == null || value2 == null) {
            return; // Skip if either is null
        }

        switch (relationshipType.toLowerCase()) {
            case "not_exceed":
                if (value1 > value2) {
                    throw new IllegalArgumentException(field1Name + " cannot exceed " + field2Name);
                }
                break;
            case "less_than":
                if (value1 >= value2) {
                    throw new IllegalArgumentException(field1Name + " must be less than " + field2Name);
                }
                break;
            case "greater_than":
                if (value1 <= value2) {
                    throw new IllegalArgumentException(field1Name + " must be greater than " + field2Name);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown relationship type: " + relationshipType);
        }
    }
}