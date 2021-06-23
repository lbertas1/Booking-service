package bartos.lukasz.bookingservice.application.dto.events;

import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class OrderEmailData extends EmailData {
    private UserDto userDto;
    private List<ReservationDto> reservationDtos;
    private String orderNumber;
}
