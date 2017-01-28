package es.udc.fi.dc.fd.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.Files;


/**
 * The Class DictionaryLoader.
 */
public class DictionaryLoader {

    /** The Constant LOGGER. */
    private final static Logger LOGGER = Logger
            .getLogger(DictionaryLoader.class.getName());

    /**
     * Gets the dictionary.
     *
     * @return the dictionary
     */
    public static Set<String> getDictionary() {
        Set<String> badWords = null;
        try {
            // Get file from resources folder
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            File file = new File(classLoader.getResource("badWords.txt")
                    .getFile());
            badWords = new HashSet<String>(Files.readLines(file,
                    Charset.forName("UTF-8")));
            return badWords;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return badWords;

    }
}
