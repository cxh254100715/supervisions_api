package com.supervisions;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.supervisions.modules.dao")
@EnableScheduling
public class SupervisionsApiApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SupervisionsApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  api启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
