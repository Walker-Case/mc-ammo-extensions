package com.walkercase.efm.exception;

public class UnkownTableException extends Exception{
    public UnkownTableException(String table){
        super("Failed to locate drop table \"" + table + "\"");
    }
}
