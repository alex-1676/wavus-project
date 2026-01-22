package wavus.wavusproject.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String userID;
    private String role;


}


