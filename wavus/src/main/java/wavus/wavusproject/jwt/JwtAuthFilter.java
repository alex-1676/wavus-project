package wavus.wavusproject.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//Access토큰 검증용
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean skip = uri.startsWith("/api/auth2/") || uri.startsWith("/auth2/");
        System.out.println("[JWT] shouldNotFilter uri=" + uri + " skip=" + skip);
        return skip;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWT FILTER URI = " + request.getRequestURI());

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 1) 토큰 없으면 그냥 통과 (나중에 authenticated에서 걸림)
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();
        if (token.isEmpty()) {
            unauthorized(response, "Empty Bearer token");
            return;
        }

        // 2) 여기서 파싱 1번만 수행 (파싱 성공 = 유효)
        final Jws<Claims> jws;
        try {
            jws = jwtProvider.parseToken(token);
        } catch (ExpiredJwtException e) {
            // 만료
            unauthorized(response, "Access token expired");
            return;
        } catch (JwtException e) {
            // 위조/변조/서명불일치/형식오류 등 JWT 관련 예외
            unauthorized(response, "Invalid access token");
            return;
        } catch (Exception e) {
            // 기타 예상치 못한 예외
            unauthorized(response, "Unauthorized");
            return;
        }

        // 3) claims 추출 후 SecurityContext 세팅
        String userId = jws.getPayload().getSubject();
        String role = jws.getPayload().get("role", String.class);

        var auth = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        if (response.isCommitted()) return;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
