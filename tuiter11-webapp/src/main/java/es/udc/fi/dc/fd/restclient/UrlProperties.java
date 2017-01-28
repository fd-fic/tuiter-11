package es.udc.fi.dc.fd.restclient;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.udc.fi.dc.fd.util.DictionaryLoader;

/**
 * The Class UrlProperties.
 */
public class UrlProperties {

    /** The Constant REST_SERVICE_URL. */
    public static final String REST_SERVICE_URL = "restService.url";

    /** The Constant LOGGER. */
    private final static Logger LOGGER = Logger
            .getLogger(DictionaryLoader.class.getName());

    /**
     * Instantiates a new url properties.
     */
    public UrlProperties() {

    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public Properties getProperties() {
        try {
            Properties propiedades = new Properties();

            propiedades.load(getClass().getResourceAsStream(
                    "/configuration.properties"));
            // si el archivo de propiedades NO esta vacio retornan las propiedes
            // leidas
            if (!propiedades.isEmpty()) {
                return propiedades;
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        return null;
    }
}
