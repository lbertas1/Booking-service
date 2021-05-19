package bartos.lukasz.bookingservice.domain.user.dto;

import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String birthDate;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String country;
    private Role role;
}
