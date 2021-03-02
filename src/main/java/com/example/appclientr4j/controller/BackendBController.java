package com.example.appclientr4j.controller;

import com.example.appclientr4j.service.BusinessService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping(value = "/backendB")
public class BackendBController
{
    private final BusinessService businessBService;

    public BackendBController(@Qualifier("businessBService")BusinessService businessBService){
        this.businessBService = businessBService;
    }

    @GetMapping("test")
    public String test()
    {
        try
        {
            return businessBService.test();
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
    public String failure(){
        return businessBService.failure();
    }

    @GetMapping("success")
    public String success(){
        return businessBService.success();
    }

    @GetMapping("successException")
    public String successException(){
        return businessBService.successException();
    }

    @GetMapping("ignore")
    public String ignore(){
        return businessBService.ignore();
    }

    @GetMapping("fallback")
    public String failureWithFallback(){
        return businessBService.failureWithFallback();
    }

    @GetMapping("failureThrowable")
    public String failureThrowableWithFallback(){
        return businessBService.failureThrowableWithFallback();
    }
}
