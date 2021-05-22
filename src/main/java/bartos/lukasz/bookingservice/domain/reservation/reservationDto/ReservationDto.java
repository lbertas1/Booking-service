package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;
    private Long reservationNumber;
    private LocalDate startOfBooking;
    private LocalDate endOfBooking;
    private UserDto userDto;
    private RoomDto roomDto;
    private OpinionDto opinionDto;
    private BookingStatusDto bookingStatusDto;

    public void setOpinionDto(OpinionDto opinionDto) {
        this.opinionDto = opinionDto;
    }

    public Reservation toReservation() {
        Reservation build = Reservation.builder()
                .id(id)
                .reservationNumber(reservationNumber)
                .bookingStatus(bookingStatusDto.toBookingStatus())
                .room(roomDto.toRoom())
                .user(userDto.toUser())
                .startOfBooking(startOfBooking)
                .endOfBooking(endOfBooking)
                .build();

        if (!Objects.isNull(opinionDto)) build.setOpinion(opinionDto.toOpinion());
        return build;
    }

    public ReservationResponseDto toReservationResponseDto() {
        ReservationResponseDto reservationResponseDto = ReservationResponseDto
                .builder()
                .id(id)
                .reservationNumber(reservationNumber)
                .bookingStatusDto(bookingStatusDto)
                .roomDto(roomDto)
                .startOfBooking(startOfBooking)
                .endOfBooking(endOfBooking)
                .build();

        if (!Objects.isNull(opinionDto)) reservationResponseDto.setOpinionDto(opinionDto);
        return reservationResponseDto;
    }
}