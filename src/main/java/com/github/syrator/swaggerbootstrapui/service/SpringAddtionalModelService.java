package com.github.syrator.swaggerbootstrapui.service;

import com.fasterxml.classmate.TypeResolver;
import com.github.syrator.swaggerbootstrapui.io.ResourceUtil;
import com.github.syrator.swaggerbootstrapui.model.SpringAddtionalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SpringAddtionalModelService{

    @Autowired
    private TypeResolver typeResolver;


    /***
     * 扫描包,获取对象
     * @param basePackage 扫描包路径
     * @return SpringAddtionModel实例
     */
    public SpringAddtionalModel scan(String... basePackage){
        if (basePackage==null||basePackage.length==0){
            throw new IllegalArgumentException("basePackage can't be empty!!!");
        }
        SpringAddtionalModel springAddtionalModel=new SpringAddtionalModel();
        ResourceUtil resourceUtil=new ResourceUtil();
        resourceUtil.find(basePackage);
        Set<Class<?>> classSets=resourceUtil.getClasses();
        if (classSets==null||classSets.isEmpty()){
            throw new IllegalArgumentException("can't find any Models in basePackage");
        }
        int a=0;
        for (Class<?> clazz:classSets){
            if (a==0){
                springAddtionalModel.setFirst(typeResolver.resolve(clazz));
            }else{
                springAddtionalModel.add(typeResolver.resolve(clazz));
            }
            a++;
        }
        return springAddtionalModel;
    }
}
