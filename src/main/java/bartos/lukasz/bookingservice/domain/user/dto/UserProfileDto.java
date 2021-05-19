package bartos.lukasz.bookingservice.domain.user.dto;

import bartos.lukasz.bookingservice.domain.reservation.reservationDto.SimpleReservationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserProfileDto {
    private UserResponseDto user;
    private List<SimpleReservationDto> reservations;
}
