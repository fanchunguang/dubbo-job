package com.lagou.service.impl;

import com.lagou.service.UserService;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ProviderApplication {

    public static void main(String[] args) throws IOException {
        /*AnnotationConfigApplicationContext config=new AnnotationConfigApplicationContext(BeanAProvider.class);
        config.start();
        System.in.read();*/
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:service-providera.xml");
        applicationContext.start();
        System.in.read();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.lagou.service.impl")
    static class BeanAProvider {

        @Bean
        public ApplicationConfig applicationConfig(){
            ApplicationConfig applicationConfig=new ApplicationConfig();
            applicationConfig.setName("service-provider-a");
            return applicationConfig;
        }
        @Bean
        public ServiceConfig<UserService> serviceConfig(){
            ServiceConfig<UserService> serviceConfig=new ServiceConfig<>();
            serviceConfig.setId("userService");
            serviceConfig.setInterface("com.lagou.service.UserService");
            return serviceConfig;
        }
        @Bean
        public RegistryConfig registryConfig(){
            RegistryConfig registryConfig=new RegistryConfig();
            registryConfig.setAddress("zookeeper://192.168.1.45:2181,192.168.1.45:2182,192.168.1.45:2183");
            return  registryConfig;
        }
        @Bean
        public ProtocolConfig protocolConfig(){
            ProtocolConfig protocolConfig=new ProtocolConfig();
            protocolConfig.setName("dubbo");
            protocolConfig.setPort(21880);
            return protocolConfig;
        }
    }
}
