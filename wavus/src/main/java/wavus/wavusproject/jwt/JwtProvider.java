package wavus.wavusproject.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    private SecretKey key(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId, String role){
        Instant now = Instant.now();
        Instant exp =  now.plus(jwtProperties.getAccessExpMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(userId)
                .claim("role",role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key())
                .compact();
    }

    public String createRefreshToken(String userId, String role){
        Instant now = Instant.now();
        Instant exp = now.plus(jwtProperties.getRefreshExpDays(), ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(userId)
                .claim("role",role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key())
                .compact();
    }

    public Jws<Claims> parseToken(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValid(String token){
        try{
            parseToken(token);
            return true;
        }catch(Exception e){{
        return false;}
        }
    }

    public String getUserID(String token){
        Claims claims =  Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getRole(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token)
                .getPayload();
        Object role = claims.get("role");
        return role == null ? null : role.toString();
    }




}
