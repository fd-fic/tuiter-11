package es.udc.fi.dc.fd.config;

import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;


/**
 * The Interface DataSourceConfig.
 */
@Configuration
public interface DataSourceConfig {

    /**
     * Data source.
     *
     * @return the data source
     */
    DataSource dataSource();
}