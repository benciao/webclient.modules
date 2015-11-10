package com.ecg.webclient.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration
{
    @Bean
    public LdapContextSource contextSource()
    {
        // nur Dummy Werte, wird zur Laufzeit durch dynamische Konfiguration ersetzt
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://server.domain.com:389");
        contextSource.setBase("OU=Employees,OU=Users,DC=domain,DC=com");
        contextSource.setUserDn("CN=myuserid,OU=employees,OU=Users,DC=domain,DC=com");
        contextSource.setPassword("ldap.password");
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate()
    {
        return new LdapTemplate(contextSource());
    }

}
