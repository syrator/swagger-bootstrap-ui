package com.github.syrator.swaggerbootstrapui.configuration;

import com.github.syrator.swaggerbootstrapui.filter.ProductionSecurityFilter;
import com.github.syrator.swaggerbootstrapui.filter.SecurityBasicAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SecurityConfiguration {

    Logger logger= LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean
    public ProductionSecurityFilter productionSecurityFilter(){
        boolean prod=false;
        if (environment!=null){
            String prodStr=environment.getProperty("swagger.production");
            if (logger.isDebugEnabled()){
                logger.debug("swagger.production:{}",prodStr);
            }
            prod=Boolean.valueOf(prodStr);
        }
        ProductionSecurityFilter p=new ProductionSecurityFilter(prod);
        return p;
    }

    @Bean
    public SecurityBasicAuthFilter securityBasicAuthFilter(){
        boolean enableSwaggerBasicAuth=false;
        String dftUserName="admin",dftPass="123321";
        if (environment!=null){
            String enableAuth=environment.getProperty("swagger.basic.enable");
            enableSwaggerBasicAuth=Boolean.valueOf(enableAuth);
            if (enableSwaggerBasicAuth){
                //如果开启basic验证,从配置文件中获取用户名和密码
                String pUser=environment.getProperty("swagger.basic.username");
                String pPass=environment.getProperty("swagger.basic.password");
                if (pUser!=null&&!"".equals(pUser)){
                    dftUserName=pUser;
                }
                if (pPass!=null&&!"".equals(pPass)){
                    dftPass=pPass;
                }
            }
        }
        SecurityBasicAuthFilter securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
        return securityBasicAuthFilter;
    }
}
