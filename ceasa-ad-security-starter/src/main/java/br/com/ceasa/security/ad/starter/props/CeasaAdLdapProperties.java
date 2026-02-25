package br.com.ceasa.security.ad.starter.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades para conexão e busca no AD via LDAPS.
 *
 * Prefixo: ceasa.security.ad.ldap
 */
@ConfigurationProperties(prefix = "ceasa.security.ad.ldap")
public class CeasaAdLdapProperties {

    /** ldaps://SRV-ADDS-01.ceasacps.com.br:636 */
    private String url;

    /** DC=ceasacps,DC=com,DC=br */
    private String baseDn;

    /** Conta de serviço para search (UPN ou DN). Ex: svc_ldap_app@ceasacps.com.br */
    private String bindDn;

    /** Senha da conta de serviço */
    private String bindPassword;

    /** Base para procurar usuários. Vazio = baseDn */
    private String userSearchBase = "";

    /** Filtro para procurar usuário. Default sAMAccountName */
    private String userSearchFilter = "(sAMAccountName={0})";

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBaseDn() { return baseDn; }
    public void setBaseDn(String baseDn) { this.baseDn = baseDn; }

    public String getBindDn() { return bindDn; }
    public void setBindDn(String bindDn) { this.bindDn = bindDn; }

    public String getBindPassword() { return bindPassword; }
    public void setBindPassword(String bindPassword) { this.bindPassword = bindPassword; }

    public String getUserSearchBase() { return userSearchBase; }
    public void setUserSearchBase(String userSearchBase) { this.userSearchBase = userSearchBase; }

    public String getUserSearchFilter() { return userSearchFilter; }
    public void setUserSearchFilter(String userSearchFilter) { this.userSearchFilter = userSearchFilter; }
}
