package wavus.wavusproject.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import wavus.wavusproject.auth.PrincipalDetails;
import wavus.wavusproject.dto.requestdto.LoginRequestDTO;
import wavus.wavusproject.dto.responseDto.TokenResponseDTO;
import wavus.wavusproject.jwt.JwtProvider;
import wavus.wavusproject.jwt.RefreshTokenService;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    @Value("${app.jwt.refresh-exp-days}")
    private long refreshExpMs;

    public TokenResponseDTO login(LoginRequestDTO loginReq) {

        log.info("Login RequestDTO : {}", loginReq);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getLoginId(),
                        loginReq.getPassword()
                )
        );

        PrincipalDetails principal =
                (PrincipalDetails) authentication.getPrincipal();

        String userId = principal.getUsername();
        String role = principal.getUser().getRole().name();

        String accessToken = jwtProvider.createAccessToken(userId,role);
        String refreshToken = jwtProvider.createRefreshToken(userId,role);

        refreshTokenService.save(userId,refreshToken, Duration.ofDays(refreshExpMs));

        return new TokenResponseDTO(accessToken, refreshToken,userId , role);
    }

}
