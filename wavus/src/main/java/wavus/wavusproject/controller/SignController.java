package wavus.wavusproject.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wavus.wavusproject.entity.User;

import wavus.wavusproject.dto.requestdto.SignUpRequestDTO;
import wavus.wavusproject.dto.responseDto.SignUpResponseDTO;

import wavus.wavusproject.service.SignUpService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth2")
public class SignController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpReqDTO) {
        try {
            User user = signUpService.signUp(signUpReqDTO);
            // 역할까지 같이 응답
            return ResponseEntity.ok().body(
                    SignUpResponseDTO.builder()
                            .userID(user.getUserid())
                            .role(user.getRole())
                            .build()
            );
        } catch (IllegalAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }
}
