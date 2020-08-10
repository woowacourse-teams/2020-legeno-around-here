package wooteco.team.ittabi.legenoaroundhere.config;

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

    public static final String BASE_PACKAGE_CONTROLLER_NAME = "wooteco.team.ittabi.legenoaroundhere.controller";
    public static final String DEFAULT_TITLE = "Legeno Around Here ";

    private String groupName;

    @Bean
    public Docket postApiDocket() {
        groupName = "posts";
        return getDocket(groupName, "/posts/**", DEFAULT_TITLE + groupName);
    }

    @Bean
    public Docket sectorApiDocket() {
        groupName = "sectors";
        return getDocket(groupName, "/sectors/**", DEFAULT_TITLE + groupName);
    }

    @Bean
    public Docket userJoinApiDocket() {
        groupName = "join";
        return getDocket(groupName, "/join/**", DEFAULT_TITLE + groupName);
    }

    @Bean
    public Docket userLoginApiDocket() {
        groupName = "login";
        return getDocket(groupName, "/login/**", DEFAULT_TITLE + groupName);
    }

    private Docket getDocket(String groupName, String groupUrl, String title) {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .groupName(groupName)
            .select()
            .apis(RequestHandlerSelectors
                .basePackage(BASE_PACKAGE_CONTROLLER_NAME))
            .paths(PathSelectors.ant(groupUrl))
            .build()
            .apiInfo(apiInfo(title, groupName));
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
