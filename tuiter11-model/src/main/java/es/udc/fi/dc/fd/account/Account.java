package es.udc.fi.dc.fd.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * The Class Account.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = Account.FIND_BY_ID,
                query = "select a from Account a where a.id = :accId"),
        @NamedQuery(name = Account.FIND_BY_EMAIL,
                query = "select a from Account a where a.email = :email"),
        @NamedQuery(name = Account.FIND_BY_NICK,
                query = "select a from Account a where a.nickName = :nickName"),
        @NamedQuery(
                name = Account.FIND_ACCOUNTS,
                query = "select a from Account a where LCASE(a.nickName) LIKE LCASE(:keyword)"),
        @NamedQuery(
                name = Account.FIND_NOT_CONFIRMED_ACCOUNS,
                query = "select a from Account a where a.role LIKE 'ROLE_NOT_CONFIRMED')") })
public class Account implements java.io.Serializable {

    /** The Constant FIND_BY_ID. */
    public static final String FIND_BY_ID = "Account.findById";

    /** The Constant FIND_BY_EMAIL. */
    public static final String FIND_BY_EMAIL = "Account.findByEmail";

    /** The Constant FIND_BY_NICK. */
    public static final String FIND_BY_NICK = "Account.findByNick";

    /** The Constant FIND_ACCOUNTS. */
    public static final String FIND_ACCOUNTS = "Account.findAccounts";

    /** The Constant FIND_NOT_CONFIRMED_ACCOUNS. */
    public static final String FIND_NOT_CONFIRMED_ACCOUNS = "Account.findNotConfirmedAccounts";

    /** The id. */
    @Id
    @GeneratedValue
    private Long id;

    /** The email. */
    @Column(unique = true)
    private String email;

    /** The nick name. */
    @Column(unique = true)
    private String nickName;

    /** The password. */
    @JsonIgnore
    private String password;

    /** The private account. */
    @Column
    private Integer privateAccount;

    /** The role. */
    @Column
    private String role;

    /**
     * Create a empty account.
     */
    protected Account() {

    }

    /**
     * Instantiates a new account.
     *
     * @param email
     *            the email
     * @param nickName
     *            the nick name
     * @param password
     *            the password
     * @param role
     *            the role
     */
    public Account(String email, String nickName, String password, String role) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.role = role.equals("ROLE_ADMIN") ? "ROLE_ADMIN"
                : "ROLE_NOT_CONFIRMED";
        this.privateAccount = null;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
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
     * Gets the private account.
     *
     * @return the private account
     */
    public Integer getPrivateAccount() {
        return privateAccount;
    }

    /**
     * Sets the private account.
     *
     * @param isPrivate
     *            the new private account
     */
    public void setPrivateAccount(Integer isPrivate) {
        this.privateAccount = isPrivate;
    }

    /**
     * Gets the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role
     *            the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Account [id=" + id + ", email=" + email + ", nickName="
                + nickName + ", role=" + role + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Account other = (Account) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nickName == null) {
            if (other.nickName != null) {
                return false;
            }
        } else if (!nickName.equals(other.nickName)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (role == null) {
            if (other.role != null) {
                return false;
            }
        } else if (!role.equals(other.role)) {
            return false;
        }
        return true;
    }

}
