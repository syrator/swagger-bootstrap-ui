package com.github.syrator.swaggerbootstrapui.model;

import springfox.documentation.swagger.web.SwaggerResource;

public class SwaggerResourceExt extends SwaggerResource {

    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
