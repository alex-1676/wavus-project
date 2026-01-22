package wavus.wavusproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wavus.wavusproject.dto.requestdto.ReissueRequestDTO;
import wavus.wavusproject.dto.responseDto.AccessTokenResponseDTO;
import wavus.wavusproject.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth2")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public AccessTokenResponseDTO reissue(
            @CookieValue(name = "refreshToken",required = false) String refreshToken) {
        return  authService.reissue(refreshToken);
    }

}
