package com.zing.boot.fileupload.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Value("${spring.application.name:base-service}")
    private String serviceName;

    @Value("${file.save.path}")
    private String savePath;

    @Bean
    public Docket createRestApi() {// 创建API基本信息

        //可以添加多个header或参数
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder
                .parameterType("header") //参数类型支持header, cookie, body, query etc
                .name("Authorization") //参数名
                .description("与请求参数access_token相同，2选1 授权认证信息 - 格式为：Bearer + 空格 + ${token}，token的获取方式: 使用oauth2 password认证/oauth-center/oauth/token接口（Post）中获取.")
                .modelRef(new ModelRef("string"))//指定参数值的类型
                .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).globalOperationParameters(aParameters)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDate.class, java.util.Date.class)
                ;
    }

    private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示
        return new ApiInfoBuilder()
                .title("YCloud " + serviceName + " Restful Apis")// API 标题
                .description(serviceName + "服务 Restful Apis")// API描述
                .contact("xxx")// 联系人
                .version("1.0")// 版本号
                .build();
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:" + savePath + "/");
        super.addResourceHandlers(registry);
    }
}
