package com.kuailework;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
        import org.springframework.context.annotation.*;

/**
 * Created by lufei on 16/6/22.
 */
@ImportResource("classpath:beans.xml")
@PropertySources(@PropertySource({ "classpath:application.properties" }))
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }


}
