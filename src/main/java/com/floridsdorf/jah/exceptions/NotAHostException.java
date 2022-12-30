package com.floridsdorf.jah.exceptions;

public class NotAHostException extends Exception{

    public NotAHostException(){
        this("This client is not a host!");
    }

    public NotAHostException(String errorMessage){
        super(errorMessage);
    }

}
