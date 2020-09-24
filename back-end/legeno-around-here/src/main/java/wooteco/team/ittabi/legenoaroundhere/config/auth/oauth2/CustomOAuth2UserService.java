package wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import wooteco.team.ittabi.legenoaroundhere.config.auth.UserPrincipal;
import wooteco.team.ittabi.legenoaroundhere.config.auth.exception.OAuth2AuthenticationProcessingException;
import wooteco.team.ittabi.legenoaroundhere.domain.user.AuthProvider;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
        throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest,
        OAuth2User oAuth2User) {

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo oAuth2UserInfo
            = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        validateEmail(oAuth2UserInfo);

        Optional<User> userOptional
            = userRepository.findByEmail(new Email(oAuth2UserInfo.getEmail()));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            validateProvider(registrationId, user);
            return UserPrincipal.create(user, attributes);
        }

        User user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        return UserPrincipal.create(user, attributes);
    }

    private void validateProvider(String registrationId, User user) {
        if (user.isDifferentProvider(registrationId)) {
            throw new OAuth2AuthenticationProcessingException(
                "Looks like you're signed up with " +
                    user.getProvider() + " account. Please use your " + user.getProvider() +
                    " account to login.");
        }
    }

    private void validateEmail(OAuth2UserInfo oAuth2UserInfo) {
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException(
                "OAuth2 provider에서 Email을 찾을 수 없습니다.");
        }
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest,
        OAuth2UserInfo oAuth2UserInfo) {
        AuthProvider provider
            = AuthProvider.of(oAuth2UserRequest.getClientRegistration().getRegistrationId());

        User user = User.builder()
            .nickname(oAuth2UserInfo.getNickname())
            .email(oAuth2UserInfo.getEmail())
            .password(provider.name())
            .build();

        user.setProvider(provider);
        user.setProviderId(oAuth2UserInfo.getId());

        return userRepository.save(user);
    }
}
