package wooteco.team.ittabi.legenoaroundhere.config;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

    Object getPrincipal();

    void setAuthentication(Authentication authentication);
}
