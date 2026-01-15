package wavus.wavusproject.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wavus.wavusproject.entity.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {
    private String userID;
    private Role role;
}
