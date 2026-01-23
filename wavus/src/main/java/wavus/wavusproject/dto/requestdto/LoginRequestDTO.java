package wavus.wavusproject.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LoginRequestDTO {

    private String userId;
    private String password;
}
