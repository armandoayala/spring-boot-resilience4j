package com.example.appclientr4j.connector;

public interface Connector
{
    String failure();

    String success();

    String successException();

    String ignoreException();

    String failureWithFallback();

    String failureThrowableWithFallback();

    String test(String param);
}
