package bartos.lukasz.bookingservice.infrastructure.security.dto;

import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentity {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Role role;
    private String token;
}
