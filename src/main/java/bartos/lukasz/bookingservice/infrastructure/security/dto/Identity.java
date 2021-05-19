package bartos.lukasz.bookingservice.infrastructure.security.dto;

import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Identity {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Role role;
    private String token;
}
