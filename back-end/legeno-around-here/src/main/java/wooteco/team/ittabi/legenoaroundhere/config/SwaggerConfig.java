package wooteco.team.ittabi.legenoaroundhere.config;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AREAS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AREAS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AWARDS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.AWARDS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.IMAGES_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.MAIL_AUTH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.MAIL_AUTH_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ME_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.NOTICES;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.NOTICES_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.PROFILE;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.PROFILE_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.RANKING;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.RANKING_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.REPORTS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.REPORTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ZZANGS;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.ZZANGS_PATH_WITH_SLASH;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String CONTROLLER_PACKAGE_NAME = "wooteco.team.ittabi.legenoaroundhere.controller";
    private static final String DEFAULT_TITLE = "Legeno Around Here ";
    private static final String ALL_ANT_PATTERN_FORMAT = "/**%s**";

    private String groupName;

    @Bean
    public Docket areaApiDocket() {
        groupName = AREAS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, AREAS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket awardApiDocket() {
        groupName = AWARDS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, AWARDS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket commentApiDocket() {
        groupName = COMMENTS;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, COMMENTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket imageApiDocket() {
        groupName = IMAGES;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, IMAGES_PATH_WITH_SLASH));
    }

    @Bean
    public Docket mailAuthApiDocket() {
        groupName = MAIL_AUTH;
        return getDocket(groupName,
            String.format(ALL_ANT_PATTERN_FORMAT, MAIL_AUTH_PATH_WITH_SLASH));
    }

    @Bean
    public Docket meApiDocket() {
        groupName = ME;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, ME_PATH_WITH_SLASH));
    }

    @Bean
    public Docket noticeApiDocket() {
        groupName = NOTICES;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, NOTICES_PATH_WITH_SLASH));
    }

    @Bean
    public Docket postApiDocket() {
        groupName = POSTS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, POSTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket profileApiDocket() {
        groupName = PROFILE;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, PROFILE_PATH_WITH_SLASH));
    }

    @Bean
    public Docket rankingApiDocket() {
        groupName = RANKING;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, RANKING_PATH_WITH_SLASH));
    }

    @Bean
    public Docket reportApiDocket() {
        groupName = REPORTS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, REPORTS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket sectorApiDocket() {
        groupName = SECTORS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, SECTORS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket userApiDocket() {
        groupName = USERS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, USERS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket zzangApiDocket() {
        groupName = ZZANGS;
        return getDocket(groupName, String.format(ALL_ANT_PATTERN_FORMAT, ZZANGS_PATH_WITH_SLASH));
    }

    @Bean
    public Docket userImageApiDocket() {
        groupName = "userImages";
        return getDocket(groupName, "/user-images/**");
    }

    @Bean
    public Docket joinApiDocket() {
        groupName = "join";
        return getDocket(groupName, "/join/**");
    }

    @Bean
    public Docket loginApiDocket() {
        groupName = "login";
        return getDocket(groupName, "/login/**");
    }

    private Docket getDocket(String groupName, String groupUrl) {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors
                .basePackage(CONTROLLER_PACKAGE_NAME))
            .paths(PathSelectors.ant(groupUrl))
            .build()
            .apiInfo(apiInfo(DEFAULT_TITLE + groupName, groupName));
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
            title,
            "이따비의 팀 프로젝트 Legeno Around Here의 API Docs",
            version,
            "www.capzzang.co.kr",
            new Contact("Conetact Me", "www.capzzang.co.kr", "aegis1920@gmail.com"),
            "Ittabi Licenses",
            "www.capzzang.co.kr",
            new ArrayList<>()
        );
    }
}
