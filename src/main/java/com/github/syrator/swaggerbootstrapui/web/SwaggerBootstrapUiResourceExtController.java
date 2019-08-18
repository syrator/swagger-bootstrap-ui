package com.github.syrator.swaggerbootstrapui.web;

import com.github.syrator.swaggerbootstrapui.model.SwaggerResourceExt;
import com.github.syrator.swaggerbootstrapui.service.SwaggerResourcesExtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/swagger-resources-ext")
public class SwaggerBootstrapUiResourceExtController {
    private final SwaggerResourcesExtProvider swaggerResourcesExtProvider;

    @Autowired
    public SwaggerBootstrapUiResourceExtController(@Qualifier("swaggerResourcesExtProvider") SwaggerResourcesExtProvider swaggerResources) {
        this.swaggerResourcesExtProvider = swaggerResources;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<List<SwaggerResourceExt>> swaggerResources() {
        return new ResponseEntity<List<SwaggerResourceExt>>(swaggerResourcesExtProvider.get(), HttpStatus.OK);
    }


}
