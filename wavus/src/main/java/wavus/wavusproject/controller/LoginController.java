package wavus.wavusproject.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wavus.wavusproject.dto.requestdto.LoginRequestDTO;
import wavus.wavusproject.dto.responseDto.TokenResponseDTO;
import wavus.wavusproject.service.LoginService;

import java.time.Duration;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth2")
public class LoginController {

    private  final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
      log.info("Login RequestDTO : {}", loginRequestDTO);
        TokenResponseDTO tokens =  loginService.login(loginRequestDTO);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(false) // **운영 https면 true로 변경
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        TokenResponseDTO body = new TokenResponseDTO(tokens.getAccessToken(), null, tokens.getUserID(), tokens.getRole());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);

    }
}
