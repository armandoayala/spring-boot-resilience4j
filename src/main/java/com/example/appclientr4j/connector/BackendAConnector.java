package com.example.appclientr4j.connector;

import com.example.appclientr4j.exception.BusinessException;
import com.example.appclientr4j.exception.FailureException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@CircuitBreaker(name = "backendA")
@RateLimiter(name = "backendA",fallbackMethod = "fallbackRateLimiter")
@Component(value = "backendAConnector")
public class BackendAConnector implements Connector
{
    private final Logger logger= LoggerFactory.getLogger(BackendAConnector.class);

    @Override
    public String test(String param)
    {
        try
        {
            System.out.println("[BackendBConnector] - Antes Thread.sleep");
            Thread.sleep(3000);
            System.out.println("[BackendBConnector] - Despues Thread.sleep");
            return "[BackendBConnector] - TEST: "+param;
        }
        catch (Exception ex)
        {
            return "[BackendBConnector] - TEST EXCEPTION: "+ex.getMessage();
        }


    }

    @Override
    @Bulkhead(name = "backendA")
    public String failure() {
        System.out.println("[backendA] - ignoreException - HttpServerErrorException");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    @Override
    public String ignoreException() {
        System.out.println("[backendA] - ignoreException - BusinessException");
        throw new BusinessException("This exception is ignored by the CircuitBreaker of backend A");
    }


    @Override
    @Bulkhead(name = "backendA")
    public String success() {
        //try {
            //logger.info("[BackendAConnector] - success - BEFORE execute");
            //Thread.sleep(5000);
            //logger.info("[BackendAConnector] - success - AFTER execute");
            return "Hello World from backend A";
        /*}
        catch (Exception ex)
        {
            return "(Exception) - Hello World from backend A";
        }*/


    }

    @Override
    public String successException() {
        System.out.println("[backendA] - Execute successException - HttpClientErrorException");
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "[backendA] - This is a remote client exception");
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public String failureWithFallback() {
        System.out.println("[backendA] - Execute failureWithFallback");
        return failure();
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public String failureThrowableWithFallback() {
        throw new FailureException("[backendA] - Exception from failureThrowableWithFallback");
    }

    private String fallback(HttpServerErrorException ex) {
        return "[backendA] - Recovered HttpServerErrorException: " + ex.getMessage();
    }

    private String fallback(Throwable ex) {
        return "[backendA] - Recovered Throwable: " + ex.getMessage();
    }

    private String fallbackRateLimiter(Throwable ex) {
        logger.info("[BackendAConnector] - Execute fallbackRateLimiter");
        return "[BackendAConnector] - fallbackRateLimiter: "+ex.getMessage();
    }

    private String fallbackBulkhead(Throwable ex) {
        logger.info("[BackendAConnector] - Execute fallbackBulkhead");
        return "[BackendAConnector] - fallbackBulkhead: "+ex.getMessage();
    }

}
