package bartos.lukasz.bookingservice.application.listener;

import bartos.lukasz.bookingservice.application.dto.events.RegistrationEmailData;
import bartos.lukasz.bookingservice.application.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void sendActivationEmail(RegistrationEmailData registrationEmailData) {
        emailService.send(registrationEmailData);
    }
}