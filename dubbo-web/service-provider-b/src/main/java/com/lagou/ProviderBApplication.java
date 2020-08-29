package com.lagou;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ProviderBApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:service-provider.xml");
        applicationContext.start();
        System.in.read();
    }
}
