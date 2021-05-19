package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChangeRoomStatusJobService implements Job {

    @Autowired
    private TechnicalBreakSocketService technicalBreakSocketService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        technicalBreakSocketService.technicalBreakStart();
//        changeRoomStatus();
//        technicalBreakSocketService.technicalBreakEnd();
    }

    private void changeRoomStatus() {
        List<Reservation> collect = reservationRepository
                .findAllByStartOfBooking(LocalDate.now())
                .stream()
                .peek(reservation -> reservation.getRoom().setIsBusy(Boolean.TRUE))
                .collect(Collectors.toList());

        reservationRepository.saveAll(collect);

        try {
            Thread.sleep(160000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Reservation> collect1 = reservationRepository
                .findAllByEndOfBooking(LocalDate.now())
                .stream()
                .peek(reservation -> reservation.getRoom().setIsBusy(Boolean.FALSE))
                .collect(Collectors.toList());

        reservationRepository.saveAll(collect1);
    }
}

