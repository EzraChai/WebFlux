package com.stpd.springboot.webflux.advice;

import com.stpd.springboot.webflux.exception.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * Exception handling
 */
@ControllerAdvice
public class CheckAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBindException(WebExchangeBindException e){
        return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<String> handleNameException(CheckException e){
        return new ResponseEntity<String>(e.getFieldName() + ": Error value = " + e.getFieldValue(),HttpStatus.BAD_REQUEST);
    }

    /**
     * Transform exception to String
     * @param e
     * @return
     */
    private String toStr(WebExchangeBindException e) {
        return e.getFieldErrors().stream()
                .map(ex -> ex.getField() + ":" + ex.getDefaultMessage())
                .reduce("",(s1,s2) -> s1 + "\n" + s2);
    }
}
