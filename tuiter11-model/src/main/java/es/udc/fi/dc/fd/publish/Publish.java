package es.udc.fi.dc.fd.publish;

import java.util.Calendar;


/**
 * Permite comparar dos publicaciones entre si segun su fecha de publicación.
 *
 * @author iago
 */
public interface Publish {

    /**
     * Gets the published date.
     *
     * @return the published date
     */
    Calendar getPublishedDate();
}
