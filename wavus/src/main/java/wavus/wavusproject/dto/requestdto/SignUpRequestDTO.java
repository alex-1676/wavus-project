package wavus.wavusproject.dto.requestdto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wavus.wavusproject.entity.Role;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SignUpRequestDTO {

    private String userId;
    private String password;
    private String regionCode;
}
