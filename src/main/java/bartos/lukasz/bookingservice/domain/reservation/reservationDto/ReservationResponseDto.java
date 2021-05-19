package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private Long reservationNumber;
    private LocalDate startOfBooking;
    private LocalDate endOfBooking;
    private RoomDto roomDto;
    private OpinionDto opinionDto;
    private BookingStatusDto bookingStatusDto;
}
