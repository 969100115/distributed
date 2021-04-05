package com.bo.distributed.erueka_client_b.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Client {

    @RequestMapping(value = "hello")
    public String hello() {
        return "hello world";
    }


}
