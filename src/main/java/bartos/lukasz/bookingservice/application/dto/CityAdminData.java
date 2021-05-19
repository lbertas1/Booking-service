package bartos.lukasz.bookingservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CityAdminData {
    private String city;
    private String username;
}
