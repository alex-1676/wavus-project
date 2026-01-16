package wavus.wavusproject.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")

public class JwtProperties {
    String secret;
    long accessExpMinutes;
    long refreshExpDays;

}
