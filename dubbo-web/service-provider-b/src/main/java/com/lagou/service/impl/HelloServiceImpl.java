package com.lagou.service.impl;

import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        String result=String.format("provider-b client ip %s", RpcContext.getContext().getAttachment("ipHost"));
        return result;
    }
}
