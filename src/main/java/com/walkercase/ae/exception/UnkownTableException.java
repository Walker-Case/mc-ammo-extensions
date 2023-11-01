package com.walkercase.ae.exception;

public class UnkownTableException extends Exception{
    public UnkownTableException(String table){
        super("Failed to locate drop table \"" + table + "\"");
    }
}
