package com.lagou.service.impl;

import com.lagou.service.UserService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;

@Component
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String s) {
        String result=String.format("providera clent ip is %s", RpcContext.getContext().getAttachment("ipHost"));
        return result;
    }
}
