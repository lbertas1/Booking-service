package bartos.lukasz.bookingservice.application;

import bartos.lukasz.bookingservice.BookingServiceApplication;
import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.application.service.dataServices.OpinionService;
import bartos.lukasz.bookingservice.application.service.dataServices.ReservationService;
import bartos.lukasz.bookingservice.application.service.dataServices.RoomService;
import bartos.lukasz.bookingservice.application.service.dataServices.UserService;
import bartos.lukasz.bookingservice.application.service.email.EmailPdfFileService;
import bartos.lukasz.bookingservice.application.service.email.EmailService;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationResponseDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import bartos.lukasz.bookingservice.domain.user.dto.RegisterUserDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandLine implements CommandLineRunner {

    private final RoomService roomService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final OpinionService opinionService;
    private final EmailService emailService;
    private final EmailPdfFileService emailPdfFileService;
    private final EntityManager em;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
    }
}
