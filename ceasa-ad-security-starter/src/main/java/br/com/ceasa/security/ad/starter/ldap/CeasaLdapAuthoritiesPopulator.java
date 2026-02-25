package br.com.ceasa.security.ad.starter.ldap;

import br.com.ceasa.security.ad.starter.props.CeasaAdGroupProperties;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converte grupos do AD em roles padronizadas.
 *
 * Exemplo:
 *  - Grupo AD: APP_AUDESP_ADMIN
 *  - authority raw (delegate): ROLE_APP_AUDESP_ADMIN
 *  - authority final: ROLE_AUDESP_ADMIN
 */
public class CeasaLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private final DefaultLdapAuthoritiesPopulator delegate;
    private final String groupPrefix; // APP_AUDESP_
    private final String rolePrefix;  // ROLE_AUDESP_
    private final Set<String> allowedRolesUpper;

    public CeasaLdapAuthoritiesPopulator(BaseLdapPathContextSource contextSource, CeasaAdGroupProperties cfg) {
        this.groupPrefix = Objects.requireNonNullElse(cfg.getGroupPrefix(), "");
        this.rolePrefix = Objects.requireNonNullElse(cfg.getRolePrefix(), "ROLE_");

        this.allowedRolesUpper = cfg.getAllowedRoles() == null
                ? Set.of()
                : cfg.getAllowedRoles().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        this.delegate = new DefaultLdapAuthoritiesPopulator(contextSource, cfg.getGroupSearchBase());
        this.delegate.setGroupSearchFilter(cfg.getGroupSearchFilter());
        this.delegate.setSearchSubtree(true);

        // delegate devolve ROLE_<CN_DO_GRUPO>
        this.delegate.setRolePrefix("ROLE_");
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        Collection<? extends GrantedAuthority> raw = delegate.getGrantedAuthorities(userData, username);

        Set<String> mapped = raw.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::mapGroupRoleToRole)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // Ajuda no debug (não use para autorização)
        mapped.add("ROLE_AUTHENTICATED");

        return mapped.stream().map(SimpleGrantedAuthority::new).toList();
    }

    private String mapGroupRoleToRole(String authority) {
        // authority vem como ROLE_<CN>
        final String rawPrefix = "ROLE_";
        if (authority == null || !authority.startsWith(rawPrefix)) return null;

        String groupName = authority.substring(rawPrefix.length());
        if (!groupName.startsWith(groupPrefix)) return null;

        String suffix = groupName.substring(groupPrefix.length());
        if (suffix.isBlank()) return null;

        String suffixUpper = suffix.toUpperCase(Locale.ROOT);
        if (!allowedRolesUpper.isEmpty() && !allowedRolesUpper.contains(suffixUpper)) {
            return null;
        }

        return rolePrefix + suffixUpper;
    }
}
