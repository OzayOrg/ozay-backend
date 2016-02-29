package com.ozay.backend.config;

/**
 * Created by naofumiezaki on 10/30/15.
 */

import com.ozay.backend.security.RequestInterceptor;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by kn9b731 on 6/17/15
 */
@Configuration
public class ApplicationConfigurerAdapter extends WebMvcConfigurerAdapter implements EnvironmentAware  {

    private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    @Inject
    private DataSource datasource;


    @Bean
    public RequestInterceptor pagePopulationInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry)  {
        registry.addInterceptor(pagePopulationInterceptor())
            .addPathPatterns("/api/building")
            .addPathPatterns("/api/page/**")
            .addPathPatterns("/api/notification/**")
            .addPathPatterns("/api/member/**")
            .addPathPatterns("/api/role/**")
            .addPathPatterns("/api/organization-user/**")
            .excludePathPatterns("/api/page/management/**")
            .excludePathPatterns("/api/page/search")
            .excludePathPatterns("/api/organization-user/register")
            .excludePathPatterns("/api/page/organization-detail/*")
        ;

    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }


}
