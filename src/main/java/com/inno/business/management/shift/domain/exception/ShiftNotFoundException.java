package com.inno.business.management.shift.domain.exception;

public class ShiftNotFoundException extends RuntimeException {

    public ShiftNotFoundException(String id) {
        super("Shift introuvable : " + id);
    }
}
