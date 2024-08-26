package com.mini.rpc.consumer.controller;

import com.lcx.rpc.provider.facade.HelloFacade;
import com.mini.rpc.consumer.annotation.RpcReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： lichenxu
 * @date： 2024/8/2615:50
 * @description：
 * @version： v1.0
 */

@RestController
public class HelloController {

    @RpcReference(serviceVersion = "1.0.0", timeout = 3000)
    private HelloFacade helloFacade;
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return helloFacade.hello("mini rpc");
    }
}
