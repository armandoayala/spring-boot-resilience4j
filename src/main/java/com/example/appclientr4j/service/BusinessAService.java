package com.example.appclientr4j.service;

import com.example.appclientr4j.connector.Connector;
import com.example.appclientr4j.controller.BackendAController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "businessAService")
public class BusinessAService implements BusinessService
{
    private final Connector backendAConnector;


    public BusinessAService(@Qualifier("backendAConnector") Connector backendAConnector){
        this.backendAConnector = backendAConnector;
    }

    @Override
    public String test()
    {
        return backendAConnector.test("BusinessAService");
    }

    @Override
    public String failure() {
        return backendAConnector.failure();
    }

    @Override
    public String success() {

        return backendAConnector.success();
    }

    @Override
    public String successException() {
        return backendAConnector.successException();
    }

    @Override
    public String ignore() {
        return backendAConnector.ignoreException();
    }

    @Override
    public String failureWithFallback() {
        return backendAConnector.failureWithFallback();
    }

    @Override
    public String failureThrowableWithFallback() {
        return backendAConnector.failureThrowableWithFallback();
    }
}
