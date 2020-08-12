package wooteco.team.ittabi.legenoaroundhere.config;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

    Object getPrincipal();
}
