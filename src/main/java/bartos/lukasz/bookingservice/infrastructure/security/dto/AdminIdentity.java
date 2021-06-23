package bartos.lukasz.bookingservice.infrastructure.security.dto;

import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationInfoDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class AdminIdentity extends UserIdentity {
    List<ReservationInfoDto> endingsReservations;
    List<ReservationInfoDto> commencingReservations;
    List<ReservationInfoDto> reservationsStartingIn3Days;
    List<ReservationInfoDto> reservationsStartingIn7Days;
}
