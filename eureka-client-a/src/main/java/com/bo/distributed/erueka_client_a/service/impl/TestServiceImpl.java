package com.bo.distributed.erueka_client_a.service.impl;

import com.bo.distributed.erueka_client_a.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Wenbo
 * @date 2021/4/7 9:01
 * @Email 969100115@qq.com
 * @phone 17621847037
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    RestTemplate restTemplate;

    
//    @HystrixCommand(fallbackMethod = "testDefault")
    @Override
    public String test() {
        return restTemplate.getForObject("http://erueka-client-b/hello", String.class);
    }

    @Override
    public String testDefault() {
        return null;
    }
}
