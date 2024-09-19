package com.l024;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
@Slf4j
@SpringBootApplication()
@MapperScan("com.l024.app.appfile.dao")
public class FinanceApplication {

    public static void main(String[] args) {
//        try {
//            System.out.println("sajkdhasjdhkas");
//            SpringApplication.run(FinanceApplication.class,args);
//            System.out.println("asihdasjd");
//        }catch (Exception e){
//            log.error(e.toString());
//        }

        ConfigurableApplicationContext ctx = SpringApplication.run(FinanceApplication.class, args);
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) ctx.getBean("tomcatServletWebServerFactory");
            int port = tomcatServletWebServerFactory.getPort();
            String contextPath = tomcatServletWebServerFactory.getContextPath();
            System.out.println("http://" + host + ":" + port + contextPath + "/");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}




