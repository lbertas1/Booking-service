package bartos.lukasz.bookingservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class RegisterUserDto {

    private String name;
    private String surname;
    private String username;
    private String birthDate;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String country;

    public UserDto toUserDto() {
        return UserDto
                .builder()
                .username(username)
                .name(name)
                .surname(surname)
                .birthDate(LocalDate.parse(birthDate))
                .email(email)
                .phone(phone)
                .address(address)
                .country(country)
                .city(city)
                .zipCode(zipCode)
                .build();
    }
}
