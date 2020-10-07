package wooteco.team.ittabi.legenoaroundhere.config;

import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2.CustomOAuth2UserService;
import wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2.OAuth2AuthenticationFailureHandler;
import wooteco.team.ittabi.legenoaroundhere.config.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenDecoder;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenDecoder jwtTokenDecoder;
    private final IAuthenticationFacade authenticationFacade;

    @Value("${security.cors.origin.domain.frontend}")
    private final String frontendDomain;

    @Value("${security.cors.origin.domain.adminTool}")
    private final String adminToolDomain;

    private final UserService userService;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public IAuthenticationFacade authenticationFacadeBean() {
        return new AuthenticationFacade();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("crossOrigin - Frontend Domain: {}", frontendDomain);
        log.info("crossOrigin - AdminTool Domain: {}", adminToolDomain);
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(frontendDomain);
        configuration.addAllowedOrigin(adminToolDomain);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Collections.singletonList("Location"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/h2-console/**",
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/check-joined",
                "/join",
                "/admin/login",
                "/login",
                "/users/me/password",
                "/mail-auth/**",
                "/profile",
                "/auth/**",
                "/oauth2/**",
                "/favicon.ico").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().hasAnyRole("USER", "ADMIN")
            .and()
            .cors()
            .and()
            .headers()
            .addHeaderWriter(
                new XFrameOptionsHeaderWriter(
                    new WhiteListedAllowFromStrategy(Collections.singletonList("localhost"))
                )
            ).frameOptions().sameOrigin()
            .and()
            .oauth2Login()
            .loginPage("/login")
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("/login/oauth2/code/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenDecoder, authenticationFacade),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception {
        authenticationManagerBuilder
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

