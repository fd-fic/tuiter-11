package es.udc.fi.dc.fd.support.web;


/**
 * A message to be displayed in web context. Depending on the type, different
 * style will be applied.
 */
public class Message {
    /**
     * Name of the flash attribute.
     */
    public static final String MESSAGE_ATTRIBUTE = "message";

    /**
     * The type of the message to be displayed. The type is used to show message
     * in a different style.
     */
    public static enum Type {

        /** The danger. */
        DANGER,
        /** The warning. */
        WARNING,
        /** The info. */
        INFO,
        /** The success. */
        SUCCESS;
    }

    /** The message. */
    private final String message;

    /** The type. */
    private final Type type;

    /** The args. */
    private final Object[] args;

    /**
     * Instantiates a new message.
     *
     * @param message
     *            the message
     * @param type
     *            the type
     */
    public Message(String message, Type type) {
        this.message = message;
        this.type = type;
        this.args = null;
    }

    /**
     * Instantiates a new message.
     *
     * @param message
     *            the message
     * @param type
     *            the type
     * @param args
     *            the args
     */
    public Message(String message, Type type, Object... args) {
        this.message = message;
        this.type = type;
        this.args = args;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the args.
     *
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }
}
