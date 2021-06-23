package bartos.lukasz.bookingservice.application;

import bartos.lukasz.bookingservice.application.service.dataServices.OpinionService;
import bartos.lukasz.bookingservice.application.service.dataServices.ReservationService;
import bartos.lukasz.bookingservice.application.service.dataServices.RoomService;
import bartos.lukasz.bookingservice.application.service.dataServices.UserService;
import bartos.lukasz.bookingservice.application.service.email.EmailPdfFileService;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandLine implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
  
    }
}
