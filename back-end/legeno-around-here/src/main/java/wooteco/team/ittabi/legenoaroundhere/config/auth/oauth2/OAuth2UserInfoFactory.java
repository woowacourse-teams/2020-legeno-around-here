package wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2;

import java.util.Map;
import wooteco.team.ittabi.legenoaroundhere.config.auth.exception.OAuth2AuthenticationProcessingException;
import wooteco.team.ittabi.legenoaroundhere.domain.user.AuthProvider;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
        Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.name())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.name())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                "Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
