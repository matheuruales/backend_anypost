package com.anypost.util;

import com.anypost.dto.LoginRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginRequest request = (LoginRequest) target;
        
        if (request.getIdToken() == null || request.getIdToken().trim().isEmpty()) {
            errors.rejectValue("idToken", "empty", "ID token cannot be empty");
        }
    }
}