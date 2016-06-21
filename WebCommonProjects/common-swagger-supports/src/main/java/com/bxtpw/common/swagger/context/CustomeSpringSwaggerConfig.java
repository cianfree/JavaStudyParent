package com.bxtpw.common.swagger.context;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 夏集球
 * @version 0.1
 * @time 2016/2/29 21:13
 * @since 0.1
 */
@Configuration
@EnableSwagger
public class CustomeSpringSwaggerConfig {

    private SpringSwaggerConfig springSwaggerConfig;

    private final ApiInfo apiInfo;

    public CustomeSpringSwaggerConfig(SwaggerApiInfo swaggerApiInfo) {
        apiInfo = new ApiInfo(//
                swaggerApiInfo.getTitle(),
                swaggerApiInfo.getDescription(),//
                swaggerApiInfo.getTermsOfServiceUrl(), //
                swaggerApiInfo.getContact(), //
                swaggerApiInfo.getLicense(), //
                swaggerApiInfo.getLicenseUrl());
    }

    /**
     * Required to autowire SpringSwaggerConfig
     */
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    /**
     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
     * framework - allowing for multiple swagger groups i.e. same code base
     * multiple swagger resource listings.
     */
    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo).includePatterns(".*?");
    }
}
