# CEASA AD Security

Este repositório é o **padrão reutilizável** de autenticação/autorização via **Active Directory (LDAPS)** para os sistemas da CEASA.

## Estrutura

- **ceasa-ad-security-starter**: biblioteca (JAR) com beans e mapeamento de grupos → roles.
- **ceasa-ad-security-demo**: aplicação exemplo (com telas simples) para validar o comportamento.

## Como reutilizar em outros sistemas (ex.: AUDESP)

1) Publique/instale o starter no seu repositório Maven (ou local):

```bash
mvn -DskipTests clean install
```

2) No projeto alvo, adicione a dependência:

```xml
<dependency>
  <groupId>br.com.ceasa.security</groupId>
  <artifactId>ceasa-ad-security-starter</artifactId>
  <version>0.2.0-SNAPSHOT</version>
</dependency>
```

3) Configure as propriedades no `application.properties` do sistema:

```properties
ceasa.security.ad.ldap.url=ldaps://SEU-AD:636
ceasa.security.ad.ldap.baseDn=DC=empresa,DC=local
ceasa.security.ad.ldap.bindDn=svc_ldap_app@empresa.local
ceasa.security.ad.ldap.bindPassword=***

ceasa.security.ad.groups.groupPrefix=APP_AUDESP_
ceasa.security.ad.groups.rolePrefix=ROLE_AUDESP_
ceasa.security.ad.groups.allowedRoles=USER,ADMIN
ceasa.security.ad.groups.groupSearchBase=OU=GRUPOS,OU=TI
ceasa.security.ad.groups.groupSearchFilter=(member={0})
```

4) No `SecurityConfig` do sistema, injete o `AuthenticationManager` (que o starter cria) e aplique suas regras (`authorizeHttpRequests`).

> O starter **não impõe** um `SecurityFilterChain`. Cada sistema mantém suas URLs e regras.

## Observações importantes

- Usuário autenticado no AD **mas fora dos grupos**: ele autentica, porém recebe **403** (e você decide a UX: `/sem-permissao`, JSON, etc.).
- Para evitar “loop” no browser, garanta que sua página de “sem permissão” esteja em `permitAll()`.

