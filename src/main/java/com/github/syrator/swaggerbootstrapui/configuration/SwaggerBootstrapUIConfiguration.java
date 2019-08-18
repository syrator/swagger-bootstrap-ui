package com.github.syrator.swaggerbootstrapui.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {
                "com.github.syrator.swaggerbootstrapui.plugin",
                "com.github.syrator.swaggerbootstrapui.web",
                "com.github.syrator.swaggerbootstrapui.service"
        }
)
public class SwaggerBootstrapUIConfiguration {






}
