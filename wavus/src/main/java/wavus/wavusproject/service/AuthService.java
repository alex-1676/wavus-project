package wavus.wavusproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import wavus.wavusproject.jwt.JwtProvider;
import wavus.wavusproject.jwt.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    @Value("${app.jwt.refresh-exp-days}")
    private long refreshExpMs;

}
