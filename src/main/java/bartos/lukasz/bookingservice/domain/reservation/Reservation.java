package bartos.lukasz.bookingservice.domain.reservation;

import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.BookingStatus;
import bartos.lukasz.bookingservice.domain.reservation.opinion.Opinion;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationResponseDto;
import bartos.lukasz.bookingservice.domain.room.Room;
import bartos.lukasz.bookingservice.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long reservationNumber;
    private LocalDate startOfBooking;
    private LocalDate endOfBooking;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    private Room room;

    @Embedded
    private Opinion opinion;

    @Embedded
    private BookingStatus bookingStatus;

    public void setReservationNumber(Long reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public ReservationDto toReservationDto() {
        ReservationDto reservationDto = ReservationDto
                .builder()
                .id(id)
                .reservationNumber(reservationNumber)
                .bookingStatusDto(bookingStatus.toBookingStatusDto())
                .roomDto(room.toRoomDto())
                .userDto(user.toUserDto())
                .startOfBooking(startOfBooking)
                .endOfBooking(endOfBooking)
                .build();

        if (!Objects.isNull(opinion)) reservationDto.setOpinionDto(opinion.toOpinionDto());
        return reservationDto;
    }

    public ReservationResponseDto toReservationResponseDto() {
        ReservationResponseDto reservationResponseDto = ReservationResponseDto
                .builder()
                .id(id)
                .reservationNumber(reservationNumber)
                .bookingStatusDto(bookingStatus.toBookingStatusDto())
                .roomDto(room.toRoomDto())
                .startOfBooking(startOfBooking)
                .endOfBooking(endOfBooking)
                .build();

        if (!Objects.isNull(opinion)) reservationResponseDto.setOpinionDto(opinion.toOpinionDto());
        return reservationResponseDto;
    }
}

