package es.udc.fi.dc.fd.signup;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import es.udc.fi.dc.fd.account.Account;


/**
 * The Class SignupForm.
 */
public class SignupForm {

    /** The Constant NOT_BLANK_MESSAGE. */
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    /** The Constant EMAIL_MESSAGE. */
    private static final String EMAIL_MESSAGE = "{email.message}";

    /** The email. */
    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    @Email(message = SignupForm.EMAIL_MESSAGE)
    private String email;

    /** The nick name. */
    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String nickName;

    /** The password. */
    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String password;

    /**
     * Create the default Form.
     */
    public SignupForm() {

    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the nick name.
     *
     * @return the nick name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets the nick name.
     *
     * @param nickName
     *            the new nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Creates the account.
     *
     * @return the account
     */
    public Account createAccount() {
        return new Account(getEmail(), getNickName(), getPassword(),
                "ROLE_USER");
    }
}
