package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@ApiModel(
        value = "ReservationResponseDto",
        description = """
                It is an object representing the status of the room reservation.
                It contains basic information about the reservation, and objects representing the order status and the issued opinion.
                The entity also contains a relationship to the user who made it and the room for which the reservation was made."""
)
public class ReservationResponseDto {

    // PRZEROBIĆ TAK, ŻEBY DODAĆ INFORMACJE O WYNAJMUJĄCYM I KONTAKT DO NIEGO!!!! I TO WYSYŁAĆ NA FRONTEND
    // W SERWISIE POPRAWIĆ TAK, ŻEBY GENEROWAŁO MI REZERWACJĘ DO 3 DNI I POMIĘDZY 3 DNIAMI, A 7.

    private Long id;
    private Long reservationNumber;
    private LocalDate startOfBooking;
    private LocalDate endOfBooking;

    @ApiModelProperty(notes = "Relationship to the room")
    private RoomDto roomDto;
    @ApiModelProperty(notes = "Object represents an opinion issued by a user.")
    private OpinionDto opinionDto;
    @ApiModelProperty(notes = "Object represents a reservation status.")
    private BookingStatusDto bookingStatusDto;
}
