package br.com.ceasa.security.ad.starter.config;

import br.com.ceasa.security.ad.starter.ldap.CeasaLdapAuthoritiesPopulator;
import br.com.ceasa.security.ad.starter.props.CeasaAdGroupProperties;
import br.com.ceasa.security.ad.starter.props.CeasaAdLdapProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties({CeasaAdLdapProperties.class, CeasaAdGroupProperties.class})
public class CeasaAdSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultSpringSecurityContextSource contextSource(CeasaAdLdapProperties props) {
        DefaultSpringSecurityContextSource contextSource =
                new DefaultSpringSecurityContextSource(List.of(props.getUrl()), props.getBaseDn());

        // Conta de serviço para search do DN do usuário + grupos
        contextSource.setUserDn(props.getBindDn());
        contextSource.setPassword(props.getBindPassword());

        // AD pode retornar referrals. Em ambientes com um único domínio, ignorar costuma ser o mais seguro.
        // (o método setIgnorePartialResultException não existe nessa classe; o que funciona é referral="ignore")
        contextSource.setReferral("ignore");

        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public CeasaLdapAuthoritiesPopulator ldapAuthoritiesPopulator(BaseLdapPathContextSource contextSource,
                                                                  CeasaAdGroupProperties cfg) {
        return new CeasaLdapAuthoritiesPopulator(contextSource, cfg);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(DefaultSpringSecurityContextSource contextSource,
                                                       CeasaAdLdapProperties props,
                                                       CeasaLdapAuthoritiesPopulator authoritiesPopulator) {

        FilterBasedLdapUserSearch userSearch =
                new FilterBasedLdapUserSearch(props.getUserSearchBase(), props.getUserSearchFilter(), contextSource);

        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserSearch(userSearch);

        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(bindAuthenticator, authoritiesPopulator);
        return new ProviderManager(provider);
    }
}
