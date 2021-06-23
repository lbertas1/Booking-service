package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(
        value = "ReservationInfoDto",
        description = """
                An object containing basic information about the reservation, such as the total cost, 
                room number or contact information of the client.
                """
)
public class ReservationInfoDto {

    private Long id;
    private Long reservationNumber;
    private LocalDate startOfBooking;
    private LocalDate endOfBooking;

    private Integer roomNumber;

    private PaymentStatus paymentStatus;
    private BigDecimal totalAmountForReservation;

    private String name;
    private String surname;
    private String email;
    private String phone;
}
