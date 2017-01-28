package es.udc.fi.dc.fd.config;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.RequestMatcher;

import es.udc.fi.dc.fd.account.UserService;


/**
 * The Class SecurityConfig.
 */
@Configuration
@ImportResource(value = "classpath:spring-security-context.xml")
class SecurityConfig {

    /**
     * Create the default config.
     */
    public SecurityConfig() {

    }

    /**
     * User service.
     *
     * @return the user service
     */
    @Bean
    public UserService userService() {
        return new UserService();
    }

    /**
     * Remember me services.
     *
     * @return the token based remember me services
     */
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me-key",
                userService());
    }

    /**
     * Password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    /**
     * Test csrf matcher.
     *
     * @return the request matcher
     */
    @Profile("test")
    @Bean(name = "csrfMatcher")
    public RequestMatcher testCsrfMatcher() {
        return new RequestMatcher() {

            @Override
            public boolean matches(HttpServletRequest request) {
                return false;
            }
        };
    }

    /**
     * Csrf matcher.
     *
     * @return the request matcher
     */
    @Profile("!test")
    @Bean(name = "csrfMatcher")
    public RequestMatcher csrfMatcher() {
        /**
         * Copy of default request matcher from
         * CsrfFilter$DefaultRequiresCsrfMatcher
         */
        return new RequestMatcher() {
            private Pattern allowedMethods = Pattern
                    .compile("^(GET|HEAD|TRACE|OPTIONS)$");

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.springframework.security.web.util.matcher.RequestMatcher#
             * matches(javax.servlet.http.HttpServletRequest)
             */
            @Override
            public boolean matches(HttpServletRequest request) {
                return !allowedMethods.matcher(request.getMethod()).matches();
            }
        };
    }
}