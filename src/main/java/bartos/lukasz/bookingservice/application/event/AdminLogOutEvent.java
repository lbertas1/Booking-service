package bartos.lukasz.bookingservice.application.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class AdminLogOutEvent extends ApplicationEvent {

    private final String adminUsername;

    public AdminLogOutEvent(Object source, String adminUsername) {
        super(source);
        this.adminUsername = adminUsername;
    }
}
