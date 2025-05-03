package com.springAWS.project.aws.spring.intrgration.exceptions;

public class FileException extends Exception{

    public FileException(){
        super();
    }

    public FileException(String message , Throwable cause){
        super(message , cause);
    }
    public FileException(String message){
        super(message);
    }

}
