package es.udc.fi.dc.fd.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * The Class WebAppInitializer.
 */
public class WebAppInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Create the default config.
     */
    public WebAppInitializer() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
     * #getServletMappings()
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.support.
     * AbstractAnnotationConfigDispatcherServletInitializer
     * #getRootConfigClasses()
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { ApplicationConfig.class,
                DataSourceConfig.class, JpaConfig.class, SecurityConfig.class };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.support.
     * AbstractAnnotationConfigDispatcherServletInitializer
     * #getServletConfigClasses()
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebMvcConfig.class };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
     * #getServletFilters()
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy(
                "springSecurityFilterChain");

        return new Filter[] { characterEncodingFilter, securityFilterChain };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
     * #customizeRegistration(javax.servlet.ServletRegistration.Dynamic)
     */
    @Override
    protected void customizeRegistration(
            ServletRegistration.Dynamic registration) {
        registration.setInitParameter("defaultHtmlEscape", "true");
        registration.setInitParameter("spring.profiles.active", "default");
    }
}