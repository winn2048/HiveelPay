package com.hiveelpay.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayBootAppliaction.class);
    }

    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {

        return super.createRootApplicationContext(servletContext);
    }

}
