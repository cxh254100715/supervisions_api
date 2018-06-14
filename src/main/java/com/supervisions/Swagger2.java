package com.supervisions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在线文档
 */
@Configuration
@EnableSwagger2
public class Swagger2
{

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.supervisions.modules.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful API")
                .description("基于Spring Boot、Swagger2构建RESTful风格的API，并自动生成文档")
                //.termsOfServiceUrl("https://baidu.com")
                //.contact(new Contact("cxh", "https://baidu.com", "123@qq.com"))
                .version("1.0")
                //.license("license")
                //.licenseUrl("https://fengwenyi.com/license")
                .build();
    }
}
