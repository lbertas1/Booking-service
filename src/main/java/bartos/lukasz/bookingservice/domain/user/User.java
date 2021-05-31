package bartos.lukasz.bookingservice.domain.user;

import bartos.lukasz.bookingservice.domain.user.dto.UpdateUserDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserResponseDto;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String country;
    private Boolean isNotLocked;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserDto toUserDto() {
        return UserDto
                .builder()
                .id(id)
                .name(name)
                .surname(surname)
                .birthDate(birthday)
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
                .birthDate(birthday.toString())
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

    public UpdateUserDto toUpdateUserDto() {
        return UpdateUserDto
                .builder()
                .id(id)
                .name(name)
                .surname(surname)
                .birthDate(birthday.toString())
                .country(country)
                .zipCode(zipCode)
                .city(city)
                .address(address)
                .phone(phone)
                .email(email)
                .build();
    }
}