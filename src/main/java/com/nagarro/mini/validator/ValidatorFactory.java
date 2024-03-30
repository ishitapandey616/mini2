package com.nagarro.mini.validator;

public class ValidatorFactory {
    private static final ValidatorFactory instance = new ValidatorFactory();

    // Private constructor to prevent external instantiation
    private ValidatorFactory() {
        // Constructor logic (if any)
    }

    

    public Validator getValidator(String type) {
        if ("Numeric".equalsIgnoreCase(type)) {
            return new NumericValidator();
        } else if ("Alphabets".equalsIgnoreCase(type)) {
            return new EnglishAlphabetsValidator();
        }
        // Add more conditions if needed for other validator types
        
        throw new IllegalArgumentException("Invalid validator type provided");
        // Or handle the case where an unknown type is provided
    }

	public static ValidatorFactory getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}



}