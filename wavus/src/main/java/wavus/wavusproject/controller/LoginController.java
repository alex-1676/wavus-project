package wavus.wavusproject.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wavus.wavusproject.dto.requestdto.LoginRequestDTO;
import wavus.wavusproject.dto.responseDto.TokenResponseDTO;

import wavus.wavusproject.service.LoginService;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class LoginController {

    private  final LoginService loginService;

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
      log.info("Login RequestDTO : {}", loginRequestDTO);
        return loginService.login(loginRequestDTO);
    }
}
