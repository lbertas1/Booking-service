package bartos.lukasz.bookingservice.application.service.dataServices;

import bartos.lukasz.bookingservice.application.exception.ReservationServiceException;
import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionResponseDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public OpinionResponseDto saveOpinion(OpinionRequestDto opinionRequestDto) {
        Reservation reservation = get(opinionRequestDto.getReservationId()).toReservation();

        if (!Objects.isNull(reservation.getOpinion()))
            throw new ReservationServiceException("Reservation already has an opinion", 400, HttpStatus.BAD_REQUEST);

        reservation.setOpinion(opinionRequestDto.toOpinion());

        ReservationDto saved = reservationRepository.save(reservation).toReservationDto();

        if (!Objects.isNull(saved)) return opinionRequestDto.toOpinionResponseDto();
        else throw new ReservationServiceException("Opinion has not been updated correctly", 400, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public OpinionResponseDto updateOpinion(OpinionRequestDto opinionRequestDto) {
        ReservationDto reservationDto = get(opinionRequestDto.getReservationId());
        reservationDto.setOpinionDto(opinionRequestDto.toOpinionDto());
        ReservationDto saved = reservationRepository.save(reservationDto.toReservation()).toReservationDto();

        if (!Objects.isNull(saved)) return saved.getOpinionDto().toOpinionResponseDto();
        else throw new ReservationServiceException("Opinion has not been updated correctly", 400, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public OpinionResponseDto removeOpinion(Long reservationId) {
        ReservationDto reservationDto = get(reservationId);
        OpinionResponseDto opinionResponseDto = reservationDto.getOpinionDto().toOpinionResponseDto();
        reservationDto.setOpinionDto(null);
        ReservationDto saved = reservationRepository.save(reservationDto.toReservation()).toReservationDto();

        if (!Objects.isNull(saved)) return saved.getOpinionDto().toOpinionResponseDto();
        else throw new ReservationServiceException("Opinion has not been updated correctly", 400, HttpStatus.BAD_REQUEST);
    }

    private ReservationDto get(Long id) {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationServiceException("Reservation doesn't found", 404, HttpStatus.NOT_FOUND))
                .toReservationDto();
    }
}
