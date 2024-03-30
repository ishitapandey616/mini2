package com.nagarro.mini.validator;

import org.springframework.stereotype.Component;


public class NumericValidator implements Validator {
    public void validate(String input) {
        if (!input.matches("\\d+")) {
            throw new IllegalArgumentException("Input should be numeric");
            // Or handle the validation error according to your application's logic
        }
    }
}
