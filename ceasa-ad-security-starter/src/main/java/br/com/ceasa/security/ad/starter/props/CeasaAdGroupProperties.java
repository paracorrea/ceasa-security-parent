package br.com.ceasa.security.ad.starter.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Propriedades para mapear grupos do AD em roles do Spring.
 *
 * Prefixo: ceasa.security.ad.groups
 */
@ConfigurationProperties(prefix = "ceasa.security.ad.groups")
public class CeasaAdGroupProperties {

    /** Prefixo dos grupos no AD (CN). Ex: APP_AUDESP_ ou APP_CEASA_SECURITY_ */
    private String groupPrefix = "APP_";

    /** Prefixo das roles no Spring. Ex: ROLE_AUDESP_ ou ROLE_CEASA_ */
    private String rolePrefix = "ROLE_";

    /** Quais roles aceitas (sem prefixo ROLE_). Ex: USER,ADMIN,SUPORTE */
    private List<String> allowedRoles = List.of("USER");

    /** Onde procurar grupos (relativo ao baseDn). Ex: OU=GRUPOS,OU=PID,OU=Ceasa Campinas */
    private String groupSearchBase = "";

    /** Filtro de busca de grupos. Default (member={0}) */
    private String groupSearchFilter = "(member={0})";

    public String getGroupPrefix() { return groupPrefix; }
    public void setGroupPrefix(String groupPrefix) { this.groupPrefix = groupPrefix; }

    public String getRolePrefix() { return rolePrefix; }
    public void setRolePrefix(String rolePrefix) { this.rolePrefix = rolePrefix; }

    public List<String> getAllowedRoles() { return allowedRoles; }
    public void setAllowedRoles(List<String> allowedRoles) { this.allowedRoles = allowedRoles; }

    public String getGroupSearchBase() { return groupSearchBase; }
    public void setGroupSearchBase(String groupSearchBase) { this.groupSearchBase = groupSearchBase; }

    public String getGroupSearchFilter() { return groupSearchFilter; }
    public void setGroupSearchFilter(String groupSearchFilter) { this.groupSearchFilter = groupSearchFilter; }
}
