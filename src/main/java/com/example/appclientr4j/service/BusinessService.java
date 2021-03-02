package com.example.appclientr4j.service;

public interface BusinessService
{
    String failure();

    String success();

    String successException();

    String ignore();

    String failureWithFallback();

    String failureThrowableWithFallback();

    String test();
}
