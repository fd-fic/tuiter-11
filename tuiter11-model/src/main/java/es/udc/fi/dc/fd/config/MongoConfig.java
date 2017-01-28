package es.udc.fi.dc.fd.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.Mongo;


/**
 * The Class MongoConfig.
 */
@Configuration
class MongoConfig {

    /**
     * Create the default config.
     */
    public MongoConfig() {

    }

    /**
     * Mongo db factory.
     *
     * @return the mongo db factory
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new Mongo(), "tuiter11");
    }

    /**
     * Mongo template.
     *
     * @return the mongo template
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MongoTemplate template = new MongoTemplate(mongoDbFactory(),
                mongoConverter());
        return template;
    }

    /**
     * Mongo type mapper.
     *
     * @return the mongo type mapper
     */
    @Bean
    public MongoTypeMapper mongoTypeMapper() {
        return new DefaultMongoTypeMapper(null);
    }

    /**
     * Mongo mapping context.
     *
     * @return the mongo mapping context
     */
    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    /**
     * Mongo converter.
     *
     * @return the mapping mongo converter
     * @throws UnknownHostException
     *             the unknown host exception
     */
    @Bean
    public MappingMongoConverter mongoConverter() throws UnknownHostException {
        MappingMongoConverter converter = new MappingMongoConverter(
                mongoDbFactory(), mongoMappingContext());
        converter.setTypeMapper(mongoTypeMapper());
        return converter;
    }
}
