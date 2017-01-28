package es.udc.fi.dc.fd.home;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import es.udc.fi.dc.fd.account.Account;


/**
 * The Class ProfileForm.
 */
public class ProfileForm {

    /** The Constant NOT_BLANK_MESSAGE. */
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    /** The Constant EMAIL_MESSAGE. */
    private static final String EMAIL_MESSAGE = "{email.message}";

    /** The email. */
    @NotBlank(message = ProfileForm.NOT_BLANK_MESSAGE)
    @Email(message = ProfileForm.EMAIL_MESSAGE)
    private String email;

    /** The nick name. */
    @NotBlank(message = ProfileForm.NOT_BLANK_MESSAGE)
    private String nickName;

    /** The password. */
    @NotBlank(message = ProfileForm.NOT_BLANK_MESSAGE)
    private String password;

    /** The old password. */
    @NotBlank(message = ProfileForm.NOT_BLANK_MESSAGE)
    private String oldPassword;

    /** The private account. */
    private boolean privateAccount;

    /**
     * Create the default Profile Form.
     */
    public ProfileForm() {

    }

    /**
     * Gets the private account.
     *
     * @return the private account
     */
    public boolean getPrivateAccount() {
        return privateAccount;
    }

    /**
     * Sets the private account.
     *
     * @param isPrivateAccount
     *            the new private account
     */
    public void setPrivateAccount(boolean isPrivateAccount) {
        this.privateAccount = isPrivateAccount;
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
     * Gets the old password.
     *
     * @return the old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the old password.
     *
     * @param oldPassword
     *            the new old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Update account.
     *
     * @param account
     *            the account
     * @return the account
     */
    public Account updateAccount(Account account) {
        account.setEmail(getEmail());
        account.setNickName(getNickName());
        // Si se introduce una contraseña vacia no se tiene en cuenta
        if (password != null && !password.equals("")) {
            account.setPassword(getPassword());
        }
        return account;
    }
}
