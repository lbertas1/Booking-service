package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.enums.Break;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechnicalBreakSocketService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public void technicalBreakStart() {
        simpMessageSendingOperations.convertAndSend("/break/technical-break", Break.BREAK_START);
    }

    public void technicalBreakEnd() {
        simpMessageSendingOperations.convertAndSend("/break/technical-break", Break.BREAK_END);
    }
}
