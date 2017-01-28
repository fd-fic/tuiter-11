package es.udc.fi.dc.fd.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import es.udc.fi.dc.fd.Application;


/**
 * The Class JpaConfig.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Application.class)
class JpaConfig implements TransactionManagementConfigurer {

    // Estes Valores son luego inyectados desde el fichero de configuracion
    // persistence.properties por el bean PropertyPlaceholderConfigurer

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

    /** The dialect. */
    @Value("${hibernate.dialect}")
    private String dialect;

    /** The hbm2ddl auto. */
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    /**
     * Create the default config.
     */
    public JpaConfig() {

    }

    /**
     * Configure data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource configureDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Configure entity manager factory.
     *
     * @return the local container entity manager factory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean configureEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(configureDataSource());
        entityManagerFactoryBean.setPackagesToScan("es.udc.fi.dc.fd");
        entityManagerFactoryBean
                .setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
        jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO,
                hbm2ddlAuto);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.transaction.annotation.TransactionManagementConfigurer
     * #annotationDrivenTransactionManager()
     */
    @Override
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
}
