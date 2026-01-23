package wavus.wavusproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wavus.wavusproject.auth.PrincipalDetails;
import wavus.wavusproject.entity.User;
import wavus.wavusproject.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + loginId));

        return new PrincipalDetails(user);
    }
}
