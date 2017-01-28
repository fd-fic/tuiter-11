package es.udc.fi.dc.fd.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import es.udc.fi.dc.fd.Application;


/**
 * The Class ApplicationConfig.
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class,
        excludeFilters = @Filter({ Controller.class, Configuration.class }))
class ApplicationConfig {

    /**
     * Create the default Application Config.
     */
    public ApplicationConfig() {

    }

    /**
     * Property placeholder configurer.
     *
     * @return the property placeholder configurer
     */
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("/persistence.properties"));
        return ppc;
    }

}