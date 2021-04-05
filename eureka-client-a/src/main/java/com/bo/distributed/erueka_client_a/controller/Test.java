package com.bo.distributed.erueka_client_a.controller;

import com.bo.distributed.erueka_client_a.ClientBServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Test {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ClientBServer clientBServer;

    @RequestMapping(value = "getServiceB_Ribbon")
    public String getServiceB_Ribbon() {
        return restTemplate.getForObject("http://erueka-client-b/hello", String.class);
    }

    @RequestMapping(value = "getServiceB_feign")
    public String getServiceB_feign() {
        return clientBServer.hello();
    }
}
