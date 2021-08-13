package com.stpd.springboot.webflux.exception;

import lombok.Data;

@Data
public class CheckException extends RuntimeException{

    /**
     * Error on his name
     */
    private String fieldName;
    /**
     * Error value
     */
    private String fieldValue;

    public CheckException(String fieldName, String fieldValue) {
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
