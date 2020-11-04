package wooteco.team.ittabi.legenoaroundhere.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication();
    }

    @Override
    public void setAuthentication(Authentication authToken) {
        SecurityContextHolder
            .getContext()
            .setAuthentication(authToken);
    }

    @Override
    public Object getPrincipal() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
    }
}
