package wooteco.team.ittabi.legenoaroundhere.config.auth;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppProperties {

    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    public static final class Auth {

        @Value("${security.jwt.token.secret-key}")
        private String tokenSecret;

        @Value("${security.jwt.token.expire-length}")
        private long tokenExpirationMsec;
    }

    @Getter
    public static final class OAuth2 {

        @Value("${security.jwt.token.oauth2.authorizedRedirectUri}")
        private String authorizedRedirectUri;

        private List<String> authorizedRedirectUris
            = Collections.singletonList(authorizedRedirectUri);
    }
}
