package com.github.syrator.swaggerbootstrapui.model;

import springfox.documentation.service.VendorExtension;

public class OrderExtensions implements VendorExtension<Integer> {

    private Integer order;

    public OrderExtensions(Integer order) {
        this.order = order;
    }

    @Override
    public String getName() {
        return "x-order";
    }
    @Override
    public Integer getValue() {
        return this.order;
    }
}
