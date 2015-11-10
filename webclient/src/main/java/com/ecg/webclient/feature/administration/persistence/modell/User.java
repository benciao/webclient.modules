package com.ecg.webclient.feature.administration.persistence.modell;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ecg.webclient.feature.administration.authentication.PasswordEncoder;

/**
 * Entität eines Benutzers.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_USER")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long              id;
    @Column(unique = true)
    private String            login;
    private String            password;
    private Date              passwordChangedTimeStamp;
    private int               loginAttempts;
    private String            firstname;
    private String            lastname;
    private boolean           enabled;
    private boolean           accountLocked;
    private boolean           changePasswordOnNextLogin;
    private boolean           internal;
    private String            email;
    @OneToOne
    private Client            defaultClient;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SEC_USER_SEC_GROUP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private List<Group>       groups;

    public User()
    {}

    @Transient
    public User bind(User newUser)
    {
        setLogin(newUser.getLogin());
        setInternal(newUser.isInternal());
        setFirstname(newUser.getFirstname());
        setLastname(newUser.getLastname());
        // wichtig, damit es nicht genullt wird in der DB bei Nichtänderung
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty())
        {
            setPassword(PasswordEncoder.encodeComplex(newUser.getPassword(), Long.toString(getId())));
        }
        setDefaultClient(newUser.getDefaultClient());
        setGroups(newUser.getGroups());
        setEnabled(newUser.isEnabled());
        setEmail(newUser.getEmail());
        setAccountLocked(newUser.isAccountLocked());
        setLoginAttempts(newUser.getLoginAttempts());
        setChangePasswordOnNextLogin(newUser.isChangePasswordOnNextLogin());

        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof User))
        {
            return false;
        }
        User other = (User) obj;
        if (email == null)
        {
            if (other.email != null)
            {
                return false;
            }
        }
        else if (!email.equals(other.email))
        {
            return false;
        }
        if (firstname == null)
        {
            if (other.firstname != null)
            {
                return false;
            }
        }
        else if (!firstname.equals(other.firstname))
        {
            return false;
        }
        if (id != other.id)
        {
            return false;
        }
        if (lastname == null)
        {
            if (other.lastname != null)
            {
                return false;
            }
        }
        else if (!lastname.equals(other.lastname))
        {
            return false;
        }
        if (login == null)
        {
            if (other.login != null)
            {
                return false;
            }
        }
        else if (!login.equals(other.login))
        {
            return false;
        }
        return true;
    }

    public Client getDefaultClient()
    {
        return defaultClient;
    }

    public String getEmail()
    {
        return email;
    }

    @Transient
    public List<Group> getEnabledGroups()
    {
        List<Group> result = new ArrayList<Group>();

        for (Group group : groups)
        {
            if (group.isEnabled())
            {
                result.add(group);
            }
        }

        return result;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    public long getId()
    {
        return id;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getLogin()
    {
        return login;
    }

    public int getLoginAttempts()
    {
        return loginAttempts;
    }

    public String getPassword()
    {
        return password;
    }

    public Date getPasswordChangedTimeStamp()
    {
        return passwordChangedTimeStamp;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        return result;
    }

    public boolean isAccountLocked()
    {
        return accountLocked;
    }

    public boolean isChangePasswordOnNextLogin()
    {
        return changePasswordOnNextLogin;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isInternal()
    {
        return internal;
    }

    public void setAccountLocked(boolean accountLocked)
    {
        this.accountLocked = accountLocked;
    }

    public void setChangePasswordOnNextLogin(boolean changePasswordOnNextLogin)
    {
        this.changePasswordOnNextLogin = changePasswordOnNextLogin;
    }

    public void setDefaultClient(Client defaultClient)
    {
        this.defaultClient = defaultClient;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public void setGroups(List<Group> groups)
    {
        this.groups = groups;
    }

    public void setId(long id)
    {
        if (id != -1)
        {
            this.id = id;
        }
    }

    public void setInternal(boolean internal)
    {
        this.internal = internal;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setLoginAttempts(int loginAttempts)
    {
        this.loginAttempts = loginAttempts;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPasswordChangedTimeStamp(Date passwordChangedTimeStamp)
    {
        this.passwordChangedTimeStamp = passwordChangedTimeStamp;
    }

}
