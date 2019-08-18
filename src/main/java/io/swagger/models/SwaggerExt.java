/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package io.swagger.models;

import org.springframework.beans.BeanUtils;

public class SwaggerExt extends Swagger{

    protected SwaggerBootstrapUi swaggerBootstrapUi;

    public SwaggerBootstrapUi getSwaggerBootstrapUi() {
        return swaggerBootstrapUi;
    }

    public void setSwaggerBootstrapUi(SwaggerBootstrapUi swaggerBootstrapUi) {
        this.swaggerBootstrapUi = swaggerBootstrapUi;
    }

    public SwaggerExt(Swagger swagger){
        BeanUtils.copyProperties(swagger,this);
    }
}
