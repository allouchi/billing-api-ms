package com.sbatec.company.exceptions;

public class FileNotFoundException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public FileNotFoundException(String message) {
        super(String.format("No clients finded for id  %s", message));
    }

}
