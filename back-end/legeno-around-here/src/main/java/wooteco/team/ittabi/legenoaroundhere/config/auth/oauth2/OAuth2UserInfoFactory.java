package wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import wooteco.team.ittabi.legenoaroundhere.config.auth.exception.OAuth2AuthenticationProcessingException;
import wooteco.team.ittabi.legenoaroundhere.domain.user.AuthProvider;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
        Map<String, Object> attributes) {

        if (AuthProvider.GOOGLE.isSame(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        }

        throw new OAuth2AuthenticationProcessingException(
            "Sorry! Login with " + registrationId + " is not supported yet.");
    }
}
