package com.example.appclientr4j.connector;

import com.example.appclientr4j.exception.BusinessException;
import com.example.appclientr4j.exception.FailureException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.control.Try;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RateLimiter(name = "backendB")
@Retry(name = "backendB")
@Component(value = "backendBConnector")
public class BackendBConnector implements Connector 
{
    @Override
    @Bulkhead(name = "backendB")
    public String failure() {
        System.out.println("[backendB] - ignoreException - HttpServerErrorException");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "[backendB] - This is a remote exception");
    }

    @Override
    public String ignoreException() {
        System.out.println("[backendB] - ignoreException - BusinessException");
        throw new BusinessException("This exception is ignored by the CircuitBreaker of backend A");
    }

    @Override
    @Bulkhead(name = "backendB")
    public String success() {
        System.out.println("[backendB] - success");
        return "Hello World from backend A";
    }

    @Override
    public String successException() {
        System.out.println("[backendB] - Execute successException - HttpClientErrorException");
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "[backendB] - This is a remote client exception");
    }

    @Override
    public String failureWithFallback() {
        return Try.ofSupplier(this::failure).recover(ex -> "Recovered - failureWithFallback").get();
    }

    @Override
    public String failureThrowableWithFallback() {
        return Try.ofSupplier(this::failure).recover(ex -> "Recovered - failureThrowableWithFallback").get();
    }

    @Override
    public String test(String param)
    {
        System.out.println("[BackendBConnector] - test - HttpServerErrorException: "+param);
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "[BackendBConnector] - This is a remote exception - "+param);
    }


}
