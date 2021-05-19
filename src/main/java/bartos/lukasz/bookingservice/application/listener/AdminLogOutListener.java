package bartos.lukasz.bookingservice.application.listener;

import bartos.lukasz.bookingservice.application.event.AdminLogOutEvent;
import bartos.lukasz.bookingservice.application.service.ChatService;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminLogOutListener {

    private final LoggedAdminService loggedAdminService;
    private final ChatService chatService;

    @EventListener
    public void onAdminLogin(AdminLogOutEvent adminLogOutEvent) {
        loggedAdminService.markAdminAsUnavailable(adminLogOutEvent.getAdminUsername());
        chatService.endChat(adminLogOutEvent.getAdminUsername());
    }
}
