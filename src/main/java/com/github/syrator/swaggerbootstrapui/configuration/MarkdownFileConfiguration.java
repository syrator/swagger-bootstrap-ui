package com.github.syrator.swaggerbootstrapui.configuration;

import io.swagger.models.MarkdownFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MarkdownFileConfiguration {

    @Autowired
    private Environment environment;

    @Bean(initMethod = "init")
    public MarkdownFiles markdownFiles(){
        MarkdownFiles markdownFiles=new MarkdownFiles(environment!=null?environment.getProperty("swagger.markdowns"):"");
        return markdownFiles;
    }



}
