package com.nagarro.mini.validator;

import org.springframework.stereotype.Component;

public class EnglishAlphabetsValidator implements Validator {
    @Override
    public void validate(String input) {
        if (!input.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Input should contain English alphabetic characters");
            // Or handle the validation error according to your application's logic
        }
    }
    }