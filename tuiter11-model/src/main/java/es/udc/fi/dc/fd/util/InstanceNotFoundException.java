package es.udc.fi.dc.fd.util;


/**
 * The Class InstanceNotFoundException.
 */
public class InstanceNotFoundException extends RuntimeException {

    /** The not found class. */
    private Class<?> notFoundClass;

    /* Serial version */
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -882579438394773049L;

    /**
     * Default constructor.
     */
    public InstanceNotFoundException() {
        super();
    }

    /**
     * Instantiates a new instance not found exception.
     *
     * @param cause the cause
     */
    public InstanceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that allows a specific error message to be specified.
     *
     * @param message            the detail message.
     * @param notFoundClass            the not found class
     * @param cause the cause
     */
    public InstanceNotFoundException(String message, Class<?> notFoundClass,
            Throwable cause) {
        super(message, cause);
        this.notFoundClass = notFoundClass;
    }

    /**
     * Instantiates a new instance not found exception.
     *
     * @param message the message
     * @param notFoundClass the not found class
     */
    public InstanceNotFoundException(String message, Class<?> notFoundClass) {
        super(message);
        this.notFoundClass = notFoundClass;
    }

    /**
     * Gets the not found class.
     *
     * @return the not found class
     */
    public Class<?> getNotFoundClass() {
        return notFoundClass;
    }
}
