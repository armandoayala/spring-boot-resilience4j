package com.example.appclientr4j.controller;

import com.example.appclientr4j.service.BusinessService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping(value = "/backendA")
public class BackendAController
{
    private final BusinessService businessAService;
    private final Logger logger=LoggerFactory.getLogger(BackendAController.class);

    public BackendAController(@Qualifier("businessAService") BusinessService businessAService){
        this.businessAService = businessAService;
    }

    @GetMapping("test")
    public String test()
    {
        try
        {
            return businessAService.test();
        }
        catch (HttpServerErrorException ex)
        {
            return "HttpServerErrorException: "+ex.getMessage();
        }
        catch (CallNotPermittedException ex)
        {
            return "CallNotPermittedException: "+ex.getMessage();
        }
        catch (Exception ex)
        {
            return "Exception: "+ex.getMessage();
        }
    }

    @GetMapping("failure")
    public String failure()
    {
        try
        {
            return businessAService.failure();
        }
        catch (HttpServerErrorException ex)
        {
            return "HttpServerErrorException: "+ex.getMessage();
        }
        catch (CallNotPermittedException ex)
        {
            return "CallNotPermittedException: "+ex.getMessage();
        }
        catch (Exception ex)
        {
            return "Exception: "+ex.getMessage();
        }
    }

    @GetMapping("success")
    public ResponseEntity<String> success() throws Exception {
        try
        {
            logger.info("[BackendAController] - success - START execute");
            String result=businessAService.success();
            logger.info("[BackendAController] - success - END execute (RESULT): "+result);

            if(result.contains("fallback"))
            {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .cacheControl(CacheControl.noCache())
                        .body(result);
            }

            return ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.noCache())
                    .body(result);
        }
        catch (Exception ex)
        {
            logger.info("[BackendAController.Succsess] - Exception: "+ex.getMessage());
            //return "Exception: "+ex.getMessage();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .cacheControl(CacheControl.noCache())
                    .body(ex.getMessage());
        }

    }

    @GetMapping("successException")
    public String successException()
    {
        try
        {
            return businessAService.successException();
        }
        catch (HttpClientErrorException ex)
        {
            return "HttpClientErrorException: "+ex.getMessage();
        }
        catch (Exception ex)
        {
            return "Exception: "+ex.getMessage();
        }

    }

    @GetMapping("ignore")
    public String ignore(){
        return businessAService.ignore();
    }

    @GetMapping("fallback")
    public String failureWithFallback(){
        return businessAService.failureWithFallback();
    }

    @GetMapping("failureThrowable")
    public String failureThrowableWithFallback(){
        return businessAService.failureThrowableWithFallback();
    }
}
