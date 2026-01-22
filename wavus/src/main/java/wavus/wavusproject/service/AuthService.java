package wavus.wavusproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wavus.wavusproject.dto.requestdto.ReissueRequestDTO;
import wavus.wavusproject.dto.responseDto.AccessTokenResponseDTO;
import wavus.wavusproject.jwt.JwtProvider;
import wavus.wavusproject.jwt.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AccessTokenResponseDTO reissue(String refreshToken) {


        // 1) refresh 토큰 유효성(서명/만료)
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken is required");
        }
        if (!jwtProvider.isValid(refreshToken)) {
            // refresh 자체가 만료/위조 => 재로그인 필요
            throw new IllegalStateException("Refresh token expired or invalid");
        }

        // 2) refresh에서 userId, role 추출
        var jws = jwtProvider.parseToken(refreshToken);
        String userId = jws.getPayload().getSubject();
        String role = jws.getPayload().get("role", String.class);

        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("Refresh token subject missing");
        }
        if (role == null || role.isBlank()) {
            // JwtProvider에서 refresh에 role을 넣도록 수정해야 함
            throw new IllegalStateException("Role not found in refresh token");
        }

        // 3) Redis 저장된 refresh와 일치하는지 확인
        String saved = refreshTokenService.get(userId);
        if (saved == null) {
            // 서버에 저장된 refresh가 없음 => 로그아웃/만료/삭제된 상태
            throw new IllegalStateException("Refresh token not found (login required)");
        }
        if (!saved.equals(refreshToken)) {
            // 토큰 재사용/탈취/다른 기기에서 갱신 등
            throw new IllegalStateException("Refresh token mismatch (login required)");
        }

        // 4) 새 access 발급
        String newAccess = jwtProvider.createAccessToken(userId, role);

        return AccessTokenResponseDTO.builder()
                .accessToken(newAccess)
                .build();
    }
}
