package wooteco.team.ittabi.legenoaroundhere.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenGenerator {

    private final String secretKey;
    private final Long tokenValidTime;

    public JwtTokenGenerator(@Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expire-length}") Long tokenValidTime) {
        this.secretKey = Base64.getEncoder()
            .encodeToString(secretKey.getBytes());
        this.tokenValidTime = tokenValidTime;
    }

    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims()
            .setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
}
