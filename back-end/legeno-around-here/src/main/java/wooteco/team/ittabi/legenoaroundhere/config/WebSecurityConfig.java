package wooteco.team.ittabi.legenoaroundhere.config;

import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenDecoder;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenDecoder jwtTokenDecoder;
    private final IAuthenticationFacade authenticationFacade;

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
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("https://www.capzzang.co.kr");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Collections.singletonList("Location"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers(
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/join",
                "/login",
                "/mail-auth").permitAll()
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
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenDecoder, authenticationFacade),
                UsernamePasswordAuthenticationFilter.class);
    }
}

