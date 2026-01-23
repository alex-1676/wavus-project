package wavus.wavusproject.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wavus.wavusproject.entity.Role;
import wavus.wavusproject.entity.User;

import wavus.wavusproject.dto.requestdto.SignUpRequestDTO;
import wavus.wavusproject.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(SignUpRequestDTO signReq) throws IllegalAccessException {
        if(userRepository.existsByUserid(signReq.getUserId())){
            throw new IllegalAccessException("userId already exists");
        }

        String encodedPassword = passwordEncoder.encode(signReq.getPassword());
        User user = User.builder()
                .userid(signReq.getUserId())
                .password(encodedPassword)
                .regionCode(signReq.getRegionCode())
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }



}
