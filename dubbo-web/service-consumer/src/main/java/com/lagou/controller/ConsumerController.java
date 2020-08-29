package com.lagou.controller;

import com.lagou.service.HelloService;
import com.lagou.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Reference(check = false)
    private HelloService helloService;
    @Reference(check=false)
    private UserService userService;

    @ResponseBody
    @RequestMapping("/hello")
    public String helloWord(){
        return helloService.sayHello("aaaa");
    }

    @ResponseBody
    @RequestMapping("/user")
    public String user(){
        return userService.sayHello("user");
    }
}
