package bartos.lukasz.bookingservice.infrastructure.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessDto {
    private String username;
    private String password;
    private String email;
}
