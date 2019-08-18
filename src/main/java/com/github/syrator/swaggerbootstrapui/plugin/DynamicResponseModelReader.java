package com.github.syrator.swaggerbootstrapui.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.github.syrator.swaggerbootstrapui.util.CommonUtils;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.DynamicParameter;
import io.swagger.annotations.DynamicResponseParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.schema.Types.isVoid;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.httpStatusCode;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.message;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE+1050)
public class DynamicResponseModelReader  implements OperationBuilderPlugin {

    private final TypeNameExtractor typeNameExtractor;

    private final Map<String,String> cacheGenModelMaps=new HashMap<>();
    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public DynamicResponseModelReader(TypeNameExtractor typeNameExtractor) {
        this.typeNameExtractor = typeNameExtractor;
    }

    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperationSupport> optional= context.findAnnotation(ApiOperationSupport.class);
        if (optional.isPresent()){
            changeResponseModel(optional.get().responses(),context);
        }else{
            Optional<DynamicResponseParameters> parametersOptional=context.findAnnotation(DynamicResponseParameters.class);
            if (parametersOptional.isPresent()){
                changeResponseModel(parametersOptional.get(),context);
            }
        }
    }

    /***
     * 改变响应Model的状态码200指定类
     * @param dynamicResponseParameters
     * @param operationContext
     */
    private void changeResponseModel(DynamicResponseParameters dynamicResponseParameters, OperationContext operationContext){
        if (dynamicResponseParameters!=null){
            DynamicParameter[] parameters=dynamicResponseParameters.properties();
            int fieldCount=0;
            for (DynamicParameter dynamicParameter:parameters){
                if (dynamicParameter.name()!=null&&!"".equals(dynamicParameter.name())&&!"null".equals(dynamicParameter.name())){
                    fieldCount++;
                }
            }
            if (fieldCount>0){
                //name是否包含
                String name=dynamicResponseParameters.name();
                if (name==null||"".equals(name)){
                    //gen
                    name=genClassName(operationContext);
                }
                //判断是否存在
                if (cacheGenModelMaps.containsKey(name)){
                    //存在,以方法名称作为ClassName
                    name=genClassName(operationContext);
                }
                //追加groupController
                name=operationContext.getGroupName().replaceAll("[_-]","")+"."+name;
                String classPath= CommonUtils.basePackage+name;
                Class<?> loadClass=CommonUtils.load(classPath);
                /*if (loadClass==null){
                    String genericFieldName=dynamicResponseParameters.genericFieldName();
                    if (genericFieldName!=null&&!"".equals(genericFieldName)&&!"null".equals(genericFieldName)){
                        ResolvedType defaultType=operationContext.alternateFor(operationContext.getReturnType());
                        Class<?> defaultReturnClazz=defaultType.getErasedType();
                        loadClass=CommonUtils.createDynamicModelByOriginalGenericClass(defaultReturnClazz,name,dynamicResponseParameters);
                        if (loadClass==null){
                            loadClass= CommonUtils.createDynamicModelClass(name,parameters);
                        }
                    }else{
                        loadClass= CommonUtils.createDynamicModelClass(name,parameters);
                    }
                }*/
                if (loadClass!=null) {

                    ResolvedType returnType = operationContext.alternateFor(typeResolver.resolve(loadClass));
                    int httpStatusCode = httpStatusCode(operationContext);
                    String message = message(operationContext);
                    ModelReference modelRef = null;
                    if (!isVoid(returnType)) {
                        ModelContext modelContext = ModelContext.returnValue(
                                operationContext.getGroupName(),
                                returnType,
                                operationContext.getDocumentationType(),
                                operationContext.getAlternateTypeProvider(),
                                operationContext.getGenericsNamingStrategy(),
                                operationContext.getIgnorableParameterTypes());
                        modelRef = modelRefFactory(modelContext, typeNameExtractor).apply(returnType);
                    }
                    ResponseMessage built = new ResponseMessageBuilder()
                            .code(httpStatusCode)
                            .message(message)
                            .responseModel(modelRef)
                            .build();
                    operationContext.operationBuilder().responseMessages(newHashSet(built));
                }

            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public String genClassName(OperationContext context){
        //gen
        String name=context.getName();
        if (name!=null&&!"".equals(name)){
            name=name.replaceAll("[_-]","");
            if (name.length()==1){
                name=name.toUpperCase();
            }else{
                name=name.substring(0,1).toUpperCase()+name.substring(1);
            }
        }
        return name;
    }
}
