package bartos.lukasz.bookingservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateUserDto {

    private Long id;
    private String name;
    private String surname;
    private String birthDate;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private String country;
}

