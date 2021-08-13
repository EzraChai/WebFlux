package com.stpd.springboot.webflux.util;

import com.stpd.springboot.webflux.exception.CheckException;

import java.util.stream.Stream;

public class CheckUtil {

    private static final String[] INVALID_NAMES = {"admin", "admins","adminstrator","adminstrators"};

    
    /**
     * Check the name
     * @param value
     */
    public static void checkName(String value) {
        Stream.of(INVALID_NAMES).filter(name -> name.equalsIgnoreCase(value)).findAny().ifPresent(name -> {
            throw new CheckException("name", value);
        });
    }

}
