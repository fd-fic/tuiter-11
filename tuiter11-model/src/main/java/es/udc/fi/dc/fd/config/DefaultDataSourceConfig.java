package es.udc.fi.dc.fd.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


/**
 * The Class DefaultDataSourceConfig.
 */
@Configuration
@Profile("default")
class DefaultDataSourceConfig implements DataSourceConfig {

    /** The driver. */
    @Value("${dataSource.driverClassName}")
    private String driver;

    /** The url. */
    @Value("${dataSource.url}")
    private String url;

    /** The username. */
    @Value("${dataSource.username}")
    private String username;

    /** The password. */
    @Value("${dataSource.password}")
    private String password;

    /**
     * Create the default DataSourceConfig.
     */
    public DefaultDataSourceConfig() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see es.udc.fi.dc.fd.config.DataSourceConfig#dataSource()
     */
    @Bean
    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
