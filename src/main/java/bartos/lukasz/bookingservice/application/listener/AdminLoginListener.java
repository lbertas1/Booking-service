package bartos.lukasz.bookingservice.application.listener;

import bartos.lukasz.bookingservice.application.event.AdminLoginEvent;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminLoginListener {

    private final LoggedAdminService loggedAdminService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void onAdminLogin(AdminLoginEvent adminLoginEvent) {
        loggedAdminService.markAdminAsAvailable(adminLoginEvent.getAdminUsername());
        simpMessageSendingOperations.convertAndSend("/topic/chat", adminLoginEvent.getAdminUsername());
    }
}
