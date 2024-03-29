package com.jeongyuneo.springsecurity.authentication.oauth2.entity;

import com.jeongyuneo.springsecurity.member.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String username;
    private final Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, String username, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.username = username;
        this.role = role;
    }
}
