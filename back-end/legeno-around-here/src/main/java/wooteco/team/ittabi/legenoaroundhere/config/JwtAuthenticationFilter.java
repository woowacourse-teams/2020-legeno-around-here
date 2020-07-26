package wooteco.team.ittabi.legenoaroundhere.config;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenDecoder;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenDecoder jwtTokenDecoder;

    public JwtAuthenticationFilter(JwtTokenDecoder jwtTokenDecoder) {
        this.jwtTokenDecoder = jwtTokenDecoder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader("X-AUTH-TOKEN");

        if (isValidToken(token)) {
            Authentication authentication = jwtTokenDecoder.getAuthentication(token);
            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        return Objects.nonNull(token)
            && jwtTokenDecoder.isValidToken(token);
    }
}
