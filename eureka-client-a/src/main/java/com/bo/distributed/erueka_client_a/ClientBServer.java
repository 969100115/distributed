package com.bo.distributed.erueka_client_a;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "erueka-client-b")
public interface ClientBServer {

    @RequestMapping(value = "/hello")
    String hello();
}
