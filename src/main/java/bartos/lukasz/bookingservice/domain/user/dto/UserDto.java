package bartos.lukasz.bookingservice.domain.user.dto;

import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String country;
    private Boolean isNotLocked;
    private Role role;

    public User toUser() {
        return User
                .builder()
                .id(id)
                .name(name)
                .surname(surname)
                .birthday(birthDate)
                .username(username)
                .password(password)
                .country(country)
                .zipCode(zipCode)
                .city(city)
                .address(address)
                .phone(phone)
                .email(email)
                .isNotLocked(isNotLocked)
                .role(role)
                .build();
    }

    public UserResponseDto toUserResponseDto() {
        return UserResponseDto
                .builder()
                .id(id)
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toString())
                .username(username)
                .country(country)
                .zipCode(zipCode)
                .city(city)
                .address(address)
                .phone(phone)
                .email(email)
                .role(role)
                .build();
    }
}
