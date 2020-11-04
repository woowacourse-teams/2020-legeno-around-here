package wooteco.team.ittabi.legenoaroundhere.config.auth;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AuthorizedRedirectUris {

    private final List<String> authorizedRedirectUris;

    public AuthorizedRedirectUris(
        @Value("${security.jwt.token.oauth2.authorizedRedirectUri}") String authorizedRedirectUri) {
        this.authorizedRedirectUris = Collections.singletonList(authorizedRedirectUri);
    }

    public List<String> getAuthorizedRedirectUris() {
        return authorizedRedirectUris;
    }
}
