package wavus.wavusproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wavus.wavusproject.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

}
